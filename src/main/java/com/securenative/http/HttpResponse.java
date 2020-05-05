package com.securenative.http;

public class HttpResponse {
    private final Boolean ok;
    private final int statusCode;
    private final String body;

    public HttpResponse(Boolean ok, int statusCode, String body){
        this.ok = ok;
        this.statusCode = statusCode;
        this.body = body;
    }

    public Boolean isOk() { return ok; }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
