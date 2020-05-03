package com.securenative.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RiskLevel {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    private String riskLevel;

    @JsonValue
    public String getRiskLevel() {
        return riskLevel;
    }

    @JsonCreator
    public static RiskLevel fromString(String key) {
        return key == null ? null : RiskLevel.valueOf(key.toUpperCase());
    }

    RiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

}