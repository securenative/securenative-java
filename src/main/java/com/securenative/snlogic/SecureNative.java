package com.securenative.snlogic;

import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.*;

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
    private Utils utils;

    public SecureNative(String apiKey, SecureNativeOptions options) throws SecureNativeSDKException {
        this.utils = new Utils();
        if (this.utils.isNullOrEmpty(apiKey)) {
            throw new SecureNativeSDKException("You must pass your com.securenative.snlogic.SecureNative api key");
        }
        this.apiKey = apiKey;
        this.snOptions = initializeOptions(options);
        this.eventManager = new SnEventManager(apiKey,this.snOptions);

    }

    private SecureNativeOptions initializeOptions(SecureNativeOptions options) {
        if (options == null) {
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
        if(options.getTimeout() == 0){
            options.setTimeout(DEFAULT_TIMEOUT);
        }

        return options;
    }

    @Override
    public String getDefaultCookieName(){
        return this.utils.COOKIE_NAME;
    }

    @Override
    public void track(Event event) {
        this.eventManager.sendAsync(event, this.snOptions.getApiUrl() + "/track");
    }

    @Override
    public RiskResult verify(Event event) {
        return this.eventManager.sendSync(event, this.snOptions.getApiUrl() + "/verify");
    }

    @Override
    public RiskResult flow(long flowId, Event event) {//FOR FUTURE PURPOSES
        return this.eventManager.sendSync(event, this.snOptions.getApiUrl() + "/flow/" + flowId);
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    public Event buildEventFromHttpServletRequest(HttpServletRequest request, Event event) {
        String encodedCookie = this.utils.getCookie(request, event != null && !this.utils.isNullOrEmpty(event.getCookieName()) ? event.getCookieName() : this.utils.COOKIE_NAME);
        String eventype =  event == null || this.utils.isNullOrEmpty(event.getEventType()) ? EventTypes.LOG_IN.getType() : event.getEventType();
        String ip = event != null && event.getIp() != null ? event.getIp() : this.utils.remoteIpFromServletRequest(request);
        String remoteIP = request.getRemoteAddr();
        String userAgent = event != null && event.getUserAgent() != null ? event.getUserAgent() : request.getHeader(this.utils.USERAGENT_HEADER);
        User user = event != null && event.getUser() != null ? event.getUser() : new User(null, null, "anonymous");
        Device device = event != null && event.getDevice() != null ? event.getDevice() : null;
        return new SnEvent.EventBuilder(eventype).withCookieValue(encodedCookie).withIp(ip).withRemoteIP(remoteIP).withUserAgent(userAgent).withUser(user).withDevice(device).build();
    }
}