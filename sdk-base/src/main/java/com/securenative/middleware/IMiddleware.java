package com.securenative.middleware;

public interface IMiddleware {
    void verifyWebhook(String ... args);
    void verifyRequest(String ... args);
    void errorHandler(String ... args);
}
