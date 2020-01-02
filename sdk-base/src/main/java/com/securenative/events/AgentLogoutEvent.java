package com.securenative.events;

import com.securenative.models.EventTypes;

import java.time.ZonedDateTime;

public class AgentLogoutEvent implements Event {
    public String eventType;
    public long ts;

    public AgentLogoutEvent() {
        this.eventType = EventTypes.AGENT_LOG_OUT.getType();
        this.ts = ZonedDateTime.now().toEpochSecond();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }
}
