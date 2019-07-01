package com.securenative.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskResult {
    private String riskLevel;
    private double score;
    private String[] triggers;

    public RiskResult() {
    }

    public RiskResult(String riskLevel, double score, String[] triggers) {
        this.riskLevel = riskLevel;
        this.score = score;
        this.triggers = triggers;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String[] getTriggers() {
        return triggers;
    }

    public void setTriggers(String[] triggers) {
        this.triggers = triggers;
    }
}