package org.ogmios.api.log;

public class Logger {

    public static void log(LogLevel level, String message, Object[] parameter) {
        System.out.println("" + level + " : " + message);
    }

}
