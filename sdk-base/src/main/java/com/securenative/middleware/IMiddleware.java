package com.securenative.middleware;

public interface IMiddleware {
    SpringVerifyWebhookMiddleware getVerifyWebhook();
    SpringVerifyRequestMiddleware getVerifyRequest();
}
