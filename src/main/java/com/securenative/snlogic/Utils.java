package com.securenative.snlogic;

import com.google.common.base.Strings;
import com.securenative.exceptions.SecureNativeSDKException;
import org.apache.http.conn.util.InetAddressUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {
    private static String[] ipHeaders = {"x-forwarded-for", "x-client-ip", "x-real-ip", "x-forwarded", "x-cluster-client-ip", "forwarded-for", "forwarded", "via"};
    public String COOKIE_NAME = "_sn";
    public String USERAGENT_HEADER = "user-agent";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA512";

    private static String EMPTY = "";

    public Utils() {
    }


    public String getCookie(HttpServletRequest request, final String cookieName) {
        if (request == null || request.getCookies() == null || request.getCookies().length == 0) {
            return null;
        }
        Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(x -> (Strings.isNullOrEmpty(cookieName) ? COOKIE_NAME : cookieName).equals(x.getName())).findFirst();
        return cookie.isPresent() ? cookie.get().getValue() : null;
    }

    public String remoteIpFromRequest(Function<String, String> headerExtractor) {
        Optional<String> bestCandidate = Optional.empty();
        String header = "";
        for (int i = 0; i < ipHeaders.length; i++) {
            List<String> candidates = Arrays.asList();
            header = headerExtractor.apply(ipHeaders[i]);
            if (!Strings.isNullOrEmpty(header)) {
                candidates = Arrays.stream(header.split(",")).map(s -> s.trim()).filter(s -> !Strings.isNullOrEmpty(s) &&
                        (InetAddressUtils.isIPv4Address(s) || InetAddressUtils.isIPv6Address(s)) &&
                        !isPrivateIPAddress(s)).collect(Collectors.toList());
                if (candidates.size() > 0) {
                    return candidates.get(0);
                }
            }
            if (!bestCandidate.isPresent()) {
                bestCandidate = candidates.stream().filter(x -> isLoopBack(x)).findFirst();
            }
        }
        return "127.0.0.1";
    }

    public String remoteIpFromServletRequest(HttpServletRequest request) {
        if (request == null) {
            return EMPTY;
        }

        return this.remoteIpFromRequest(request::getHeader);
    }

    private boolean isLoopBack(String ip) {
        try {
            return InetAddress.getByName(ip).isLoopbackAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isPrivateIPAddress(String ipAddress) {
        InetAddress ia = null;
        try {
            InetAddress ad = InetAddress.getByName(ipAddress);
            byte[] ip = ad.getAddress();
            ia = InetAddress.getByAddress(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ia.isSiteLocalAddress();
    }

    private String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }

    public String calculateRFC2104HMAC(String data, String key) throws SecureNativeSDKException {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            return toHexString(mac.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SecureNativeSDKException("failed calculating hmac");
        }
    }

    private String calculateSignature (String payload, String apikey){
        if (Strings.isNullOrEmpty(payload)){
            return null;
        }
        try {
            return this.calculateRFC2104HMAC(payload,apikey);
        } catch (SecureNativeSDKException e) {
            return null;
        }
    }

    public boolean verifySnRequest(String payload, String hedaerSignature, String apiKey){
        String signed = calculateSignature(payload, apiKey);
        if (Strings.isNullOrEmpty(signed) || Strings.isNullOrEmpty(hedaerSignature)){
            return false;
        }
        return hedaerSignature.equals(signed);
    }



}
