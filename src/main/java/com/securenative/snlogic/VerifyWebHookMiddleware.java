package com.securenative.snlogic;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VerifyWebHookMiddleware implements Filter {
    private String apikey;
    private Utils utils;

    private final String SINATURE_KEY = "x-securenative";

    public VerifyWebHookMiddleware(String apiKey) {
        this.apikey = apiKey;
        this.utils = new Utils();

    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String signature = "";
        if (req != null && !this.utils.isNullOrEmpty(req.getHeader(SINATURE_KEY))){
            signature = req.getHeader(SINATURE_KEY);
        }
        String payload = getBody(servletRequest);
        if (utils.isVerifiedSnRequest(payload,signature,this.apikey)){
            filterChain.doFilter(req,res);
            return;
        }
        res.sendError(401, "Unauthorized");
        return;
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
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        return stringBuilder.toString();
    }

}
