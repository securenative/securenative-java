package com.securenative.snlogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.models.SecureNativeOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ConfigurationManager {
    static SecureNativeOptions config;
    static String CONFIG_FILE = "securenative.json";

    public static SecureNativeOptions getConfig() {
        if (config == null) {
            SecureNativeOptions fileConfig = readConfigFile();

            if (!fileConfig.getApiUrl().isEmpty()) {
                config.setApiUrl(fileConfig.getApiUrl());
            } else {
                if (!System.getenv("SECURENATIVE_API_URL").isEmpty()) {
                    config.setApiUrl(System.getenv("SECURENATIVE_API_URL"));
                } else {
                    config.setApiUrl("https://api.securenative.com/collector/api/v1");
                }
            }

            if (!fileConfig.getApiKey().isEmpty()) {
                config.setApiKey(fileConfig.getApiKey());
            } else {
                if (!System.getenv("SECURENATIVE_API_KEY").isEmpty()) {
                    config.setApiKey(System.getenv("SECURENATIVE_API_KEY"));
                } else {
                    config.setApiKey(null);
                }
            }

            if (!fileConfig.getAppName().isEmpty()) {
                config.setAppName(fileConfig.getAppName());
            } else {
                if (!System.getenv("SECURENATIVE_APP_NAME").isEmpty()) {
                    config.setAppName(System.getenv("SECURENATIVE_APP_NAME"));
                } else {
                    config.setAppName("");
                }
            }

            if (fileConfig.getInterval() != 0) {
                config.setInterval(fileConfig.getInterval());
            } else {
                if (!System.getenv("SECURENATIVE_INTERVAL").isEmpty()) {
                    config.setInterval(Integer.parseInt(System.getenv("SECURENATIVE_INTERVAL")));
                } else {
                    config.setInterval(1000);
                }
            }

            if (fileConfig.getMaxEvents() != 0) {
                config.setMaxEvents(fileConfig.getMaxEvents());
            } else {
                if (!System.getenv("SECURENATIVE_MAX_EVENTS").isEmpty()) {
                    config.setMaxEvents(Integer.parseInt(System.getenv("SECURENATIVE_MAX_EVENTS")));
                } else {
                    config.setMaxEvents(1000);
                }
            }

            if (fileConfig.getTimeout() != 0) {
                config.setTimeout(fileConfig.getTimeout());
            } else {
                if (!System.getenv("SECURENATIVE_TIMEOUT").isEmpty()) {
                    config.setTimeout(Integer.parseInt(System.getenv("SECURENATIVE_TIMEOUT")));
                } else {
                    config.setTimeout(1500);
                }
            }

            if (fileConfig.getAutoSend() == null) {
                config.setAutoSend(fileConfig.getAutoSend());
            } else {
                if (!System.getenv("SECURENATIVE_AUTO_SEND").isEmpty()) {
                    config.setAutoSend(Boolean.parseBoolean(System.getenv("SECURENATIVE_AUTO_SEND")));
                } else {
                    config.setAutoSend(true);
                }
            }

            if (fileConfig.getAgentDisable() == null) {
                config.setAgentDisable(fileConfig.getAgentDisable());
            } else {
                if (!System.getenv("SECURENATIVE_DISABLE").isEmpty()) {
                    config.setAgentDisable(Boolean.parseBoolean(System.getenv("SECURENATIVE_DISABLE")));
                } else {
                    config.setAgentDisable(false);
                }
            }

            if (fileConfig.getDebugMode() == null) {
                config.setDebugMode(fileConfig.getDebugMode());
            } else {
                if (!System.getenv("SECURENATIVE_DEBUG_MODE").isEmpty()) {
                    config.setDebugMode(Boolean.parseBoolean(System.getenv("SECURENATIVE_DEBUG_MODE")));
                } else {
                    config.setDebugMode(false);
                }
            }
        }

        return config;
    }

    public static SecureNativeOptions readConfigFile() {
        Logger.getLogger().debug(String.format("Reading %s", CONFIG_FILE));

        String configPath = System.getProperty("user.dir") + "/" + CONFIG_FILE;
        Path path = Paths.get(configPath);

        if (Files.exists(path)) {
            String content;
            try {
                content = new String(Files.readAllBytes(path));
            } catch (IOException e) {
                Logger.getLogger().debug(String.format("Unable to parse %s", CONFIG_FILE));
                return null;
            }

            SecureNativeOptions conf;
            ObjectMapper mapper = new ObjectMapper();
            try {
                conf = mapper.readValue(content, SecureNativeOptions.class);
            } catch (IOException e) {
                Logger.getLogger().debug(String.format("Unable to parse %s", CONFIG_FILE));
                return null;
            }
            return conf;
        }

        return null;
    }
}
