package com.securenative.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtilsTest {
    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should convert date to iso8601 format")
    public void toTimestampTest() {
        String iso8601Date = "2000-01-01T00:00:00.000Z";
        Date date = Date.from(Instant.parse(iso8601Date));
        String result = DateUtils.toTimestamp(date);

        Assertions.assertThat(result).isEqualTo(iso8601Date);
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should generate current date in iso8601 format")
    public void generateTimestampTest() {
        String result = DateUtils.generateTimestamp();
        Assertions.assertThat(result).isNotEmpty();
    }
}
