package org.ogmios.api;

import org.ogmios.api.config.Configuration;
import org.ogmios.api.provider.ConfigurationProvider;
import org.ogmios.api.provider.FindProvider;

public class OgmiosConfiguration {

    private static Configuration configuration;
    private static boolean initialized=false;
    private static ConfigurationProvider configurationProvider;

    public static Configuration getConfiguration(String name) {
        initialization();
        return configuration;
    }

    private static void initialization() {
        if(!initialized){
            FindProvider findProvider=new FindProvider();
            configurationProvider=findProvider.get(false);
        }
    }

}
