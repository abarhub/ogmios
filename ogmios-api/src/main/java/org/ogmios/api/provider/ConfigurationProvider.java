package org.ogmios.api.provider;

import org.ogmios.api.config.Configuration;
import org.ogmios.api.exception.ConfigurationException;

public interface ConfigurationProvider {

    Configuration getConfiguration(String name) throws ConfigurationException;

}
