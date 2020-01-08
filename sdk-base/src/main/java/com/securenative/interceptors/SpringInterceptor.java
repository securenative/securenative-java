package com.securenative.interceptors;

import com.securenative.middleware.IMiddleware;
import com.securenative.middleware.SpringVerifyRequestMiddleware;
import com.securenative.middleware.SpringVerifyWebhookMiddleware;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class SpringInterceptor extends WebSecurityConfigurerAdapter implements Interceptor {
    private SpringVerifyRequestMiddleware verifyRequest;
    private SpringVerifyWebhookMiddleware verifyWebhook;

    public SpringInterceptor(IMiddleware middleware) {
        this.verifyRequest = middleware.getVerifyRequest();
        this.verifyWebhook = middleware.getVerifyWebhook();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(this.verifyWebhook, BasicAuthenticationFilter.class);
        http.addFilterAfter(this.verifyRequest, BasicAuthenticationFilter.class);
    }

    @Override
    public Boolean canExecute() {
        return null;
    }

    @Override
    public void getModule() {

    }
}
