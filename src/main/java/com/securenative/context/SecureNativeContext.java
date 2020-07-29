package com.securenative.context;

import java.util.Map;

public class SecureNativeContext {
    private String clientToken;
    private String ip;
    private String remoteIp;
    private Map<String, String> headers;
    private String url;
    private String method;
    private String body;

    public SecureNativeContext() {
    }

    public SecureNativeContext(String clientToken, String ip, String remoteIp, Map<String, String> headers, String url, String method, String body) {
        this.clientToken = clientToken;
        this.ip = ip;
        this.remoteIp = remoteIp;
        this.headers = headers;
        this.url = url;
        this.method = method;
        this.body = body;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
