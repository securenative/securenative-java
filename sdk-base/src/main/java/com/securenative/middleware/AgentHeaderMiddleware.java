package com.securenative.middleware;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AgentHeaderMiddleware implements Filter {
    private String headerName = "SN-Agent-Session";
    private String sessoionId;

    public AgentHeaderMiddleware(String sessionId) {
        this.sessoionId = sessionId;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setHeader(this.headerName, this.sessoionId);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
