package com.securenative.models;

import com.securenative.utils.DateUtils;

public class SampleEvent implements Event {
    private final String eventType = "custom-event";
    private final String timestamp = DateUtils.generateTimestamp();

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }
}
