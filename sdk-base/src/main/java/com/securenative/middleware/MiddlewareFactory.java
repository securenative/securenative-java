package com.securenative.middleware;

import com.securenative.snlogic.SecureNative;

public class MiddlewareFactory {
    public static IMiddleware createMiddleware(SecureNative secureNative) {
//        if (secureNative.moduleManager.getModule().equals("spring")) {
//            return new SpringMiddleware(secureNative);
//        }
        return new SpringMiddleware(secureNative);    // return default middleware
    }
}
