package org.ogmios.api;

import org.ogmios.api.config.Configuration;
import org.ogmios.api.exception.ConfigurationException;
import org.ogmios.api.log.LogLevel;
import org.ogmios.api.log.Logger;
import org.ogmios.api.provider.ConfigurationProvider;
import org.ogmios.api.provider.FindProvider;

public class OgmiosConfiguration {

    private static Configuration configuration;
    private static boolean initialized = false;
    private static ConfigurationProvider configurationProvider;

    public static Configuration getConfiguration(String name) throws ConfigurationException {
        initialization();
        return configurationProvider.getConfiguration(name);
    }

    private static void initialization() {
        if (!initialized) {
            FindProvider findProvider = new FindProvider();
            configurationProvider = findProvider.get(false);
            if (configurationProvider == null) {
                Logger.log(LogLevel.ERROR, "No configuration provider found");
            }
        }
    }

}
