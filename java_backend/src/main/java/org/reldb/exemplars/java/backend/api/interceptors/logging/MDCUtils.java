package org.reldb.exemplars.java.backend.api.interceptors.logging;

import org.slf4j.MDC;

public final class MDCUtils {
    private MDCUtils() {
    }

    public static final String REQUEST_ID_KEY = "X-Request-Id";

    public static void updateMDC(String key, String requestId) {
        MDC.put(key, requestId);
    }

    public static void clearMDC(String key) {
        MDC.remove(key);
    }

    public static String getMDC(String key) {
        return MDC.get(key);
    }
}
