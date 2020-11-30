package com.securenative.utils;

import com.securenative.config.SecureNativeOptions;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtils {
    public final static String SECURENATIVE_COOKIE = "_sn";
    public final static String SECURENATIVE_HEADER = "x-securenative";
    private final static List<String> ipHeaders = Arrays.asList("x-forwarded-for", "x-client-ip", "x-real-ip", "x-forwarded", "x-cluster-client-ip", "forwarded-for", "forwarded", "via");
    private final static List<String> piiHeaders = Arrays.asList("authorization", "access_token", "apikey", "password", "passwd", "secret", "api_key");

    public static Map<String, String> getHeadersFromRequest(HttpServletRequest request, SecureNativeOptions options) {
        Map<String, String> headersMap = new HashMap<>();
        if (options != null && options.getPiiHeaders().size() > 0) {
            for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
                String headerName = headerNames.nextElement();
                if (!options.getPiiHeaders().contains(headerName.toLowerCase()) && !options.getPiiHeaders().contains(headerName.toUpperCase())) {
                    String headerValue = request.getHeader(headerName);
                    headersMap.put(headerName, headerValue);
                }
            }
        } else if (options != null && options.getPiiRegexPattern() != null) {
            for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
                String headerName = headerNames.nextElement();
                Pattern pattern = Pattern.compile(options.getPiiRegexPattern(), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(headerName);
                if (!matcher.find()) {
                    String headerValue = request.getHeader(headerName);
                    headersMap.put(headerName, headerValue);
                }
            }
        } else {
            for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
                String headerName = headerNames.nextElement();
                if (!piiHeaders.contains(headerName.toLowerCase()) && !piiHeaders.contains(headerName.toUpperCase())) {
                    String headerValue = request.getHeader(headerName);
                    headersMap.put(headerName, headerValue);
                }
            }
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

    public static String getClientIpFromRequest(HttpServletRequest request, Map<String, String> headers, SecureNativeOptions options) {
        if (options != null && options.getProxyHeaders().size() > 0) {
            for (String header : options.getProxyHeaders()) {
                if (headers.containsKey(header)) {
                    String headerValue = headers.get(header);

                    Optional<String> ip = Arrays.stream(headerValue.split(","))
                            .map(String::trim)
                            .filter(IPUtils::isIpAddress)
                            .filter(IPUtils::isValidPublicIp)
                            .findFirst();

                    if (ip.isPresent()) {
                        return ip.get();
                    }
                }
            }
        }

        Optional<String> bestCandidate = Optional.empty();
        for (String ipHeader : ipHeaders) {
            if (!headers.containsKey(ipHeader)) {
                continue;
            }
            String headerValue = headers.get(ipHeader);

            Optional<String> candidateIp = Arrays.stream(headerValue.split(","))
                    .map(String::trim)
                    .filter(IPUtils::isIpAddress)
                    .filter(IPUtils::isValidPublicIp)
                    .findFirst();

            if (candidateIp.isPresent()) {
                return candidateIp.get();
            } else if (!bestCandidate.isPresent()) {
                bestCandidate = Arrays.stream(headerValue.split(","))
                        .map(String::trim)
                        .filter(IPUtils::isLoopBack)
                        .findFirst();
            }
        }
        return bestCandidate.orElseGet(request::getRemoteAddr);
    }

    public static String getRemoteIpFromRequest(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
