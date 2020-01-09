package com.securenative.interceptors;

import com.securenative.middleware.AgentHeaderMiddleware;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class AgentHeaderInterceptor extends WebSecurityConfigurerAdapter {
    private AgentHeaderMiddleware middleware;

    public AgentHeaderInterceptor(String sessionId) {
        this.middleware = new AgentHeaderMiddleware(sessionId);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(this.middleware, BasicAuthenticationFilter.class);
    }
}
