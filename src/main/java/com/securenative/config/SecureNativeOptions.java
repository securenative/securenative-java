package com.securenative.config;

import com.securenative.enums.FailoverStrategy;

import java.util.ArrayList;

public class SecureNativeOptions {
    /**
     * Api Secret associated with SecureNative account
     */
    private final String apiKey;

    /**
     * SecureNative backend API URL
     */
    private final String apiUrl;

    /**
     * SecureNative event persistence interval
     */
    private final int interval;

    /**
     * Maximum queue capacity
     */
    private final int maxEvents;

    /**
     * Event sending timeout
     */
    private final int timeout;

    /**
     * Allow automatically track event
     */
    private final Boolean autoSend;

    /**
     * Disable SDk, all operation will not take effect
     */
    private final Boolean disable;

    /**
     * Default log level
     */
    private final String logLevel;

    /**
     * Failover strategy
     */
    private final FailoverStrategy failoverStrategy;

    /**
     * Proxy Headers
     */
    private final ArrayList<String> proxyHeaders;

    public SecureNativeOptions(String apiKey, String apiUrl, int interval, int maxEvents, int timeout, boolean autoSend, boolean disable, String logLevel, FailoverStrategy failoverStrategy, ArrayList<String> proxyHeaders) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.interval = interval;
        this.maxEvents = maxEvents;
        this.timeout = timeout;
        this.autoSend = autoSend;
        this.disable = disable;
        this.logLevel = logLevel;
        this.failoverStrategy = failoverStrategy;
        this.proxyHeaders = proxyHeaders;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public int getInterval() {
        return interval;
    }

    public int getMaxEvents() {
        return maxEvents;
    }

    public int getTimeout() {
        return timeout;
    }

    public Boolean getAutoSend() {
        return autoSend;
    }

    public Boolean getDisabled() {
        return disable;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public FailoverStrategy getFailoverStrategy() {
        return failoverStrategy;
    }

    public ArrayList<String> getProxyHeaders() {
        return proxyHeaders;
    }
}
