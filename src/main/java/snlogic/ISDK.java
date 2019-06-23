package snlogic;

import models.RiskResult;
import models.EventOptions;

import javax.servlet.http.HttpServletRequest;


public interface ISDK {
    void track(EventOptions options, HttpServletRequest request) throws Exception;
    RiskResult verify(EventOptions options, HttpServletRequest request);
    RiskResult flow(long flowId, EventOptions options, HttpServletRequest request);
    String getApiKey();

}
