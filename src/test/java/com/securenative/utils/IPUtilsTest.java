package com.securenative.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

public class IPUtilsTest {
    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Check if ip is valid ipv4")
    public void isIpAddressValidIpV4Test() {
        String validIPv4 = "172.16.254.1";
        Assertions.assertThat(IPUtils.isIpAddress(validIPv4)).isTrue();
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Check if ip is valid ipv6")
    public void isIpAddressValidIpV6Test() {
        String validIPv6 = "2001:db8:1234:0000:0000:0000:0000:0000";
        Assertions.assertThat(IPUtils.isIpAddress(validIPv6)).isTrue();
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Check if ip is invalid ipv4")
    public void isIpAddressInvalidIpV4Test() {
        String validIPv4 = "172.16.2541";
        Assertions.assertThat(IPUtils.isIpAddress(validIPv4)).isFalse();
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Check if ip is invalid ipv6")
    public void isIpAddressInvalidIpV6Test() {
        String validIPv6 = "2001:db8:1234:0000";
        Assertions.assertThat(IPUtils.isIpAddress(validIPv6)).isFalse();
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Check if ip is valid public ip")
    public void isValidPublicIpTest() {
        String ip = "64.71.222.37";
        Assertions.assertThat(IPUtils.isValidPublicIp(ip)).isTrue();
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Check if ip is not valid public ip")
    public void isNotValidPublicIpTest() {
        String ip = "10.0.0.0";
        Assertions.assertThat(IPUtils.isValidPublicIp(ip)).isFalse();
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Check if ip is valid loopback ip")
    public void isValidLoopbackIpTest() {
        String ip = "127.0.0.1";
        Assertions.assertThat(IPUtils.isLoopBack(ip)).isTrue();
    }
}

