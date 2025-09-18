package org.reldb.exemplars.selenium.tests.utils;

public class Config {
    public static String getConfigSetting(String key) {
        return System.getProperty(key, System.getenv().get(key));
    }
}
