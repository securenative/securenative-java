package com.securenative.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class SnUtils {
    public static String calculateHash(String str) {
        return DigestUtils.sha256Hex(str);
    }
}
