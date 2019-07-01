package com.securenative.models;

import java.util.AbstractMap;
import java.util.List;

public class EventOptions {
    private String ip;
    private String userAgent;
    private String remoteIP;
    private User user;
    private Device device;
    private String cookieName;
    private String cookieValue;
    private String eventType;
    private List <AbstractMap.SimpleEntry<String, String>> params;

    public EventOptions(String ip, String remoteIP, String userAgent, Device device, User user, String cookieName, String cookieValue, String eventType, List<AbstractMap.SimpleEntry<String, String>> params) {
        this.ip = ip;
        this.remoteIP = remoteIP;
        this.userAgent = userAgent;
        this.device = device;
        this.user = user;
        this.cookieName = cookieName;
        this.eventType = eventType;
        this.params = params;
        this.cookieValue = cookieValue;
    }

    public EventOptions(String ip, String userAgent,String eventType) {
        this.ip = ip;
        this.userAgent = userAgent;
        this.eventType = eventType;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRemoteIP() {
        return remoteIP;
    }

    public void setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public List<AbstractMap.SimpleEntry<String, String>> getParams() {
        return params;
    }

    public void setParams(List<AbstractMap.SimpleEntry<String, String>> params) {
        this.params = params;
    }

    public String getCookieValue() {
        return cookieValue;
    }

    public void setCookieValue(String cookieValue) {
        this.cookieValue = cookieValue;
    }





}
