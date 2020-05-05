package com.securenative.models;

public class RequestOptions {
    private final String url;
    private final String body;
    private Boolean retry;

    public RequestOptions(String url, String body, Boolean retry) {
        this.url = url;
        this.body = body;
        this.retry = retry;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public Boolean getRetry() {
        return retry;
    }

    public void setRetry(Boolean retry) {
        this.retry = retry;
    }
}
