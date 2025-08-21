package org.reldb.exemplar.selenium.tests.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.net.URL;

public abstract class TestBase {
    protected WebDriver driver;

    private final static String SELENIUM_GRID_URL = "SELENIUM_GRID_URL";

    private static URL gridUrl() throws Exception {
        String url = System.getProperty(SELENIUM_GRID_URL,
                System.getenv().getOrDefault(SELENIUM_GRID_URL, "http://localhost:4444"));
        return new URI(url).toURL();
    }

    private static MutableCapabilities optionsFor(String browser) {
        return switch (browser.toLowerCase()) {
            case "chrome" -> new ChromeOptions();
            case "firefox" -> new FirefoxOptions();
            case "edge" -> new EdgeOptions();
            default -> throw new IllegalArgumentException("Unknown BROWSER: " + browser);
        };
    }

    @BeforeEach
    void createDriver() throws Exception {
        String browser = System.getProperty("BROWSER", "chrome");
        driver = new RemoteWebDriver(gridUrl(), optionsFor(browser));
    }

    @AfterEach
    void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
