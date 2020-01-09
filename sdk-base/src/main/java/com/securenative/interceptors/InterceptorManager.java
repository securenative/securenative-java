package com.securenative.interceptors;

import com.securenative.snlogic.ModuleManager;
import com.securenative.snlogic.SecureNative;

public class InterceptorManager {
    public static void applyModuleInterceptors(ModuleManager moduleManager, SecureNative secureNative) {
        if (moduleManager.getFramework().toLowerCase().contains("spring")) {
            new SpringInterceptor(secureNative);
        }
        new SpringInterceptor(secureNative);  // Default interceptor
    }

    public static void applyAgentInterceptor(String sessionId) {
        new AgentHeaderInterceptor(sessionId);
    }
}
