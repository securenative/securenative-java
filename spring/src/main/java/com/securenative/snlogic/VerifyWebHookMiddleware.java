package com.securenative.snlogic;

import com.securenative.models.*;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;

public class VerifyWebHookMiddleware implements Filter {
    private String apikey;
    private Utils utils;
    private final String EMPTY = "";
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

    public String getCookie(HttpServletRequest request, final String cookieName) {
        if (request == null || request.getCookies() == null || request.getCookies().length == 0) {
            return null;
        }
        Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(x -> (utils.isNullOrEmpty(cookieName) ? utils.COOKIE_NAME : cookieName).equals(x.getName())).findFirst();
        return cookie.isPresent() ? cookie.get().getValue() : null;
    }

    public String remoteIpFromServletRequest(HttpServletRequest request) {
        if (request == null) {
            return EMPTY;
        }

        return utils.remoteIpFromRequest(request::getHeader);
    }

    public Event buildEventFromHttpServletRequest(HttpServletRequest request, Event event) {
        String encodedCookie = getCookie(request, event != null && !this.utils.isNullOrEmpty(event.getCookieName()) ? event.getCookieName() : this.utils.COOKIE_NAME);
        String eventype =  event == null || this.utils.isNullOrEmpty(event.getEventType()) ? EventTypes.LOG_IN.getType() : event.getEventType();
        String ip = event != null && event.getIp() != null ? event.getIp() : remoteIpFromServletRequest(request);
        String remoteIP = request.getRemoteAddr();
        String userAgent = event != null && event.getUserAgent() != null ? event.getUserAgent() : request.getHeader(this.utils.USERAGENT_HEADER);
        User user = event != null && event.getUser() != null ? event.getUser() : new User(null, null, "anonymous");
        Device device = event != null && event.getDevice() != null ? event.getDevice() : null;
        return new SnEvent.EventBuilder(eventype).withCookieValue(this.utils.isNullOrEmpty(encodedCookie) ? request.getHeader(utils.SN_HEADER) : encodedCookie).withIp(ip).withRemoteIP(remoteIP).withUserAgent(userAgent).withUser(user).withDevice(device).build();
    }


}
