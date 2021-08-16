package org.ogmios.api.config;

public interface SecretValue extends AutoCloseable {

    char[] getValue();

}
