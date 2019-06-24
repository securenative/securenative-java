package com.securenative.snlogic;

import com.securenative.models.ClientFingerPrint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UtilsTest {

    Utils utils;

    HttpServletRequest request;

    @Before
    public void setup() {
        request = mock(HttpServletRequest.class);
        utils = new Utils();
    }

    @Test
    public void getCookieWhenRequestNullExpectNullTest(){
        String cookie = utils.getCookie(null, "");
        Assert.assertEquals(cookie,null);
    }


    @Test
    public void getCookieWhenCookieNameEmpytExpectDefaultTest(){
        Cookie[] cookies = {new Cookie("_sn","value")};
        when(request.getCookies()).thenReturn(cookies);
        String cookie = utils.getCookie(request, "");
        Assert.assertEquals(cookie,"value");
    }

    @Test
    public void remoteIpFromRequestWhenRequestNullExpectEmptyString(){
        Assert.assertEquals(utils.remoteIpFromRequest(null),"");
    }


    @Test
    public void testBase64(){
        String encoded = "eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJla3MtYWRtaW4tdG9rZW4tNjlmZ2YiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoiZWtzLWFkbWluIiwia3ViZXJuZXRlcy5pby9zZXJ2a";
        String expected = "{\"alg\":\"RS256\",\"kid\":\"\"}{\"iss\":\"kubernetes/serviceaccount\",\"kubernetes.io/serviceaccount/namespace\":\"kube-system\",\"kubernetes.io/serviceaccount/secret.name\":\"eks-admin-token-69fgf\",\"kubernetes.io/serviceaccount/service-account.name\":\"eks-admin\",\"kubernetes.io/serv";
        Assert.assertEquals(new String(Base64.getDecoder().decode(encoded), Charset.forName("UTF-8")),expected);


    }












}
