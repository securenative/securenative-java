package com.securenative.snlogic;


import com.securenative.events.Event;
import com.securenative.models.RiskResult;

public interface EventManager {
    RiskResult sendSync(Event event, String requestUrl);

    String sendAgentEvent(Event event, String requestUrl);

    void sendAsync(Event event, String url);

    void setSessionId(String sessionId);

    void stopEventsPersist();

    void startEventsPersist();
}
