package org.ogmios.provider.domain;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigData {

    private Map<String, String> configuration;
    private Map<String, Path> files;

    public ConfigData() {
        configuration=new ConcurrentHashMap<>();
        files=new ConcurrentHashMap<>();
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public Map<String, Path> getFiles() {
        return files;
    }
}
