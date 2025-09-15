import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.*;
import org.junit.platform.launcher.listeners.*;
import org.junit.platform.reporting.open.xml.OpenTestReportGeneratingListener;
import org.reldb.exemplars.selenium.tests.utils.ProgressListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class TestAllBrowsers {

    private static final String TEST_PACKAGE = "org.reldb.exemplars.selenium.tests";

    private static final String PROPERTY_FILE = "test.properties";

    private static final String PROPERTY_BROWSER_WINDOWS = "browsers.windows";
    private static final String PROPERTY_BROWSER_MACOS = "browsers.macos";
    private static final String PROPERTY_BROWSER_LINUX = "browsers.linux";
    private static final String PROPERTY_BROWSER_REMOTE = "browsers.remote";

    public static void main(String[] args) throws IOException {

        var properties = loadProperties();

        var system = System.getProperty("os.name");

        System.out.println("Running on " + system);

        var browserSet = PROPERTY_BROWSER_LINUX;
        if (system.contains("Windows")) {
            browserSet = PROPERTY_BROWSER_WINDOWS;
        } else if (system.contains("Mac")) {
            browserSet = PROPERTY_BROWSER_MACOS;
        } else if (System.getenv().get("SELENIUM_GRID_URL") != null) {
            browserSet = PROPERTY_BROWSER_REMOTE;
        }

        var browsersString = properties.getProperty(browserSet, "chrome,firefox");

        System.out.println("Testing browsers: " + browsersString);

        var browsersRun = 0L;
        var totalTests = 0L;
        var totalPasses = 0L;
        var totalSkips = 0L;
        var totalFailures = 0L;

        var browsers = browsersString.split(",");
        for (String browser : browsers) {
            System.setProperty("BROWSER", browser);
            System.out.println("===== Running test suite for " + browser + " =====");

            var out = new PrintWriter(System.out, true);

            var progressListener = new ProgressListener(browser, out);

            var launcherConfig = LauncherConfig.builder()
                    .enableTestExecutionListenerAutoRegistration(true)
                    .build();

            Path reportsDir = Paths.get("selenium_tests", "target", "reports", "test-results", browser);
            Files.createDirectories(reportsDir);

            var request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(DiscoverySelectors.selectPackage(TEST_PACKAGE))
                    .configurationParameter("junit.platform.reporting.open.xml.enabled", "true")
                    .configurationParameter("junit.platform.reporting.output.dir", reportsDir.toAbsolutePath().toString())
                    .build();

            var openXmlListener = new OpenTestReportGeneratingListener();

            var summaryGeneratingListener = new SummaryGeneratingListener();
            try (var session = LauncherFactory.openSession(launcherConfig)) {
                var launcher = session.getLauncher();
                launcher.registerTestExecutionListeners(progressListener, summaryGeneratingListener, openXmlListener);
                launcher.execute(request);
            }
            summaryGeneratingListener.getSummary().printTo(out);

            browsersRun++;

            var summary = summaryGeneratingListener.getSummary();
            totalTests += summary.getTestsFoundCount();
            totalSkips += summary.getTestsSkippedCount();
            totalPasses += summary.getTestsSucceededCount();
            totalFailures += summary.getTotalFailureCount();
        }

        System.out.println();
        System.out.printf("Browsers: %s; Tests: %s; Passed: %s; Failed: %s; Skipped: %s",
                browsersRun, totalTests, totalPasses, totalFailures, totalSkips);
        System.out.println();

        System.exit(totalFailures > 0 ? 1 : 0);
    }

    private static Properties loadProperties() throws IOException {
        var properties = new Properties();
        var loader = Thread.currentThread().getContextClassLoader();
        try (var stream = loader.getResourceAsStream(PROPERTY_FILE)) {
            properties.load(stream);
            return properties;
        }
    }
}
