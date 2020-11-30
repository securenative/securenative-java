package com.securenative.config;

import com.securenative.enums.FailoverStrategy;

import java.util.ArrayList;

public class SecureNativeConfigurationBuilder {
    /**
     * Api Secret associated with SecureNative account
     */
    private String apiKey;

    /**
     * SecureNative backend API URL
     */
    private String apiUrl;

    /**
     * SecureNative event persistence interval
     */
    private int interval;

    /**
     * Maximum queue capacity
     */
    private int maxEvents;

    /**
     * Event sending timeout
     */
    private int timeout;

    /**
     * Allow automatically track event
     */
    private Boolean autoSend;

    /**
     * Disable SDk, all operation will not take effect
     */
    private Boolean disable;

    /**
     * Default log level
     */
    private String logLevel;

    /**
     * Failover strategy
     */
    private FailoverStrategy failoverStrategy;

    /**
     * Proxy Headers
     */
    private ArrayList<String> proxyHeaders;

    /**
     * Pii Headers
     */
    private ArrayList<String> piiHeaders;

    /**
     * Pii Regex Pattern
     */
    private String piiRegexPattern;

    private SecureNativeConfigurationBuilder() {
    }

    public static SecureNativeConfigurationBuilder defaultConfigBuilder() {
        return new SecureNativeConfigurationBuilder()
                .withApiKey(null)
                .withApiUrl("https://api.securenative.com/collector/api/v1")
                .withInterval(1000)
                .withTimeout(1500)
                .withMaxEvents(1000)
                .withAutoSend(true)
                .withDisable(false)
                .withLogLevel("fatal")
                .withFailoverStrategy(FailoverStrategy.FAIL_OPEN)
                .withProxyHeaders(new ArrayList<>())
                .withPiiHeaders(new ArrayList<>())
                .withPiiRegexPattern(null);
    }

    public SecureNativeConfigurationBuilder withApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public SecureNativeConfigurationBuilder withApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }

    public SecureNativeConfigurationBuilder withInterval(int interval) {
        this.interval = interval;
        return this;
    }

    public SecureNativeConfigurationBuilder withMaxEvents(int maxEvents) {
        this.maxEvents = maxEvents;
        return this;
    }

    public SecureNativeConfigurationBuilder withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public SecureNativeConfigurationBuilder withAutoSend(Boolean autoSend) {
        this.autoSend = autoSend;
        return this;
    }

    public SecureNativeConfigurationBuilder withDisable(Boolean disable) {
        this.disable = disable;
        return this;
    }

    public SecureNativeConfigurationBuilder withLogLevel(String logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public SecureNativeConfigurationBuilder withFailoverStrategy(FailoverStrategy failoverStrategy) {
        this.failoverStrategy = failoverStrategy;
        return this;
    }

    public SecureNativeConfigurationBuilder withProxyHeaders(ArrayList<String> proxyHeaders) {
        this.proxyHeaders = proxyHeaders;
        return this;
    }

    public SecureNativeConfigurationBuilder withPiiHeaders(ArrayList<String> piiHeaders) {
        this.piiHeaders = piiHeaders;
        return this;
    }

    public SecureNativeConfigurationBuilder withPiiRegexPattern(String piiRegexPattern) {
        this.piiRegexPattern = piiRegexPattern;
        return this;
    }

    public SecureNativeOptions build() {
        return new SecureNativeOptions(apiKey, apiUrl, interval, maxEvents, timeout, autoSend, disable, logLevel, failoverStrategy, proxyHeaders, piiHeaders, piiRegexPattern);
    }
}
