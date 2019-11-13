package com.securenative.snlogic;


import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.Event;
import com.securenative.models.RiskResult;
import com.securenative.models.SecureNativeOptions;
import org.apache.logging.log4j.util.Strings;

public class SecureNative implements ISDK {
    private final String API_URL = "https://api.securenative.com/collector/api/v1";
    private final int INTERVAL = 1000;
    private final int MAX_EVENTS = 1000;
    private final Boolean AUTO_SEND = true;
    private final Boolean SDK_ENABLED = true;
    private final Boolean DEBUG_LOG = false;
    private final int DEFAULT_TIMEOUT = 1500;

    private EventManager eventManager;
    private SecureNativeOptions snOptions;
    private String apiKey;
    private Utils utils;

    private static ISDK secureNative = null;


    private SecureNative(String apiKey, SecureNativeOptions options) throws SecureNativeSDKException {
        String apiKeyEnvVar = Strings.EMPTY;
        try{
            apiKeyEnvVar = System.getenv("SN_API_KEY");
        }
        catch (Exception e){
            //logger is not initialized yet
        }

        this.utils = new Utils();
        if (this.utils.isNullOrEmpty(apiKey) && this.utils.isNullOrEmpty(apiKeyEnvVar)) {
            throw new SecureNativeSDKException("You must pass your SecureNative api key");
        }
        this.apiKey = this.utils.isNullOrEmpty(apiKey) ? apiKeyEnvVar : apiKey;
        this.snOptions = initializeOptions(options);
        this.eventManager = new SnEventManager(apiKey,this.snOptions);
        Logger.setLoggingEnable(this.snOptions.getDebugMode());
    }


    public static ISDK init(String apiKey, SecureNativeOptions options) throws SecureNativeSDKException {
        if (secureNative == null) {
            secureNative = new SecureNative(apiKey, options);
            if(options != null && options.getDebugMode() != null){
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

    private SecureNativeOptions initializeOptions(SecureNativeOptions options) {
        if (options == null) {
            Logger.getLogger().info("SecureNative options are empty, initializing default values");
            options = new SecureNativeOptions();
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
        if (options.getSdkEnabled() == null){
            options.setSdkEnabled(SDK_ENABLED);
        }
        if (options.getDebugMode() == null){
            options.setSdkEnabled(DEBUG_LOG);
        }
        if(options.getTimeout() == 0){
            options.setTimeout(DEFAULT_TIMEOUT);
        }
        if(options.getDebugMode() == null){
            options.setDebugMode(false);
        }

        return options;
    }

    @Override
    public String getDefaultCookieName(){
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
        return apiKey;
    }

    }