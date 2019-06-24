package com.securenative.snlogic;

import com.google.common.base.Strings;
import com.securenative.models.ActionType;
import com.securenative.models.EventOptions;
import com.securenative.models.EventTypes;
import com.securenative.models.RiskResult;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VerifyRequestMiddleware implements Filter {

    private ISDK sn;

    private Utils utils;

    public VerifyRequestMiddleware(SecureNative sn) {
        this.sn = sn;
    }


    @Override
    public void init(FilterConfig filterConfig){
        utils = new Utils();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {}
}
