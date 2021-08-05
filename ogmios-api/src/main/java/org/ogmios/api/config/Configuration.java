package org.ogmios.api.config;

import org.ogmios.api.exception.ConfigurationException;

import java.io.Reader;
import java.util.Optional;

public interface Configuration {

    String get(String name);

    <T> T get(String name, Class<T> returnType);

    SecretValue getSecret(String name);

    Optional<Reader> getConfigFile(String name) throws ConfigurationException;

}
