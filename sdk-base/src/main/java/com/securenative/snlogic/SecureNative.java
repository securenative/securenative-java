package com.securenative.snlogic;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.Event;
import com.securenative.models.RiskResult;
import com.securenative.models.SecureNativeOptions;

import java.io.File;
import java.io.IOException;

public class SecureNative implements ISDK {
    private final String API_URL = "https://api.securenative.com/collector/api/v1";
    private final String SECURE_NATIVE_CONFIG_FILE_PATH = "SECURE_NATIVE_CONFIG_FILE_PATH";

    private final String API_KEY_ENV_VAR = "SECURENATIVE_API_KEY";
    private final int INTERVAL = 1000;
    private final int MAX_EVENTS = 1000;
    private final Boolean AUTO_SEND = true;
    private final Boolean SDK_ENABLED = true;
    private final Boolean DEBUG_LOG = false;
    private final int DEFAULT_TIMEOUT = 1500;

    private EventManager eventManager;
    private SecureNativeOptions snOptions;

    private static ISDK secureNative = null;
    private Utils utils;


    private SecureNative(SecureNativeOptions options) throws SecureNativeSDKException {
        utils = new Utils();
        this.snOptions = initializeOptions(options);
        this.eventManager = new SnEventManager(this.snOptions);
        Logger.setLoggingEnable(this.snOptions.getDebugMode());
    }

    public static ISDK init(SecureNativeOptions options) throws SecureNativeSDKException {
        if (secureNative == null) {
            secureNative = new SecureNative(options);
            if (options != null && options.getDebugMode() != null) {
                Logger.setLoggingEnable(options.getDebugMode());
            }
            return secureNative;
        }
        throw new SecureNativeSDKException("This SDK was already initialized");
    }

    public static ISDK getInstance() throws SecureNativeSDKException {
        if (secureNative == null) {
            throw new SecureNativeSDKException("Secure Native SDK wasnt initialized yet, please call init first");
        }
        return secureNative;
    }

    private SecureNativeOptions initializeOptions(SecureNativeOptions options) throws SecureNativeSDKException {
        if (options == null) {
            Logger.getLogger().info("SecureNative options are empty, initializing default values");
            SecureNativeOptions optFromEnv = loadFromEnv();
            options = optFromEnv == null ? new SecureNativeOptions() : optFromEnv;
        }

        if (options.getApiKey() == null) {
            options.setApiKey(getApiKeyFromEnv());
            if (options.getApiKey() == null) {
                throw new SecureNativeSDKException("couldnt find api key");
            }
        }

        if (this.utils.isNullOrEmpty(options.getApiUrl())) {
            options.setApiUrl(API_URL);
        }

        if (options.getInterval() == 0) {
            options.setInterval(INTERVAL);
        }

        if (options.getMaxEvents() == 0) {
            options.setMaxEvents(MAX_EVENTS);
        }
        if (options.isAutoSend() == null) {
            options.setAutoSend(AUTO_SEND);
        }
        if (options.getSdkEnabled() == null) {
            options.setSdkEnabled(SDK_ENABLED);
        }
        if (options.getDebugMode() == null) {
            options.setSdkEnabled(DEBUG_LOG);
        }
        if (options.getTimeout() == 0) {
            options.setTimeout(DEFAULT_TIMEOUT);
        }
        if (options.getDebugMode() == null) {
            options.setDebugMode(false);
        }

        return options;
    }

    private SecureNativeOptions loadFromEnv() {
        try {
            String snConfigPath = System.getenv(SECURE_NATIVE_CONFIG_FILE_PATH);
            if (!this.utils.isNullOrEmpty(snConfigPath)) {
                ObjectMapper objectMapper = new ObjectMapper();
                File file = new File(snConfigPath);
                return objectMapper.readValue(file, SecureNativeOptions.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getApiKeyFromEnv() {
        try {
            return System.getenv(API_KEY_ENV_VAR);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getDefaultCookieName() {
        return this.utils.COOKIE_NAME;
    }

    @Override
    public void track(Event event) {
        Logger.getLogger().info("Track event call");
        this.eventManager.sendAsync(event, this.snOptions.getApiUrl() + "/track");
    }

    @Override
    public RiskResult verify(Event event) {
        Logger.getLogger().info("Verify event call");
        return this.eventManager.sendSync(event, this.snOptions.getApiUrl() + "/verify");
    }

    @Override
    public RiskResult flow(long flowId, Event event) {//FOR FUTURE PURPOSES
        Logger.getLogger().info("Flow event call");
        return this.eventManager.sendSync(event, this.snOptions.getApiUrl() + "/flow/" + flowId);
    }

    @Override
    public String getApiKey() {
        return this.snOptions.getApiKey();
    }


}