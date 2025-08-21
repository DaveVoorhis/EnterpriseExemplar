package org.reldb.exemplars.java.backend.api.interceptors.logging;

import static org.reldb.exemplars.java.backend.api.interceptors.logging.MDCUtils.REQUEST_ID_KEY;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.List;
import org.slf4j.LoggerFactory;

public class LogAppenderForTests extends ListAppender<ILoggingEvent> {

    public static LogAppenderForTests initialize() {
        final var appender = new LogAppenderForTests();
        appender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        final var logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.DEBUG);
        logger.addAppender(appender);
        appender.start();
        return appender;
    }

    public List<String> loggedMessages(Level level) {
        return list.stream()
                .filter(event -> event.getLevel().equals(level))
                .map(this::format)
                .toList();
    }

    private String format(ILoggingEvent event) {
        final var formatted = new StringBuilder();
        final var mdc = event.getMDCPropertyMap();
        if (mdc.containsKey(REQUEST_ID_KEY)) {
            formatted.append(mdc.get(REQUEST_ID_KEY));
            formatted.append(": ");
        }
        formatted.append(event.getFormattedMessage());
        return formatted.toString();
    }
}
