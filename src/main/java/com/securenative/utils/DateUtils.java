package com.securenative.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    private static final DateTimeFormatter ISO_8601_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static String generateTimestamp() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(ISO_8601_PATTERN);
    }

    public static String toTimestamp(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC).format(ISO_8601_PATTERN);
    }
}
