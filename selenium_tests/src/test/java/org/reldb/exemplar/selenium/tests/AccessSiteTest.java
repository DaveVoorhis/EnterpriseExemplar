package org.reldb.exemplar.selenium.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.reldb.exemplar.selenium.tests.utils.TestBase;

import java.time.Duration;

public class AccessSiteTest extends TestBase {

    public static final String EXPECTED_TITLE = "Java + Spring Boot × React";

    @Test
    void canOpenApplication() throws Exception {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(siteURL().toString());

        wait.until(session -> session.getTitle() != null && session.getTitle().contains(EXPECTED_TITLE));
    }

    @Test
    void canAccessTabs() throws Exception {
        driver.get(siteURL().toString());

        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(session -> session.getTitle() != null && session.getTitle().contains(EXPECTED_TITLE));

        var tabs = driver.findElements(By.className("tab-button"));

        var demoTab = tabs.get(0);
        var usersTab = tabs.get(1);
        var rolesTab = tabs.get(2);
        var miscellaneousTab = tabs.get(3);

        demoTab.click();
        wait.until(session -> session.findElement(By.id("demo_content")));

        usersTab.click();
        wait.until(session -> session.findElement(By.id("users_content")));

        rolesTab.click();
        wait.until(session -> session.findElement(By.id("roles_content")));

        miscellaneousTab.click();
        wait.until(session -> session.findElement(By.id("miscellaneous_content")));
    }
}
