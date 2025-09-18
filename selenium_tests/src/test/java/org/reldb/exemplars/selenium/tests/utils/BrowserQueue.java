package org.reldb.exemplars.selenium.tests.utils;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BrowserQueue {
    private static final AbstractQueue<Browser> browsersToTest = new ConcurrentLinkedQueue<>();
    private static final AbstractQueue<Browser> browsersTested = new ConcurrentLinkedQueue<>();

    public static void add(String[] browsers) {
        Arrays.stream(browsers).forEach(browser -> browsersToTest.add(new Browser(browser)));
    }

    public synchronized static Browser getOrDefault(String defaultItem) {
        try {
            var browserToTest = browsersToTest.remove();
            browsersTested.add(browserToTest);
            return browserToTest;
        } catch (NoSuchElementException nsee) {
            return new Browser(defaultItem);
        }
    }

    public static void quit() {
        browsersToTest.forEach(Browser::quitDriver);
        browsersTested.forEach(Browser::quitDriver);
    }
}
