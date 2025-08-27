package hr.lknezevic.entitygen.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for logging messages at different levels.
 * This class provides static methods to log messages using SLF4J.
 */
@Slf4j
public class LoggingUtility {

    private LoggingUtility() {
        // Private constructor to prevent instantiation
    }

    public static void debug(String message) {
        log.debug(message);
    }

    public static void debug(String message, Object... args) {
        log.debug(message, args);
    }

    public static void info(String message) {
        log.info(message);
    }

    public static void info(String message, Object... args) {
        log.info(message, args);
    }

    public static void warn(String message) {
        log.warn(message);
    }

    public static void warn(String message, Object... args) {
        log.warn(message, args);
    }

    public static void error(String message) {
        log.error(message);
    }

    public static void error(String message, Object... args) {
        log.error(message, args);
    }

}
