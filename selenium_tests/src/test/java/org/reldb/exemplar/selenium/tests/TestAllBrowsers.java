package org.reldb.exemplar.selenium.tests;

import org.junit.platform.launcher.core.*;
import org.junit.platform.launcher.listeners.*;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.reldb.exemplar.selenium.tests.utils.ProgressListener;

import java.io.PrintWriter;
import java.util.List;

public class TestAllBrowsers {

    private static final String TEST_PACKAGE = "org.reldb.exemplar.selenium.tests";

    private static final List<String> BROWSERS = List.of("chrome", "firefox", "edge");

    public static void main(String[] args) {
        var request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectPackage(TEST_PACKAGE))
                .build();

        long totalFailures = 0;

        for (String browser : BROWSERS) {
            System.setProperty("BROWSER", browser);
            System.out.println("===== Running test suite for " + browser + " =====");

            var out = new PrintWriter(System.out, true);

            var progressListener = new ProgressListener(browser, out);

            var summaryGeneratingListener = new SummaryGeneratingListener();
            try (var session = LauncherFactory.openSession()) {
                var launcher = session.getLauncher();
                launcher.registerTestExecutionListeners(progressListener, summaryGeneratingListener);
                launcher.execute(request);
            }
            summaryGeneratingListener.getSummary().printTo(out);

            totalFailures += summaryGeneratingListener.getSummary().getTotalFailureCount();
        }

        System.exit(totalFailures > 0 ? 1 : 0);
    }
}
