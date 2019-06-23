package snlogic;

import exceptions.SecureNativeSDKException;
import javafx.util.Pair;
import models.Device;
import models.EventOptions;
import models.User;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SecureNativeTest {

    HttpServletRequest request;
    Utils utils;

    @Before
    public void setup() {
        request = mock(HttpServletRequest.class);
        utils = mock(Utils.class);
        when(request.getRemoteAddr()).thenReturn("address");
        when(request.getHeader("header")).thenReturn("header");
        when(utils.getCookie(any(),anyString())).thenReturn("cookie");
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("k","v"),new Cookie("_sn","X3NuX3ZhbHVl")});
        when(utils.base64decode(anyString())).thenReturn("base");

    }

    @Test (expected = SecureNativeSDKException.class)
    public void initializeSnWhenApiKeyNullTest()  throws Exception {
        SecureNative sn = new SecureNative(null, null);
    }


    @Test (expected = SecureNativeSDKException.class)
    public void initializeSnWhenApiKeyEmptyTest()  throws Exception {
        SecureNative sn = new SecureNative("", null);
    }

    @Test
    public void initializeSnWhenApiKeyGoodAndOptionsNullTest()  throws Exception {
        SecureNative sn = new SecureNative("api", null);
    }

    @Test(expected = SecureNativeSDKException.class)
    public void initializeSnWithEmptyApikeyTest()  throws Exception {
        SecureNative sn = new SecureNative("", null);
    }

    @Test
    public void callTrackWith7CustomParams()  throws Exception {
        SecureNative sn = new SecureNative("api", null);
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair<>("one", "one"));
        params.add(new Pair<>("two", "two"));
        params.add(new Pair<>("three", "three"));
        params.add(new Pair<>("four", "four"));
        params.add(new Pair<>("five", "five"));
        params.add(new Pair<>("six", "six"));
        params.add(new Pair<>("seven", "seven"));
        EventOptions options = new EventOptions("ip", "remoteIP", "userAgent" , new Device("1"), new User("1","name","email") , "cookieName" , "eventType", params);
        sn.track(options, request);
    }

}
