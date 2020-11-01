package com.securenative.utils;

import com.securenative.config.SecureNativeConfigurationBuilder;
import com.securenative.config.SecureNativeOptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestUtilsTest {

    @Test
    @DisplayName("Extract ip using proxy headers ipv4")
    public void ExtractRequestWithProxyHeadersIPV4()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder()
                .withProxyHeaders(new ArrayList<>(Collections.singleton("CF-Connecting-IP"))).build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("CF-Connecting-IP", "203.0.113.1");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("203.0.113.1"));
    }

    @Test
    @DisplayName("Extract ip using proxy headers ipv6")
    public void ExtractRequestWithProxyHeadersIPV6()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder()
                .withProxyHeaders(new ArrayList<>(Collections.singleton("CF-Connecting-IP"))).build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("CF-Connecting-IP", "f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d"));
    }

    @Test
    @DisplayName("Extract ip using proxy headers with multiple ipv4")
    public void ExtractRequestWithProxyHeadersMultipleIPV4()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder()
                .withProxyHeaders(new ArrayList<>(Collections.singleton("CF-Connecting-IP"))).build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("CF-Connecting-IP", "141.246.115.116, 203.0.113.1, 12.34.56.3");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("141.246.115.116"));
    }
}
