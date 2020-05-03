package com.securenative.models;

import com.securenative.context.SecureNativeContext;

import java.util.Date;
import java.util.Map;

public class EventOptions {
    private String event;
    private String userId;
    private UserTraits userTraits;
    private SecureNativeContext context;
    private Map<Object, Object> properties;
    private Date timestamp;

    public EventOptions(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserTraits getUserTraits() {
        return userTraits;
    }

    public void setUserTraits(UserTraits userTraits) {
        this.userTraits = userTraits;
    }

    public SecureNativeContext getContext() {
        return context;
    }

    public void setContext(SecureNativeContext context) {
        this.context = context;
    }

    public Map<Object, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<Object, Object> properties) {
        this.properties = properties;
    }

    public Date getTimestamp() { return timestamp; }

    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
