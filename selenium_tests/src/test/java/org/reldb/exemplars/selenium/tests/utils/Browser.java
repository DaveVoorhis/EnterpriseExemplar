package org.reldb.exemplars.selenium.tests.utils;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Browser {
    private final static String SELENIUM_GRID_URL = "SELENIUM_GRID_URL"; // often http://localhost:4444

    private final WebDriver driver;

    private String gridURI() {
        return Config.getConfigSetting(SELENIUM_GRID_URL);
    }

    public Browser(String name) {
        driver = gridURI() != null
                ? obtainRemoteWebDriver(name)
                : obtainLocalWebDriver(name);
    }

    public WebDriver getDriver() {
        return driver;
    }

    private URL gridUrl() {
        var gridURI = gridURI();
        var uri = gridURI != null ? gridURI : "http://localhost:4444";
        try {
            return new URI(uri).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException("Invalid URI %s".formatted(uri), e);
        }
    }

    private WebDriver obtainLocalWebDriver(String browser) {
        return switch (browser) {
            case "chrome" -> new ChromeDriver();
            case "firefox" -> new FirefoxDriver();
            case "edge" -> new EdgeDriver();
            case "safari" -> new SafariDriver();
            default -> throw new IllegalArgumentException("Unknown browser: " + browser);
        };
    }

    private MutableCapabilities optionsFor(String browser) {
        return switch (browser) {
            case "chrome" -> new ChromeOptions();
            case "firefox" -> new FirefoxOptions();
            case "edge" -> new EdgeOptions();
            case "safari" -> new SafariOptions();
            default -> throw new IllegalArgumentException("Unknown browser: " + browser);
        };
    }

    private RemoteWebDriver obtainRemoteWebDriver(String browser) {
        return new RemoteWebDriver(gridUrl(), optionsFor(browser));
    }
}
