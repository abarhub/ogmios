package org.ogmios.api.log;

import java.time.LocalDateTime;

public class Logger {

    private static LogLevel logLevelSelected = null;

    public static void log(LogLevel level, String message, Object... parameter) {
        if (showLogLevel(level)) {
            if (parameter != null && parameter.length > 0) {
                message = String.format(message, (parameter));
            }
            System.out.printf("%s %s %s\n", LocalDateTime.now(), level, message);
        }
    }

    private static boolean showLogLevel(LogLevel level) {
        LogLevel logLevelSelected = getLogLevelSelected();
        return greaterThan(level, logLevelSelected);
    }

    private static boolean greaterThan(LogLevel level, LogLevel level2) {
        return level.getNo() >= level2.getNo();
    }

    private static LogLevel getLogLevelSelected() {
        if (Logger.logLevelSelected == null) {
            Logger.logLevelSelected = LogLevel.DEFAULT;
            String logLevel = System.getProperty("CONFIG_LOG_LEVEL");
            if (logLevel != null && !logLevel.trim().isEmpty()) {
                LogLevel logLevel2 = LogLevel.get(logLevel);
                if (logLevel2 != null) {
                    Logger.logLevelSelected = logLevel2;
                }
            }
        }
        return Logger.logLevelSelected;
    }

}
