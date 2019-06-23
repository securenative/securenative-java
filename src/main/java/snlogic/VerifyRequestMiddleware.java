package snlogic;

import com.google.common.base.Strings;
import models.RiskResult;
import models.ActionType;
import models.EventOptions;

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
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String cookie = utils.getCookie(req, null);
        if (Strings.isNullOrEmpty(cookie)){
            RiskResult response = this.sn.verify(new EventOptions(utils.remoteIpFromRequest(req), req.getHeader("user-agent"), EventTypes.VERIFY.getType()), req);
            if (ActionType.type.BLOCK.name() == response.getRiskLevel()){
                res.sendRedirect(String.valueOf(500));
            }
            if (ActionType.type.REDIRECT.name() == response.getRiskLevel()){
                res.sendRedirect("/error");
            }
        }
        filterChain.doFilter(req,res);
    }

    @Override
    public void destroy() {}
}
