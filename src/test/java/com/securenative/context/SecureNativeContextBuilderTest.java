package com.securenative.context;

import com.securenative.Maps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.Cookie;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


public class SecureNativeContextBuilderTest {
    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Create context from http servlet request test")
    public void createContextFromHttpServletRequestTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.setRequestURI("/login");
        request.setQueryString("param1=value1&param2=value2");
        request.setMethod("Post");
        request.setRemoteAddr("51.68.201.122");
        request.addHeader("x-securenative", "71532c1fad2c7f56118f7969e401f3cf080239140d208e7934e6a530818c37e544a0c2330a487bcc6fe4f662a57f265a3ed9f37871e80529128a5e4f2ca02db0fb975ded401398f698f19bb0cafd68a239c6caff99f6f105286ab695eaf3477365bdef524f5d70d9be1d1d474506b433aed05d7ed9a435eeca357de57817b37c638b6bb417ffb101eaf856987615a77a");

        SecureNativeContext context = SecureNativeContextBuilder.fromHttpServletRequest(request)
                .build();

        assertThat(context.getClientToken()).isEqualTo("71532c1fad2c7f56118f7969e401f3cf080239140d208e7934e6a530818c37e544a0c2330a487bcc6fe4f662a57f265a3ed9f37871e80529128a5e4f2ca02db0fb975ded401398f698f19bb0cafd68a239c6caff99f6f105286ab695eaf3477365bdef524f5d70d9be1d1d474506b433aed05d7ed9a435eeca357de57817b37c638b6bb417ffb101eaf856987615a77a");
        assertThat(context.getIp()).isEqualTo("51.68.201.122");
        assertThat(context.getMethod()).isEqualTo("Post");
        assertThat(context.getUrl()).isEqualTo("/login");
        assertThat(context.getRemoteIp()).isEqualTo("51.68.201.122");
        assertThat(context.getHeaders()).isEqualTo(Maps.defaultBuilder().put("x-securenative", "71532c1fad2c7f56118f7969e401f3cf080239140d208e7934e6a530818c37e544a0c2330a487bcc6fe4f662a57f265a3ed9f37871e80529128a5e4f2ca02db0fb975ded401398f698f19bb0cafd68a239c6caff99f6f105286ab695eaf3477365bdef524f5d70d9be1d1d474506b433aed05d7ed9a435eeca357de57817b37c638b6bb417ffb101eaf856987615a77a").build());
        assertThat(context.getBody()).isNull();
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Create context from http servlet request with cookie test")
    public void createContextFromHttpServletRequestWithCookieTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.securenative.com");
        request.setRequestURI("/login");
        request.setQueryString("param1=value1&param2=value2");
        request.setMethod("Post");
        request.setRemoteAddr("51.68.201.122");
        request.setCookies(new Cookie("_sn", "71532c1fad2c7f56118f7969e401f3cf080239140d208e7934e6a530818c37e544a0c2330a487bcc6fe4f662a57f265a3ed9f37871e80529128a5e4f2ca02db0fb975ded401398f698f19bb0cafd68a239c6caff99f6f105286ab695eaf3477365bdef524f5d70d9be1d1d474506b433aed05d7ed9a435eeca357de57817b37c638b6bb417ffb101eaf856987615a77a"));

        SecureNativeContext context = SecureNativeContextBuilder.fromHttpServletRequest(request)
                .build();

        assertThat(context.getClientToken()).isEqualTo("71532c1fad2c7f56118f7969e401f3cf080239140d208e7934e6a530818c37e544a0c2330a487bcc6fe4f662a57f265a3ed9f37871e80529128a5e4f2ca02db0fb975ded401398f698f19bb0cafd68a239c6caff99f6f105286ab695eaf3477365bdef524f5d70d9be1d1d474506b433aed05d7ed9a435eeca357de57817b37c638b6bb417ffb101eaf856987615a77a");
        assertThat(context.getIp()).isEqualTo("51.68.201.122");
        assertThat(context.getMethod()).isEqualTo("Post");
        assertThat(context.getUrl()).isEqualTo("/login");
        assertThat(context.getRemoteIp()).isEqualTo("51.68.201.122");
        assertThat(context.getHeaders()).isEqualTo(Maps.defaultBuilder().put("Cookie", "_sn=71532c1fad2c7f56118f7969e401f3cf080239140d208e7934e6a530818c37e544a0c2330a487bcc6fe4f662a57f265a3ed9f37871e80529128a5e4f2ca02db0fb975ded401398f698f19bb0cafd68a239c6caff99f6f105286ab695eaf3477365bdef524f5d70d9be1d1d474506b433aed05d7ed9a435eeca357de57817b37c638b6bb417ffb101eaf856987615a77a").build());
        assertThat(context.getBody()).isNull();
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Create default context builder")
    public void createDefaultContextBuilderTest() {
        SecureNativeContext context = SecureNativeContextBuilder.defaultContextBuilder()
                .build();

        assertThat(context.getClientToken()).isNull();
        assertThat(context.getIp()).isNull();
        assertThat(context.getMethod()).isNull();
        assertThat(context.getUrl()).isNull();
        assertThat(context.getRemoteIp()).isNull();
        assertThat(context.getHeaders()).isNull();
        assertThat(context.getBody()).isNull();
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Create custom context with ContextBuilder test")
    public void createCustomContextWithContextBuilderTest() {
        SecureNativeContext context = SecureNativeContextBuilder
                .defaultContextBuilder()
                .withUrl("/some-url")
                .withClientToken("SECRET_TOKEN")
                .withIp("10.0.0.0")
                .withBody("{ \"name\": \"YOUR_NAME\" }")
                .withMethod("Get")
                .withRemoteIp("10.0.0.1")
                .withHeaders(Maps.defaultBuilder()
                        .put("header1", "value1")
                        .build())
                .build();
        assertThat(context.getUrl()).isEqualTo("/some-url");
        assertThat(context.getClientToken()).isEqualTo("SECRET_TOKEN");
        assertThat(context.getIp()).isEqualTo("10.0.0.0");
        assertThat(context.getBody()).isEqualTo("{ \"name\": \"YOUR_NAME\" }");
        assertThat(context.getMethod()).isEqualTo("Get");
        assertThat(context.getRemoteIp()).isEqualTo("10.0.0.1");
        assertThat(context.getHeaders()).isEqualTo(Maps.defaultBuilder()
                .put("header1", "value1")
                .build());
    }
}

