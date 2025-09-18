package org.reldb.exemplars.selenium.tests.utils;

import org.openqa.selenium.*;
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

    private final static String TEST_SITE_URL = "TEST_SITE_URL";  // often http://localhost or http://localhost:5173

    protected WebDriver driver = BrowserQueue.getOrDefault("chrome").getDriver();

    public String siteURI() {
        return Browser.getConfigSetting(TEST_SITE_URL);
    }

    public URL siteURL() throws Exception {
        var siteURI = siteURI();
        var uri = siteURI != null ? siteURI : "http://localhost:5173";
        return new URI(uri).toURL();
    }

    protected void home() throws Exception {
        locateTo(siteURL().toString(), EXPECTED_TITLE);
    }

    protected void locateTo(String url, String expectedTitle) {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(url);

        wait.until(session -> session.getTitle() != null);

        assertThat(driver.getTitle()).isEqualTo(expectedTitle);
    }

    protected void login(String userId, String password) throws Exception {
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

    protected void logout() {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        var logoutButton = wait.until(session -> session.findElement(By.id("logoutButton")));
        logoutButton.click();

        var loginButton = wait.until(session -> session.findElement(By.id("loginButton")));
        assertThat(loginButton).isNotNull();
    }
}
