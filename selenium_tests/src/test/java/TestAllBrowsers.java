import org.junit.platform.launcher.core.*;
import org.junit.platform.launcher.listeners.*;
import org.junit.platform.reporting.open.xml.OpenTestReportGeneratingListener;
import org.reldb.exemplar.selenium.tests.utils.ProgressListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class TestAllBrowsers {

    private static final String TEST_PACKAGE = "org.reldb.exemplar.selenium.tests";

    private static final List<String> BROWSERS = List.of("chrome", "firefox", "edge");

    public static void main(String[] args) throws IOException {

        var browsersRun = 0L;
        var totalTests = 0L;
        var totalPasses = 0L;
        var totalSkips = 0L;
        var totalFailures = 0L;

        for (String browser : BROWSERS) {
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
                    .selectors(selectPackage(TEST_PACKAGE))
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
}
