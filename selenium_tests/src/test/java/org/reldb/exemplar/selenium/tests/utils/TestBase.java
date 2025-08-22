package org.reldb.exemplar.selenium.tests.utils;

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

import java.net.URI;
import java.net.URL;

public abstract class TestBase {
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
