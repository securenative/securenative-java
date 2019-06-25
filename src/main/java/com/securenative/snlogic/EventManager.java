package com.securenative.snlogic;


import com.securenative.models.Event;
import com.securenative.models.RiskResult;
import com.securenative.models.SnEvent;

public interface EventManager {
    RiskResult sendSync(Event event, String requestUrl);
    void sendAsync(SnEvent event,String url);

}
