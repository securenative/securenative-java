package com.securenative.context;

import com.securenative.config.SecureNativeOptions;
import com.securenative.utils.RequestUtils;
import com.securenative.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class SecureNativeContextBuilder {
    private final SecureNativeContext context;

    private SecureNativeContextBuilder() {
        this.context = new SecureNativeContext();
    }

    public SecureNativeContextBuilder withClientToken(String clientToken) {
        this.context.setClientToken(clientToken);
        return this;
    }

    public SecureNativeContextBuilder withIp(String ip) {
        this.context.setIp(ip);
        return this;
    }

    public SecureNativeContextBuilder withRemoteIp(String remoteIp) {
        this.context.setRemoteIp(remoteIp);
        return this;
    }

    public SecureNativeContextBuilder withHeaders(Map<String, String> headers) {
        this.context.setHeaders(headers);
        return this;
    }

    public SecureNativeContextBuilder withUrl(String url) {
        this.context.setUrl(url);
        return this;
    }

    public SecureNativeContextBuilder withMethod(String method) {
        this.context.setMethod(method);
        return this;
    }

    public SecureNativeContextBuilder withBody(String body) {
        this.context.setBody(body);
        return this;
    }

    public static SecureNativeContextBuilder defaultContextBuilder() {
        return new SecureNativeContextBuilder();
    }

    public static SecureNativeContextBuilder fromHttpServletRequest(HttpServletRequest request, SecureNativeOptions options) {
        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);

        String clientToken = RequestUtils.getCookieValueFromRequest(request, RequestUtils.SECURENATIVE_COOKIE);
        if (Utils.isNullOrEmpty(clientToken)) {
            clientToken = RequestUtils.getSecureHeaderFromRequest(headers);
        }

        return new SecureNativeContextBuilder()
                .withUrl(request.getRequestURI())
                .withMethod(request.getMethod())
                .withHeaders(headers)
                .withClientToken(clientToken)
                .withIp(RequestUtils.getClientIpFromRequest(request, headers, options))
                .withRemoteIp(RequestUtils.getRemoteIpFromRequest(request))
                .withBody(null);
    }

    public SecureNativeContext build() {
        return this.context;
    }
}

