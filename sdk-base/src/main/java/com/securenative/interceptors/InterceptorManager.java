package com.securenative.interceptors;

import com.securenative.middleware.IMiddleware;
import com.securenative.snlogic.ModuleManager;

public class InterceptorManager {
    public static void applyInterceptors(ModuleManager moduleManager, IMiddleware middleware) {
//        if (moduleManager.getModule().equals("spring")) {
//            new SpringInterceptor(verifyRequest, verifyWebhook);
//        }
        new SpringInterceptor(middleware);  // Default interceptor
    }
}
