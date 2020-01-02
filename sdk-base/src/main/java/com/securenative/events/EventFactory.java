package com.securenative.events;

import com.securenative.models.Event;
import com.securenative.models.EventTypes;

public class EventFactory {
    public static Event createEvent(EventTypes eventType, String... args) {
        if (eventType == EventTypes.AGENT_LOG_IN) {
            String framework = args[0];
            String frameworkVersion = args[1];
            String appName = args[2];
            return new AgentLoginEvent(framework, frameworkVersion, appName);
        } else if (eventType == EventTypes.AGENT_LOG_OUT) {
            return new AgentLogoutEvent();
        }
        return null;
    }
}
