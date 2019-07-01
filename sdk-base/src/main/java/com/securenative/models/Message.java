package com.securenative.models;

public class Message {
    private Event snEvent;
    private String url;

    public Message(Event snEvent, String url) {
        this.snEvent = snEvent;
        this.url = url;
    }

    public Event getSnEvent() {
        return snEvent;
    }

    public void setSnEvent(Event snEvent) {
        this.snEvent = snEvent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
