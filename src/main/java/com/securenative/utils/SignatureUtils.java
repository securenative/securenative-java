package com.securenative.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Formatter;

import static com.securenative.utils.Utils.timingSafeEqual;

public class SignatureUtils {
    public final static String SIGNATURE_HEADER = "x-securenative";
    private static final String HMAC_SHA512 = "HmacSHA512";

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }

    private static String buildHmacSignature(String message, String key) {
        try {
            Mac hasher = Mac.getInstance(HMAC_SHA512);
            hasher.init(new SecretKeySpec(key.getBytes(), HMAC_SHA512));
            byte[] hash = hasher.doFinal(message.getBytes());
            return toHexString(hash);
        } catch (Exception ignored) {
        }

        return "";
    }

    public static boolean isValidSignature(String headerSignature, String payload, String apiKey) {
        String signed = buildHmacSignature(payload, apiKey);
        if (Utils.isNullOrEmpty(signed) || Utils.isNullOrEmpty(headerSignature)) {
            return false;
        }

        return timingSafeEqual(headerSignature.getBytes(), signed.getBytes());
    }
}
