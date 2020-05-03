package com.securenative.config;

import com.securenative.exceptions.SecureNativeConfigException;
import com.securenative.SecureNative;
import com.securenative.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ConfigurationManager {
    private static final String DEFAULT_CONFIG_FILE = "securenative.properties";
    private static final String CUSTOM_CONFIG_FILE_ENV_NAME = "SECURENATIVE_CONFIG_FILE";

    private static String getEnvOrDefault(String envName, String defaultValue) {
        String envValue = System.getenv(envName);
        if (envValue != null) {
            return envValue;
        }

        String propValue = System.getProperty(envName);
        if (propValue != null) {
            return propValue;
        }
        return defaultValue;
    }

    private static Properties loadProperties(Properties properties, InputStream inputStream) {
        try (final InputStream stream = inputStream) {
            properties.load(stream);
        } catch (IOException e) {
            return new Properties();
        }
        return properties;
    }

    private static Properties readResourceFile(String resourcePath) {
        Properties properties = new Properties();
        URL resourceUrl = SecureNative.class.getClassLoader().getResource(resourcePath);
        if (resourceUrl != null) {
            InputStream resourceStream = SecureNative.class.getClassLoader().getResourceAsStream(resourcePath);
            properties = loadProperties(properties, resourceStream);
        }
        return properties;
    }

    private static String getPropertyOrEnvOrDefault(Properties properties, String key, Object defaultValue){
        String defaultStrValue = defaultValue == null? null: defaultValue.toString();
        Object  res = properties.getOrDefault(key, getEnvOrDefault(key, defaultStrValue));
        return res == null? null: res.toString();
    }

    public static SecureNativeConfigurationBuilder configBuilder(){
        return  SecureNativeConfigurationBuilder.defaultConfigBuilder();
    }

    public static SecureNativeOptions loadConfig() throws SecureNativeConfigException {
        SecureNativeConfigurationBuilder builder =  SecureNativeConfigurationBuilder.defaultConfigBuilder();
        SecureNativeOptions defaultOptions = builder.build();
        String resourceFilePath = getEnvOrDefault(CUSTOM_CONFIG_FILE_ENV_NAME, DEFAULT_CONFIG_FILE);
        Properties properties = readResourceFile(resourceFilePath);


        builder.withApiKey(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_API_KEY",  defaultOptions.getApiKey()))
                .withApiUrl(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_API_URL", defaultOptions.getApiUrl()))
                .withInterval(Utils.parseIntegerOrDefault(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_INTERVAL", defaultOptions.getInterval()), defaultOptions.getInterval()))
                .withMaxEvents(Utils.parseIntegerOrDefault(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_MAX_EVENTS", defaultOptions.getMaxEvents()), defaultOptions.getMaxEvents()))
                .withTimeout(Utils.parseIntegerOrDefault(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_TIMEOUT", defaultOptions.getTimeout()), defaultOptions.getTimeout()))
                .withAutoSend(Utils.parseBooleanOrDefault(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_AUTO_SEND", defaultOptions.getAutoSend()), defaultOptions.getAutoSend()))
                .withDisable(Utils.parseBooleanOrDefault(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_DISABLE", defaultOptions.getDisabled()), defaultOptions.getDisabled()))
                .withLogLevel(getPropertyOrEnvOrDefault(properties, "SECURENATIVE_LOG_LEVEL", defaultOptions.getLogLevel()));

        return builder.build();
    }
}

