package com.securenative.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class RequestUtils {
    public final static String SECURENATIVE_COOKIE = "_sn";
    public final static String SECURENATIVE_HEADER = "x-securenative";
    private final static List<String> ipHeaders =  Arrays.asList("x-forwarded-for", "x-client-ip", "x-real-ip", "x-forwarded", "x-cluster-client-ip", "forwarded-for", "forwarded", "via");

    public static Map<String, String> getHeadersFromRequest(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();
        for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headersMap.put(headerName, headerValue);
        }
        return headersMap;
    }

    public static String getSecureHeaderFromRequest(Map<String, String> headers) {
        return headers.getOrDefault(SECURENATIVE_HEADER, "");
    }

    public static String getCookieValueFromRequest(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static String getClientIpFromRequest(HttpServletRequest request, Map<String, String> headers) {
        Optional<String> bestCandidate = Optional.empty();
        for (String ipHeader: ipHeaders) {
            if(!headers.containsKey(ipHeader)){
                continue;
            }
            String headerValue = headers.get(ipHeader);

            Optional<String> candidateIp = Arrays.stream(headerValue.split(","))
                                                    .map(String::trim)
                                                    .filter(IPUtils::isIpAddress)
                                                    .filter(IPUtils::isValidPublicIp)
                                                    .findFirst();

            if(candidateIp.isPresent()){
                return  candidateIp.get();
            }else if(!bestCandidate.isPresent()) {
                bestCandidate = Arrays.stream(headerValue.split(","))
                                       .map(String::trim)
                                       .filter(IPUtils::isLoopBack)
                                       .findFirst();
            }
        }
        return bestCandidate.orElseGet(request::getRemoteAddr);
    }

    public static String getRemoteIpFromRequest(HttpServletRequest request){
        return request.getRemoteAddr();
    }
}
