package com.securenative.models;

public class SecureNativeOptions {
    private String apiKey;
    private String appName;
    private String apiUrl;
    private int interval;
    private long maxEvents;
    private long timeout;
    private Boolean autoSend;
    private Boolean isSdkEnabled;
    private Boolean debugMode;
    private String minSupportedVersion;

    public SecureNativeOptions(){
        this.autoSend = true;
        this.isSdkEnabled = true;
        this.debugMode = false;
        this.minSupportedVersion = "1.8.0";
    }

    public SecureNativeOptions(String apiKey){
        this.apiKey = apiKey;
        this.autoSend = true;
        this.isSdkEnabled = true;
        this.debugMode = false;
        this.minSupportedVersion = "1.8.0";
    }

    public SecureNativeOptions(String apiKey, String appName, String apiUrl, int interval, long maxEvents, int timeout, boolean autoSend, boolean isSdkEnabled, boolean debugMode, String minSupportedVersion) {
        this.apiKey = apiKey;
        this.appName = appName;
        this.interval = interval;
        this.maxEvents = maxEvents;
        this.apiUrl = apiUrl;
        this.timeout = timeout;
        this.autoSend = autoSend;
        this.isSdkEnabled = isSdkEnabled;
        this.debugMode = debugMode;
        this.minSupportedVersion = minSupportedVersion;
    }

    public SecureNativeOptions(String apiKey,String apiUrl, int interval, long maxEvents, int timeout) {
        this.apiKey = apiKey;
        this.interval = interval;
        this.maxEvents = maxEvents;
        this.apiUrl = apiUrl;
        this.timeout = timeout;
        this.autoSend = true;
        this.isSdkEnabled = true;
        this.debugMode = false;
        this.minSupportedVersion = "1.8.0";
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

    public String getMinSupportedVersion() {
        return minSupportedVersion;
    }

    public void setMinSupportedVersion(String minSupportedVersion) {
        this.minSupportedVersion = minSupportedVersion;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
