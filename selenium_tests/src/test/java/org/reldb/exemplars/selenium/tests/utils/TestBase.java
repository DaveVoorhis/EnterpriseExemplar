package org.reldb.exemplars.selenium.tests.utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.net.URL;
import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public abstract class TestBase {

    public static final String EXPECTED_TITLE = "Java + Spring Boot × React";

    public static final String USER0 = "alice@example.com";
    public static final String USER1 = "bob@example.com";
    public static final String USER2 = "charlie@examplecom";
    public static final String PASSWORD0 = "pwd";
    public static final String PASSWORD1 = "pwd";
    public static final String PASSWORD2 = "pwd";

    protected static WebDriver driver;

    private final static String SELENIUM_GRID_URL = "SELENIUM_GRID_URL"; // often http://localhost:4444
    private final static String TEST_SITE_URL = "TEST_SITE_URL";  // often http://localhost or http://localhost:5173

    public static String getConfigSetting(String key) {
        return System.getProperty(key, System.getenv().get(key));
    }

    public static String siteURI() {
        return getConfigSetting(TEST_SITE_URL);
    }

    public static URL siteURL() throws Exception {
        var siteURI = siteURI();
        var uri = siteURI != null ? siteURI : "http://localhost:5173";
        return new URI(uri).toURL();
    }

    protected static void home() throws Exception {
        locateTo(siteURL().toString(), EXPECTED_TITLE);
    }

    protected static void locateTo(String url, String expectedTitle) {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(url);

        wait.until(session -> session.getTitle() != null);

        assertThat(driver.getTitle()).isEqualTo(expectedTitle);
    }

    protected static void login(String userId, String password) throws Exception {
        home();

        if (!driver.findElements(By.id("logoutButton")).isEmpty()) {
            logout();
        }

        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        var loginButton = wait.until(session -> session.findElement(By.id("loginButton")));
        loginButton.click();

        wait.until(session -> session.getTitle() != null && session.getTitle().contains("dex"));

        var loginTextbox = wait.until(session -> session.findElement(By.id("login")));
        var passwordTextbox = wait.until(session -> session.findElement(By.id("password")));
        var submitButton = wait.until(session -> session.findElement(By.id("submit-login")));

        loginTextbox.sendKeys("alice@example.com");
        passwordTextbox.sendKeys("pwd");
        submitButton.click();

        wait.until(session -> session.getTitle() != null && session.getTitle().equals(EXPECTED_TITLE));
    }

    protected static void logout() {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        var logoutButton = wait.until(session -> session.findElement(By.id("logoutButton")));
        logoutButton.click();

        var loginButton = wait.until(session -> session.findElement(By.id("loginButton")));
        assertThat(loginButton).isNotNull();
    }

    private static String gridURI() {
        return getConfigSetting(SELENIUM_GRID_URL);
    }

    private static URL gridUrl() throws Exception {
        var gridURI = gridURI();
        var uri = gridURI != null ? gridURI : "http://localhost:4444";
        return new URI(uri).toURL();
    }

    private static MutableCapabilities optionsFor(String browser) {
        return switch (browser.toLowerCase()) {
            case "chrome" -> new ChromeOptions();
            case "firefox" -> new FirefoxOptions();
            case "edge" -> new EdgeOptions();
            case "safari" -> new SafariOptions();
            default -> throw new IllegalArgumentException("Unknown BROWSER: " + browser);
        };
    }

    private static RemoteWebDriver obtainRemoteWebDriver() throws Exception {
        var browser = System.getProperty("BROWSER", "chrome");
        return new RemoteWebDriver(gridUrl(), optionsFor(browser));
    }

    private static WebDriver obtainLocalWebDriver() {
        var browser = System.getProperty("BROWSER", "chrome");
        return switch (browser.toLowerCase()) {
            case "chrome" -> new ChromeDriver();
            case "firefox" -> new FirefoxDriver();
            case "edge" -> new EdgeDriver();
            case "safari" -> new SafariDriver();
            default -> throw new IllegalArgumentException("Unknown BROWSER: " + browser);
        };
    }

    @BeforeAll
    static void createDriver() throws Exception {
        driver = gridURI() != null
                ? obtainRemoteWebDriver()
                : obtainLocalWebDriver();
    }

    @AfterAll
    static void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
