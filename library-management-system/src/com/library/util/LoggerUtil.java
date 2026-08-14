package com.library.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Centralized logging utility for the Library Management System.
 * Configures formatted console logging using java.util.logging.
 */
public class LoggerUtil {

    private static final Formatter CUSTOM_FORMATTER = new Formatter() {
        @Override
        public String format(LogRecord record) {
            return String.format("[%1$tF %1$tT] [%2$-7s] [%3$s] %4$s%n",
                    record.getMillis(),
                    record.getLevel().getLocalizedName(),
                    record.getLoggerName(),
                    record.getMessage());
        }
    };

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setUseParentHandlers(false);

        // Remove existing handlers to avoid duplicate logs
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(CUSTOM_FORMATTER);
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.ALL);

        return logger;
    }
}
