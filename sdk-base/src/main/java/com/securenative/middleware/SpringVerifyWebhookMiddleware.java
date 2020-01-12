package com.securenative.middleware;

import com.securenative.snlogic.Logger;
import com.securenative.snlogic.SecureNative;
import com.securenative.snlogic.Utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SpringVerifyWebhookMiddleware implements Filter {
    private SecureNative secureNative;
    private final String SIGNATURE_KEY = "x-securenative";
    private Utils utils;

    public SpringVerifyWebhookMiddleware(SecureNative secureNative) {
        this.secureNative = secureNative;
        this.utils = new Utils();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String signature = "";
        if (req != null && !Utils.isNullOrEmpty(req.getHeader(SIGNATURE_KEY))) {
            signature = req.getHeader(SIGNATURE_KEY);
        }
        String payload = getBody(servletRequest);
        if (this.utils.isVerifiedSnRequest(payload, signature, this.secureNative.getApiKey())) {
            filterChain.doFilter(req, res);
            return;
        }
        Logger.getLogger().info("Request have been blocked due to incompatible signature");
        res.sendError(401, "Unauthorized");
    }

    @Override
    public void destroy() {
    }

    private String getBody(ServletRequest servletRequest) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = servletRequest.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return stringBuilder.toString();
    }
}
