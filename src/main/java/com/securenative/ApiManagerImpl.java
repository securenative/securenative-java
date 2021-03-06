package com.securenative;

import com.securenative.config.SecureNativeOptions;
import com.securenative.enums.ApiRoute;
import com.securenative.enums.FailoverStrategy;
import com.securenative.enums.RiskLevel;
import com.securenative.exceptions.SecureNativeInvalidOptionsException;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.Event;
import com.securenative.models.EventOptions;
import com.securenative.models.SDKEvent;
import com.securenative.models.VerifyResult;

public class ApiManagerImpl implements ApiManager {
    private final EventManager eventManager;
    private final SecureNativeOptions options;
    public static final Logger logger = Logger.getLogger(SecureNative.class);

    public ApiManagerImpl(EventManager eventManager, SecureNativeOptions options) throws SecureNativeSDKException {
        this.eventManager = eventManager;
        this.options = options;
    }

    @Override
    public void track(EventOptions eventOptions) throws SecureNativeInvalidOptionsException {
        logger.info("Track event call");
        Event event = new SDKEvent(eventOptions, this.options);
        this.eventManager.sendAsync(event, ApiRoute.TRACK.getApiRoute(), true);
    }

    @Override
    public VerifyResult verify(EventOptions eventOptions) throws SecureNativeInvalidOptionsException {
        logger.info("Verify event call");
        Event event = new SDKEvent(eventOptions, this.options);
        try {
            return this.eventManager.sendSync(VerifyResult.class, event, ApiRoute.VERIFY.getApiRoute());
        } catch (Exception ex) {
            logger.error("Failed to call verify", ex);
            return this.options.getFailoverStrategy() == FailoverStrategy.FAIL_OPEN ?
                    new VerifyResult(RiskLevel.LOW, 0, new String[0])
                    : new VerifyResult(RiskLevel.HIGH, 1, new String[0]);
        }
    }
}
