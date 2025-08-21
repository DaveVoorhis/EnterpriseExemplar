package org.reldb.exemplar.selenium.tests.utils;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

import java.io.PrintWriter;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class ProgressListener implements TestExecutionListener {
    private final String browser;
    private final PrintWriter out;
    private final Map<String, Long> startNanos = new ConcurrentHashMap<>();
    private final Object lock = new Object();

    public ProgressListener(String browser, PrintWriter out) {
        this.browser = browser;
        this.out = out;
    }

    @Override
    public void executionStarted(TestIdentifier id) {
        if (id.isTest()) {
            startNanos.put(id.getUniqueId(), System.nanoTime());
            synchronized (lock) {
                out.printf("→ [%s] START %s%n", browser, displayName(id));
            }
        }
    }

    @Override
    public void executionFinished(TestIdentifier id, TestExecutionResult result) {
        if (!id.isTest()) {
            return;
        }

        var millisecondDuration = -1L;
        var started = startNanos.remove(id.getUniqueId());
        if (started != null) {
            millisecondDuration = Duration.ofNanos(System.nanoTime() - started).toMillis();
        }

        var symbol = switch (result.getStatus()) {
            case SUCCESSFUL -> "✔";
            case ABORTED    -> "⚠";
            case FAILED     -> "✖";
            default         -> "?";
        };

        synchronized (lock) {
            out.printf("%s [%s] %s (%d ms)%n", symbol, browser, displayName(id), millisecondDuration);
            result.getThrowable().ifPresent(this::dumpThrowable);
        }
    }

    private void dumpThrowable(Throwable throwable) {
        out.printf("    %s: %s%n", throwable.getClass().getSimpleName(),
                throwable.getMessage() == null
                        ? ""
                        : throwable.getMessage());
        var trace = throwable.getStackTrace();
        IntStream.range(0, Math.min(5, trace.length)).forEach(index ->
                out.printf("      at %s%n", trace[index]));
    }

    private String displayName(TestIdentifier id) {
        return id.getSource()
                .flatMap(this::methodSignature)
                .orElse(id.getDisplayName());
    }

    private java.util.Optional<String> methodSignature(TestSource src) {
        return src instanceof MethodSource ms
                ? java.util.Optional.of(obtainSimpleClassName(ms) + "#" + ms.getMethodName())
                : java.util.Optional.empty();
    }

    private Object obtainSimpleClassName(MethodSource ms) {
        return ms
                .getClassName()
                .substring(ms.getClassName().lastIndexOf('.') + 1);
    }
}

