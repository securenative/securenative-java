package com.securenative.snlogic;

import javax.servlet.*;
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
