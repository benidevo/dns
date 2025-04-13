package dns.config;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code LoggingConfig} class provides utility methods for configuring
 * and retrieving loggers for the application.
 * <p>
 * This class is designed to simplify the setup of logging configurations
 * and ensure consistent logging behavior across the application.
 * </p>
 * <p>
 * The {@link #initialize()} method configures the root logger to output log
 * messages to the console with a specified logging level. The
 * {@link #getLogger(Class)}
 * method provides a convenient way to retrieve a logger instance for a specific
 * class.
 * </p>
 * <p>
 * Example usage:
 *
 * <pre>
 * {@code
 * LoggingConfig.initialize();
 * Logger logger = LoggingConfig.getLogger(MyClass.class);
 * logger.info("This is an info message.");
 * }
 * </pre>
 * </p>
 */
public class LoggingConfig {
    /**
     * Initializes the logging configuration for the application.
     * <p>
     * This method configures the root logger by removing all existing handlers
     * and adding a new {@link ConsoleHandler} with the logging level set to
     * {@link Level#INFO}.
     * It also sets the root logger's logging level to {@link Level#INFO}.
     * </p>
     * <p>
     * This ensures that log messages are output to the console with the specified
     * logging level.
     * </p>
     */
    public static void initialize() {
        Logger rootLogger = Logger.getLogger("");

        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        rootLogger.addHandler(consoleHandler);

        rootLogger.setLevel(Level.INFO);
    }

    /**
     * Retrieves a logger instance for the specified class.
     *
     * @param clazz the class for which the logger is to be retrieved
     * @return a {@link Logger} instance associated with the specified class
     */
    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }

}
