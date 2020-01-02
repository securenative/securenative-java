package com.securenative.snlogic;


import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.events.Event;
import com.securenative.models.RiskResult;

public interface ISDK {
    void track(Event event) throws Exception;
    RiskResult verify(Event event);
    RiskResult flow(long flowId, Event event);
    String getApiKey();
    String getDefaultCookieName();
    String  agentLogin();
    Boolean agentLogout();
    Boolean startAgent() throws SecureNativeSDKException;
    void stopAgent();
}
