package com.securenative.middleware;

import com.securenative.snlogic.SecureNative;

public class SpringMiddleware implements IMiddleware {
    private SpringVerifyWebhookMiddleware verifyWebhook;
    private SpringVerifyRequestMiddleware verifyRequest;

    public SpringMiddleware(SecureNative secureNative) {
        this.verifyRequest = new SpringVerifyRequestMiddleware(secureNative);
        this.verifyWebhook = new SpringVerifyWebhookMiddleware(secureNative);
    }

    public SpringVerifyWebhookMiddleware getVerifyWebhook() {
        return verifyWebhook;
    }

    public SpringVerifyRequestMiddleware getVerifyRequest() {
        return verifyRequest;
    }
}