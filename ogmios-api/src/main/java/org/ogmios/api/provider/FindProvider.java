package org.ogmios.api.provider;

import java.util.Iterator;
import java.util.ServiceLoader;

public class FindProvider {

    private ServiceLoader<ConfigurationProvider> loader = ServiceLoader.load(ConfigurationProvider.class);

    public ConfigurationProvider get(boolean refresh){
        if (refresh) {
            loader.reload();
        }
        Iterator<ConfigurationProvider> iterator = loader.iterator();
        while(iterator.hasNext()){
            ConfigurationProvider config = iterator.next();
            return config;
        }
        return null;
    }

}
