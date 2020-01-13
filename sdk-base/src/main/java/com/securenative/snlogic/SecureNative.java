package com.securenative.snlogic;


import com.securenative.events.Event;
import com.securenative.events.EventFactory;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.interceptors.InterceptorManager;
import com.securenative.models.EventTypes;
import com.securenative.models.RiskResult;
import com.securenative.models.SecureNativeOptions;

public class SecureNative implements ISDK {
    private final String API_URL = "https://api.securenative.com/collector/api/v1";
    private final int INTERVAL = 1000;
    private final int MAX_EVENTS = 1000;
    private final Boolean AUTO_SEND = true;
    private final Boolean SDK_ENABLED = true;
    private final Boolean DEBUG_LOG = false;
    private final int DEFAULT_TIMEOUT = 1500;

    private Boolean isAgentStarted = false;
    private EventManager eventManager;
    private SecureNativeOptions snOptions;
    private String apiKey;
    private Utils utils;

    private static ISDK secureNative = null;

    public ModuleManager moduleManager;

    public SecureNative(ModuleManager moduleManager, SecureNativeOptions snOptions) throws SecureNativeSDKException {
        this.apiKey = snOptions.getApiKey();
        this.utils = new Utils();
        this.snOptions = initializeOptions(snOptions);
        Logger.setLoggingEnable(this.snOptions.getDebugMode());
        this.eventManager = new SnEventManager(this.apiKey, this.snOptions);
        this.moduleManager = moduleManager;
        this.snOptions = snOptions;
    }

    private SecureNative(String apiKey, SecureNativeOptions options) throws SecureNativeSDKException {
        this.utils = new Utils();
        if (Utils.isNullOrEmpty(apiKey)) {
            throw new SecureNativeSDKException("You must pass your SecureNative api key");
        }
        this.apiKey = apiKey;
        this.snOptions = initializeOptions(options);
        this.eventManager = new SnEventManager(apiKey, this.snOptions);
        Logger.setLoggingEnable(this.snOptions.getDebugMode());
    }

    public static ISDK init(String apiKey, SecureNativeOptions options) throws SecureNativeSDKException {
        if (secureNative == null) {
            secureNative = new SecureNative(apiKey, options);
            if (options != null && options.getDebugMode() != null) {
                Logger.setLoggingEnable(options.getDebugMode());
            }
            return secureNative;
        }
        throw new SecureNativeSDKException("This SDK was already initialized");
    }

    public static ISDK getInstance() throws SecureNativeSDKException {
        if (secureNative == null) {
            throw new SecureNativeSDKException("Secure Native SDK wasn't initialized yet, please call init first");
        }
        return secureNative;
    }

    private SecureNativeOptions initializeOptions(SecureNativeOptions options) {
        if (options == null) {
            Logger.getLogger().info("SecureNative options are empty, initializing default values");
            options = new SecureNativeOptions();
        }
        if (Utils.isNullOrEmpty(options.getApiUrl())) {
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

    @Override
    public String getDefaultCookieName() {
        return this.utils.COOKIE_NAME;
    }

    @Override
    public String agentLogin() {
        Logger.getLogger().debug("Performing agent login");
        String requestUrl = this.snOptions.getApiUrl() + "/agent-login";

        String framework = this.moduleManager.getFramework();
        String frameworkVersion = this.moduleManager.getFrameworkVersion();

        Event event = EventFactory.createEvent(EventTypes.AGENT_LOG_IN, framework, frameworkVersion, this.snOptions.getAppName());
        try {
            String sessionId = this.eventManager.sendAgentEvent(event, requestUrl);

            if (sessionId.toLowerCase().equals("invalid api key id")) {
                Logger.getLogger().debug("Failed to perform agent login: Invalid api key id");
                return null;
            }

            Logger.getLogger().debug(String.format("Agent successfully logged-in, sessionId: %s", sessionId));
            return sessionId;
        } catch (Exception e) {
            Logger.getLogger().debug(String.format("Failed to perform agent login: %s", e.toString()));
        }
        return null;
    }

    @Override
    public Boolean agentLogout() {
        Logger.getLogger().debug("Performing agent logout");
        String requestUrl = this.snOptions.getApiUrl() + "/agent-logout";

        Event event = EventFactory.createEvent(EventTypes.AGENT_LOG_OUT);
        try {
            this.eventManager.sendAgentEvent(event, requestUrl);
            Logger.getLogger().debug("Agent successfully logged-out");
            return true;
        } catch (Exception e) {
            Logger.getLogger().debug(String.join("Failed to perform agent logout; ", e.toString()));
        }
        return false;
    }

    @Override
    public Boolean startAgent() throws SecureNativeSDKException {
        if (!this.isAgentStarted) {
            Logger.getLogger().debug("Attempting to start agent");
            if (this.snOptions.getApiKey() == null) {
                throw new SecureNativeSDKException("You must pass your SecureNative api key");
            }

            if (this.snOptions.isAgentDisable()) {
                Logger.getLogger().debug("Skipping agent start");
                return false;
            }

            // apply interceptors
            InterceptorManager.applyModuleInterceptors(this.moduleManager, this);

            // obtain session
            String sessionId = this.agentLogin();
            if (sessionId != null) {
                InterceptorManager.applyAgentInterceptor(sessionId);
                // TODO add event persist
                this.eventManager.startEventsPersist();
                this.isAgentStarted = true;

                Logger.getLogger().debug("Agent successfully started!");
                return true;
            } else {
                Logger.getLogger().debug("No session obtained, unable to start agent!");
                return false;
            }
        } else {
            Logger.getLogger().debug("Agent already started, skipping");
            return false;
        }
    }

    @Override
    public void stopAgent() {
        if (this.isAgentStarted) {
            Logger.getLogger().debug("Attempting to stop agent");
            Boolean status = this.agentLogout();
            if (status) {
                this.eventManager.stopEventsPersist();
                this.isAgentStarted = false;
            }
        }
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
    public RiskResult flow(long flowId, Event event) { // FOR FUTURE PURPOSES
        Logger.getLogger().info("Flow event call");
        return this.eventManager.sendSync(event, this.snOptions.getApiUrl() + "/flow/" + flowId);
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }
}