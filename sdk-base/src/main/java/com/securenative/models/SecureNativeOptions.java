package com.securenative.models;

public class SecureNativeOptions {
    private String apiUrl;
    private int interval;
    private long maxEvents;
    private long timeout;
    private Boolean autoSend;
    private Boolean isSdkEnabled;
    private Boolean debugMode;
    private String appName;

    public SecureNativeOptions(){
        this.autoSend = true;
        this.isSdkEnabled = true;
        this.debugMode = false;
    }

    public SecureNativeOptions(String appName, String apiUrl, int interval, long maxEvents, int timeout, boolean autoSend, boolean isSdkEnabled, boolean debugMode) {
        this.interval = interval;
        this.maxEvents = maxEvents;
        this.apiUrl = apiUrl;
        this.timeout = timeout;
        this.autoSend = autoSend;
        this.isSdkEnabled = isSdkEnabled;
        this.debugMode = debugMode;
        this.appName = appName;
    }

    public SecureNativeOptions(String apiUrl, int interval, long maxEvents, int timeout) {
        this.interval = interval;
        this.maxEvents = maxEvents;
        this.apiUrl = apiUrl;
        this.timeout = timeout;
        this.autoSend = true;
        this.isSdkEnabled = true;
        this.debugMode = false;

    }

    public String getAppName() {
        return appName;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public long getMaxEvents() {
        return maxEvents;
    }

    public void setMaxEvents(long maxEvents) {
        this.maxEvents = maxEvents;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Boolean isAutoSend() {
        return autoSend;
    }

    public void setAutoSend(Boolean autoSend) {
        this.autoSend = autoSend;
    }

    public Boolean getSdkEnabled() {
        return isSdkEnabled;
    }

    public void setSdkEnabled(Boolean sdkEnabled) {
        isSdkEnabled = sdkEnabled;
    }

    public Boolean getDebugMode() {
        return debugMode;
    }

    public void setDebugMode(Boolean debugMode) {
        this.debugMode = debugMode;
    }
}
