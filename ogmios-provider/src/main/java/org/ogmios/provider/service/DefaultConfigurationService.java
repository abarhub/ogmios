package org.ogmios.provider.service;

import org.ogmios.api.config.Configuration;
import org.ogmios.api.config.SecretValue;
import org.ogmios.api.exception.ConfigurationException;
import org.ogmios.api.log.LogLevel;
import org.ogmios.api.log.Logger;
import org.ogmios.provider.domain.ConfigData;
import org.ogmios.provider.utils.Constant;
import org.ogmios.provider.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static org.ogmios.provider.utils.Constant.CLASSPATH_PROTOCOL;

public class DefaultConfigurationService implements Configuration {

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
                throw new ConfigurationException("Error for read " + p, e);
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
        addProperties(prop, metaConfiguration);

        List<String> dirList = null;
        if (metaConfiguration.containsKey(Constant.PARAM_OGMIOS_DIRECTORIES)) {
            String directories = metaConfiguration.get(Constant.PARAM_OGMIOS_DIRECTORIES);
            if (directories != null && !directories.trim().isEmpty()) {
                dirList = StringUtils.split(directories);
            }
        }

        Logger.log(LogLevel.DEBUG, "config directory and file is %s", dirList);
        if (dirList != null) {
            for (String dir : dirList) {
                process(dir, configData);
            }
        }
    }

    private void process(String dir, ConfigData configData) throws ConfigurationException {
        Path p=null;
        if (dir.startsWith(CLASSPATH_PROTOCOL)) {
            try {
                String filename = dir.substring(CLASSPATH_PROTOCOL.length());
                URL url = ClassLoader.getSystemResource(filename);
                Logger.log(LogLevel.DEBUG, "url:%s (%s)", url, url.toURI());
                Properties properties = new Properties();
                try (InputStream in = url.openStream()) {
                    properties.load(in);
                }
                addProperties(properties, configData.getConfiguration());
            } catch (URISyntaxException | IOException e) {
                throw new ConfigurationException("Error for path " + dir, e);
            }
        } else {
            p = Paths.get(dir);
        }
        if (p != null && Files.exists(p)) {
            if (endWith(p, ".properties")) {
                Properties properties = new Properties();
                readFile(properties, p);
                addProperties(properties, configData.getConfiguration());
            } else if (endWith(p, ".xml")) {
                configData.getFiles().put(p.getFileName().toString(), p);
            }
        }
    }

    private void addProperties(Properties properties, Map<String, String> configuration) {
        for (Object key : properties.keySet()) {
            if (key instanceof String) {
                String ketString = (String) key;
                String value = properties.getProperty(ketString);
                configuration.put(ketString, value);
            }
        }
    }

    private boolean endWith(Path p, String end) {
        return !end.isEmpty() && p.getFileName().toString().endsWith(end);
    }

    private Properties loadFromSystemProperties() throws ConfigurationException {
        String value = System.getProperty(Constant.SYS_PROP_OGMIOS_PROPERTIES);
        if (!StringUtils.isEmpty(value)) {
            Path path = Paths.get(value);
            if (Files.exists(path)) {
                Logger.log(LogLevel.INFO, "find file %s from system properties", path);
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
        try (InputStream is = DefaultConfigurationService.class.getClassLoader().getResourceAsStream(Constant.OGMIOS_PROPERTIES)) {

            if (is == null) {
                return null;
            }

            Logger.log(LogLevel.INFO, "find file %s in classpath", Constant.OGMIOS_PROPERTIES);

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
            Logger.log(LogLevel.INFO, "find file %s from path", path);
            Properties properties = new Properties();
            readFile(properties, path);
            return properties;
        } else {
            path = Paths.get("./config/" + Constant.OGMIOS_PROPERTIES);
            if (Files.exists(path)) {
                Logger.log(LogLevel.INFO, "find file %s from path", path);
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
