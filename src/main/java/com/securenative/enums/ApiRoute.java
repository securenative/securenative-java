package com.securenative.enums;

public enum ApiRoute {
    TRACK("track"),
    VERIFY("verify");

    private final String apiRoute;

    public String getApiRoute() {
        return apiRoute;
    }

    ApiRoute(String apiRoute) {
        this.apiRoute = apiRoute;
    }
}