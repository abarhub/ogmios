package org.ogmios.provider;

import org.ogmios.api.config.Configuration;
import org.ogmios.api.exception.ConfigurationException;
import org.ogmios.api.provider.ConfigurationProvider;
import org.ogmios.provider.service.DefaultConfigurationService;

public class SpiProvider implements ConfigurationProvider {

    private DefaultConfigurationService configuration;
    private boolean init;

    @Override
    public synchronized Configuration getConfiguration(String name) throws ConfigurationException {
        if (!init) {
            initialisation();
            init = true;
        }
        return configuration;
    }

    private void initialisation() throws ConfigurationException {
        configuration = new DefaultConfigurationService();
        configuration.load();
    }
}
