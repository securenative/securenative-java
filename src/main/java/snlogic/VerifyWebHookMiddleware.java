package snlogic;

import com.google.common.base.Strings;
import exceptions.SecureNativeSDKException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class VerifyWebHookMiddleware implements Filter {
    private String apikey;
    private Utils utils;

    private final String SINATURE_KEY = "x-securenative";

    public VerifyWebHookMiddleware(String apiKey) {
        this.apikey = apiKey;
        this.utils = new Utils();

    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpServletRequestWrapper n = new HttpServletRequestWrapper(req);


        String signature = "";
        if (req != null && !Strings.isNullOrEmpty(req.getHeader(SINATURE_KEY))){
            signature = req.getHeader(SINATURE_KEY);
        }
        String payload = getBody(servletRequest);//.lines().collect(Collectors.joining(System.lineSeparator()));
        if (Strings.isNullOrEmpty(payload)) {
            res.sendError(400, "bad request");
            return;
        }
        String comparison_signature = "";
        try {
            comparison_signature = utils.calculateRFC2104HMAC(payload,apikey);
        } catch (SecureNativeSDKException e) {
            e.printStackTrace();
            res.sendError(400, "bad request");
            return;
        }
        if (!signature.equals(comparison_signature)){
            res.sendError(401, "Mismatched signatures");
            return;
        }
        filterChain.doFilter(req,res);
    }

    @Override
    public void destroy() {

    }


    private String getBody(ServletRequest servletRequest) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = servletRequest.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        //Store request pody content in 'body' variable
        return stringBuilder.toString();
    }

}
