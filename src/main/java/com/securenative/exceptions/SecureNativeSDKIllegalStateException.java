package com.securenative.exceptions;

public class SecureNativeSDKIllegalStateException extends SecureNativeSDKException {
    public SecureNativeSDKIllegalStateException() {
        super("Secure Native SDK wasn't initialized yet, please call init first");
    }
}
