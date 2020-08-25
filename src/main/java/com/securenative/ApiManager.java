package com.securenative;

import com.securenative.exceptions.SecureNativeInvalidOptionsException;
import com.securenative.models.EventOptions;
import com.securenative.models.VerifyResult;

public interface ApiManager {
    void track(EventOptions eventOptions) throws SecureNativeInvalidOptionsException;

    VerifyResult verify(EventOptions eventOptions) throws SecureNativeInvalidOptionsException;
}
