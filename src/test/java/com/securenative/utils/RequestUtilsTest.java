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

    @Test
    @DisplayName("extract ip using x-forwarded-for header ipv6")
    public void ExtractIpUsingXForwardedForHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("x-forwarded-for", "f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d"));
    }

    @Test
    @DisplayName("extract ip using x-forwarded-for header multiple ipv4")
    public void ExtractMultipleIpUsingXForwardedForHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("x-forwarded-for", "141.246.115.116, 203.0.113.1, 12.34.56.3");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("141.246.115.116"));
    }

    @Test
    @DisplayName("extract ip using x-client-ip header ipv6")
    public void ExtractIpUsingXClientIpHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("x-client-ip", "f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d"));
    }

    @Test
    @DisplayName("extract ip using x-client-ip header multiple ipv4")
    public void ExtractMultipleIpUsingXClientIpHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("x-client-ip", "141.246.115.116, 203.0.113.1, 12.34.56.3");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("141.246.115.116"));
    }

    @Test
    @DisplayName("extract ip using x-real-ip header ipv6")
    public void ExtractIpUsingXRealIpHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("x-real-ip", "f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d"));
    }

    @Test
    @DisplayName("extract ip using x-real-ip header multiple ipv4")
    public void ExtractMultipleIpUsingXRealIpHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("x-real-ip", "141.246.115.116, 203.0.113.1, 12.34.56.3");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("141.246.115.116"));
    }

    @Test
    @DisplayName("extract ip using x-forwarded header ipv6")
    public void ExtractIpUsingXForwardedHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("x-forwarded", "f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d"));
    }

    @Test
    @DisplayName("extract ip using x-forwarded header multiple ipv4")
    public void ExtractMultipleIpUsingXForwardedHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("x-forwarded", "141.246.115.116, 203.0.113.1, 12.34.56.3");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("141.246.115.116"));
    }

    @Test
    @DisplayName("extract ip using x-cluster-client-ip header ipv6")
    public void ExtractIpUsingXClusterClientIpHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("x-cluster-client-ip", "f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d"));
    }

    @Test
    @DisplayName("extract ip using x-cluster-client-ip header multiple ipv4")
    public void ExtractMultipleIpUsingXClusterClientIpHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("x-cluster-client-ip", "141.246.115.116, 203.0.113.1, 12.34.56.3");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("141.246.115.116"));
    }

    @Test
    @DisplayName("extract ip using forwarded-for header ipv6")
    public void ExtractIpUsingForwardedForHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("forwarded-for", "f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d"));
    }

    @Test
    @DisplayName("extract ip using forwarded-for header multiple ipv4")
    public void ExtractMultipleIpUsingForwardedForHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("forwarded-for", "141.246.115.116, 203.0.113.1, 12.34.56.3");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("141.246.115.116"));
    }

    @Test
    @DisplayName("extract ip using forwarded header ipv6")
    public void ExtractIpUsingForwardedHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("forwarded", "f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d"));
    }

    @Test
    @DisplayName("extract ip using forwarded header multiple ipv4")
    public void ExtractMultipleIpUsingForwardedHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("forwarded", "141.246.115.116, 203.0.113.1, 12.34.56.3");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("141.246.115.116"));
    }

    @Test
    @DisplayName("extract ip using via header ipv6")
    public void ExtractIpUsingViaHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("via", "f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("f71f:5bf9:25ff:1883:a8c4:eeff:7b80:aa2d"));
    }

    @Test
    @DisplayName("extract ip using via header multiple ipv4")
    public void ExtractMultipleIpUsingViaHeader()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("via", "141.246.115.116, 203.0.113.1, 12.34.56.3");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("141.246.115.116"));
    }

    @Test
    @DisplayName("extract ip using priority with x forwarded for")
    public void ExtractIpUsingPriorityWithXForwardedFor()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("x-forwarded-for", "203.0.113.1");
        request.addHeader("x-real-ip", "198.51.100.101");
        request.addHeader("x-client-ip", "198.51.100.102");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("203.0.113.1"));
    }

    @Test
    @DisplayName("extract ip using priority without x forwarded for")
    public void ExtractIpUsingPriorityWithoutXForwardedFor()
    {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.addHeader("x-real-ip", "198.51.100.101");
        request.addHeader("x-client-ip", "203.0.113.1, 141.246.115.116, 12.34.56.3");

        Map<String, String> headers = RequestUtils.getHeadersFromRequest(request);
        String clientIp = RequestUtils.getClientIpFromRequest(request, headers, options);

        assertThat(clientIp.equals("203.0.113.1"));
    }
}
