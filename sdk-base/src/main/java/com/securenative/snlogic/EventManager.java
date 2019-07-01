package com.securenative.snlogic;


import com.securenative.models.Event;
import com.securenative.models.RiskResult;

public interface EventManager {
    RiskResult sendSync(Event event, String requestUrl);
    void sendAsync(Event event, String url);

}
