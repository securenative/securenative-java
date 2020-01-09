package com.securenative.interceptors;

import com.securenative.middleware.SpringVerifyRequestMiddleware;
import com.securenative.middleware.SpringVerifyWebhookMiddleware;
import com.securenative.snlogic.SecureNative;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class SpringInterceptor extends WebSecurityConfigurerAdapter {
    private SpringVerifyRequestMiddleware verifyRequest;
    private SpringVerifyWebhookMiddleware verifyWebhook;

    public SpringInterceptor(SecureNative secureNative) {
        this.verifyRequest = new SpringVerifyRequestMiddleware(secureNative);
        this.verifyWebhook = new SpringVerifyWebhookMiddleware(secureNative);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(this.verifyWebhook, BasicAuthenticationFilter.class);
        http.addFilterAfter(this.verifyRequest, BasicAuthenticationFilter.class);
    }
}
