package org.ogmios.example;

import com.google.common.base.Preconditions;
import org.ogmios.api.OgmiosConfiguration;
import org.ogmios.api.config.Configuration;
import org.ogmios.api.exception.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ConfigurationException {
        LOGGER.info("Hello");

        Configuration configuration = OgmiosConfiguration.getConfiguration("aaa");

        Preconditions.checkNotNull(configuration);
        LOGGER.info("key1:{}", configuration.get("key1"));
        LOGGER.info("key2:{}", configuration.get("key2"));
        LOGGER.info("key0:{}", configuration.get("key0"));

        LOGGER.info("Bye");
    }

}
