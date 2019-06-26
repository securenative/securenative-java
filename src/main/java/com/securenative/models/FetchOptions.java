package com.securenative.models;

import com.sun.net.httpserver.Headers;

import java.util.List;

public class FetchOptions {
    private String apiKey;
    private String url;
    private String method;
    private List<Headers> headers;

    public FetchOptions(String apiKey, String url, String method, List<Headers> headers) {
        this.apiKey = apiKey;
        this.url = url;
        this.method = method;
        this.headers = headers;

    }


    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Headers> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Headers> headers) {
        this.headers = headers;
    }
}
