package org.reldb.exemplar.selenium.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.reldb.exemplar.selenium.tests.utils.TestBase;

import java.time.Duration;

public class ExampleTest extends TestBase {

    @Test
    void canOpenExample() {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.get("http://example.com");
        wait.until(session -> session.getTitle().toLowerCase().contains("example"));
        Assertions.assertTrue(driver.getTitle().toLowerCase().contains("example"));
    }
}
