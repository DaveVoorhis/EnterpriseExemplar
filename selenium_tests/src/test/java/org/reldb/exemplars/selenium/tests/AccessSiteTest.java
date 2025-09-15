package org.reldb.exemplars.selenium.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.reldb.exemplars.selenium.tests.utils.TestBase;

import java.time.Duration;

public class AccessSiteTest extends TestBase {

    @Test
    @Disabled("Only enable for debugging.")
    void keepBrowserAlive() throws Exception {
        driver.get("about:blank");
        var wait = new WebDriverWait(driver, Duration.ofDays(10));
        wait.until(session -> session.findElements(By.className("does-not-exist")).size() == 1);
    }

    @Test
    void canOpenApplication() throws Exception {
        home();
    }

    @Test
    void canLoginAndLogout() throws Exception {
        login(USER0, PASSWORD0);
        logout();
    }

    @Test
    void canAccessTabs() throws Exception {
        login(USER0, PASSWORD0);

        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(session -> session.findElements(By.className("tab-button")).size() == 4);

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

        logout();
    }
}
