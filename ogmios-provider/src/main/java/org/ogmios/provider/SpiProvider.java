package org.ogmios.provider;

import org.ogmios.api.config.Configuration;
import org.ogmios.api.exception.ConfigurationException;
import org.ogmios.api.provider.ConfigurationProvider;
import org.ogmios.provider.service.DefaultConfiguration;

public class SpiProvider implements ConfigurationProvider {

    private DefaultConfiguration configuration;
    private boolean init;

    @Override
    public Configuration getConfiguration(String name) throws ConfigurationException {
        if(!init){
            initialisation();
            init=true;
        }
        return configuration;
    }

    private void initialisation() throws ConfigurationException {
        configuration=new DefaultConfiguration();
        configuration.load();
    }
}
