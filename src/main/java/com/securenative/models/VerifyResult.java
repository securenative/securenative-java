package com.securenative.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securenative.enums.RiskLevel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyResult {
    private RiskLevel riskLevel;
    private float score;
    private String[] triggers;

    public VerifyResult() {
    }

    public VerifyResult(RiskLevel riskLevel, float score, String[] triggers) {
        this.riskLevel = riskLevel;
        this.score = score;
        this.triggers = triggers;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String[] getTriggers() {
        return triggers;
    }

    public void setTriggers(String[] triggers) {
        this.triggers = triggers;
    }
}