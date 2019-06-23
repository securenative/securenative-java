package snlogic;

import com.google.common.base.Strings;
import exceptions.SecureNativeSDKException;
import models.RiskResult;
import models.EventOptions;
import models.SecureNativeOptions;
import models.SnEvent;

import javax.servlet.http.HttpServletRequest;

public class SecureNative implements ISDK {
    private final int MAX_CUSTOM_PARAMS = 6;
    private final String API_URL = "https://api.securenative.com/collector/api/v1";
    private final int INTERVAL = 1000;
    private final int MAX_EVENTS = 1000;
    private final Boolean AUTO_SEND = true;
    private final long DEFAULT_TIMEOUT = 1500;

    private EventManager eventManager;
    private SecureNativeOptions snOptions;
    private String apiKey;

    public SecureNative(String apiKey, SecureNativeOptions options) throws SecureNativeSDKException {
        if (Strings.isNullOrEmpty(apiKey)) {
            throw new SecureNativeSDKException("You must pass your snlogic.SecureNative api key");
        }
        this.apiKey = apiKey;
        this.snOptions = initializeOptions(options);
        this.eventManager = new SnEventManager(apiKey,this.snOptions);
    }

    private SecureNativeOptions initializeOptions(SecureNativeOptions options) {
        if (options == null) {
            options = new SecureNativeOptions();
        }
        if (Strings.isNullOrEmpty(options.getApiUrl())) {
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
        if(options.getTimeout() == 0){
            options.setTimeout(DEFAULT_TIMEOUT);
        }

        return options;
    }

    @Override
    public void track(EventOptions options, HttpServletRequest request) {
        if (options != null && options.getParams() != null && options.getParams().size() > MAX_CUSTOM_PARAMS) {
            options.setParams(options.getParams().subList(0,5));
        }
        SnEvent event = this.eventManager.buildEvent(request, options);
        this.eventManager.sendSync(event, this.snOptions.getApiUrl() + "/track");
    }

    @Override
    public RiskResult verify(EventOptions options, HttpServletRequest request) {
        SnEvent event = this.eventManager.buildEvent(request, options);
        return this.eventManager.sendSync(event, this.snOptions.getApiUrl() + "/verify");
    }

    @Override
    public RiskResult flow(long flowId, EventOptions options, HttpServletRequest request) {
        SnEvent event = this.eventManager.buildEvent(request, options);
        return this.eventManager.sendSync(event, this.snOptions.getApiUrl() + "/flow/" + flowId);
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }
}