package org.ogmios.provider.service;

import org.ogmios.api.config.Configuration;
import org.ogmios.api.config.SecretValue;
import org.ogmios.api.exception.ConfigurationException;
import org.ogmios.provider.domain.ConfigData;
import org.ogmios.provider.utils.Constant;
import org.ogmios.provider.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultConfiguration implements Configuration {

    private Map<String, String> metaConfiguration;
    private ConfigData configData = new ConfigData();

    @Override
    public String get(String name) {
        return configData.getConfiguration().get(name);
    }

    @Override
    public <T> T get(String name, Class<T> returnType) {
        return null;
    }

    @Override
    public SecretValue getSecret(String name) {
        return null;
    }

    @Override
    public Optional<Reader> getConfigFile(String name) throws ConfigurationException {
        if (configData.getFiles().containsKey(name)) {
            return Optional.empty();
        } else {
            Path p = configData.getFiles().get(name);
            try {
                return Optional.of(Files.newBufferedReader(p));
            } catch (IOException e) {
                throw new ConfigurationException("Error for read file " + p, e);
            }
        }
    }

    public void load() throws ConfigurationException {
        Properties prop;

        prop = loadFromSystemProperties();
        if (prop == null) {
            prop = loadFromClasspath();
        }
        if (prop == null) {
            prop = loadFromPath();
        }
        if (prop == null) {
            throw new ConfigurationException("Can't find config file " + Constant.OGMIOS_PROPERTIES);
        }

        metaConfiguration = new ConcurrentHashMap<>();
        for (Object key : prop.keySet()) {
            if (key instanceof String) {
                String ketString = (String) key;
                String value = prop.getProperty(ketString);
                metaConfiguration.put(ketString, value);
            }
        }

        List<String> dirList = null;
        if (metaConfiguration.containsKey(Constant.PARAM_OGMIOS_DIRECTORIES)) {
            String directories = metaConfiguration.get(Constant.PARAM_OGMIOS_DIRECTORIES);
            if (directories != null && !directories.trim().isEmpty()) {
                dirList = StringUtils.split(directories);
            }
        }

        if (dirList != null) {
            for (String dir : dirList) {
                process(dir, configData);
            }
        }
    }

    private void process(String dir, ConfigData configData) throws ConfigurationException {
        Path p = Paths.get(dir);
        if (Files.exists(p)) {
            if (p.endsWith(".properties")) {
                Properties properties = new Properties();
                readFile(properties, p);
                for (Object key : properties.keySet()) {
                    if (key instanceof String) {
                        String ketString = (String) key;
                        String value = properties.getProperty(ketString);
                        configData.getConfiguration().put(ketString, value);
                    }
                }
            } else if (p.endsWith(".xml")) {
                configData.getFiles().put(p.getFileName().toString(), p);
            }
        }
    }

    private Properties loadFromSystemProperties() throws ConfigurationException {
        String value = System.getProperty(Constant.SYS_PROP_OGMIOS_PROPERTIES);
        if (!StringUtils.isEmpty(value)) {
            Path path = Paths.get(value);
            if (Files.exists(path)) {
                Properties properties = new Properties();
                readFile(properties, path);
                return properties;
            } else {
                throw new ConfigurationException("File " + path + " not exists");
            }
        }
        return null;
    }

    private Properties loadFromClasspath() throws ConfigurationException {
        Properties prop;
        try (InputStream is = DefaultConfiguration.class.getClassLoader().getResourceAsStream(Constant.OGMIOS_PROPERTIES)) {

            if (is == null) {
                return null;
            }

            prop = new Properties();
            prop.load(is);

        } catch (IOException e) {
            throw new ConfigurationException("Can't acces file " + Constant.OGMIOS_PROPERTIES, e);
        }
        return prop;
    }

    private Properties loadFromPath() throws ConfigurationException {
        Path path;
        path = Paths.get("./" + Constant.OGMIOS_PROPERTIES);
        if (Files.exists(path)) {
            Properties properties = new Properties();
            readFile(properties, path);
            return properties;
        } else {
            path = Paths.get("./config/" + Constant.OGMIOS_PROPERTIES);
            if (Files.exists(path)) {
                Properties properties = new Properties();
                readFile(properties, path);
                return properties;
            }

        }
        return null;
    }

    private void readFile(Properties prop, Path path) throws ConfigurationException {
        try (InputStream is = Files.newInputStream(path)) {

            prop.load(is);

        } catch (IOException e) {
            throw new ConfigurationException("Can't acces file " + path, e);
        }
    }

}
