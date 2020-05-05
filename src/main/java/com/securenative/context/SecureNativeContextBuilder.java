package com.securenative.context;

import com.securenative.utils.RequestUtils;
import com.securenative.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class SecureNativeContextBuilder {
    private final SecureNativeContext context;

    private SecureNativeContextBuilder() {
        this.context = new SecureNativeContext();
    }

    public SecureNativeContextBuilder clientToken(String clientToken) {
        this.context.setClientToken(clientToken);
        return this;
    }

    public SecureNativeContextBuilder ip(String ip) {
        this.context.setIp(ip);
        return this;
    }

    public SecureNativeContextBuilder remoteIp(String remoteIp) {
        this.context.setRemoteIp(remoteIp);
        return this;
    }

    public SecureNativeContextBuilder headers(Map<String, String> headers) {
        this.context.setHeaders(headers);
        return this;
    }

    public SecureNativeContextBuilder url(String url) {
        this.context.setUrl(url);
        return this;
    }

    public SecureNativeContextBuilder method(String method) {
        this.context.setMethod(method);
        return this;
    }

    public SecureNativeContextBuilder body(String body) {
        this.context.setBody(body);
        return this;
    }

    public static SecureNativeContextBuilder defaultContextBuilder(){
        return new SecureNativeContextBuilder();
    }

    public static SecureNativeContextBuilder fromHttpServletRequest(HttpServletRequest request) {
        Map<String,String> headers  = RequestUtils.getHeadersFromRequest(request);

        String clientToken = RequestUtils.getCookieValueFromRequest(request, RequestUtils.SECURENATIVE_COOKIE);
        if(Utils.isNullOrEmpty(clientToken)){
            clientToken = RequestUtils.getSecureHeaderFromRequest(headers);
        }

        return new SecureNativeContextBuilder()
                .url(request.getRequestURI())
                .method(request.getMethod())
                .headers(headers)
                .clientToken(clientToken)
                .ip(RequestUtils.getClientIpFromRequest(request, headers))
                .remoteIp(RequestUtils.getRemoteIpFromRequest(request))
                .body(null);
    }

    public SecureNativeContext build(){
        return this.context;
    }
}

