package com.securenative.models;

public interface Event {
    String getEventType();
    String getCid();
    String getVid();
    String getFp();
    String getIp();
    String getRemoteIP();
    String getUserAgent();
    User getUser();
    Device getDevice();
    String getCookieName();
    String getCookieValue();
}
