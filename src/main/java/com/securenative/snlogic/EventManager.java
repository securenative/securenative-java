package com.securenative.snlogic;


import com.securenative.models.Event;
import com.securenative.models.RiskResult;
import com.securenative.models.SnEvent;

import javax.servlet.http.HttpServletRequest;

public interface EventManager {
    Event buildEventFromHttpServletRequest(final HttpServletRequest request, final Event event);
    RiskResult sendSync(Event event, String requestUrl);
    void sendAsync(SnEvent event,String url);

}
