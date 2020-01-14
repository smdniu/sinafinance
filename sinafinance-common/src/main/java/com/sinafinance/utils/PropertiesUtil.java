package com.sinafinance.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description
 * Author wenchao02
 * Time 2019/4/22 14:57
 */
public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static final String PROPERTIES_SUFFIX = ".properties";
    private static Map<String, Properties> propertiesMap =
            new HashMap<String, Properties>();

    public static Properties getProperties(String fileName) {
        if (propertiesMap.containsKey(fileName)) {
            return propertiesMap.get(fileName);
        }
        Properties properties = new Properties();
        try {
            InputStream inputStream = PropertiesUtil.class.getClassLoader()
                    .getResourceAsStream(fileName + PROPERTIES_SUFFIX);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            logger.error("PropertiesUtil#getProperties error", e);
        }
        propertiesMap.put(fileName, properties);
        return properties;
    }

}
