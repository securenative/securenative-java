package com.securenative.utils;

import com.securenative.Logger;


public class Utils {
    private static final Logger logger = Logger.getLogger(Utils.class);

    public static boolean timingSafeEqual(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }

    public static Boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static Integer parseIntegerOrDefault(String str, Integer defaultValue) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static Boolean parseBooleanOrDefault(String str, Boolean defaultValue) {
        try {
            return Boolean.valueOf(str);
        } catch (Exception ex) {
            return defaultValue;
        }
    }
}
