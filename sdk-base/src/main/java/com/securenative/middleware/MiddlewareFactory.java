package com.securenative.middleware;

import com.securenative.snlogic.SecureNative;

public class MiddlewareFactory {
    // TODO implement me
    public static IMiddleware createMiddleware(SecureNative secureNative) {
        if (secureNative.moduleManager.getModule().equals("spring")) {
            return new SpringMiddleware();
        }
        return new SpringMiddleware();    // return default middleware
    }
}
