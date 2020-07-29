package com.securenative.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FailoverStrategy {
    FAIL_OPEN("fail-open"),
    FAIL_CLOSED("fail-closed");

    private final String failoverStrategy;

    @JsonValue
    public String getFailoverStrategy() {
        return failoverStrategy;
    }

    public static FailoverStrategy fromString(String key, FailoverStrategy failoverStrategy) {
        try {
            return FailoverStrategy.valueOf(key.replace("-", "_").toUpperCase());
        } catch (IllegalArgumentException ex) {
            return failoverStrategy;
        }
    }

    FailoverStrategy(String failoverStrategy) {
        this.failoverStrategy = failoverStrategy;
    }
}