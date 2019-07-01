package com.securenative.models;

import java.util.AbstractMap;
import java.util.List;

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
    List<AbstractMap.SimpleEntry<String, String>> getParams();
}
