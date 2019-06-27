package com.securenative.snlogic;

import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.AbstractMap;
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
        when(utils.getCookie(any(), anyString())).thenReturn("cookie");
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("k", "v"), new Cookie("_sn", "X3NuX3ZhbHVl")});
    }

    @Test(expected = SecureNativeSDKException.class)
    public void initializeSnWhenApiKeyNullTest() throws Exception {
        SecureNative sn = new SecureNative(null, null);
    }


    @Test(expected = SecureNativeSDKException.class)
    public void initializeSnWhenApiKeyEmptyTest() throws Exception {
        SecureNative sn = new SecureNative("", null);
    }

    @Test
    public void initializeSnWhenApiKeyGoodAndOptionsNullTest() throws Exception {
        SecureNative sn = new SecureNative("api", null);
        Assert.assertEquals(sn.getApiKey(),"api");
    }

    @Test
    public void callTrackWith7CustomParams() {
        List<AbstractMap.SimpleEntry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry<>("one", "one"));
        params.add(new AbstractMap.SimpleEntry<>("two", "two"));
        params.add(new AbstractMap.SimpleEntry<>("three", "three"));
        params.add(new AbstractMap.SimpleEntry<>("four", "four"));
        params.add(new AbstractMap.SimpleEntry<>("five", "five"));
        params.add(new AbstractMap.SimpleEntry<>("six", "six"));
        params.add(new AbstractMap.SimpleEntry<>("seven", "seven"));
        Assert.assertEquals(6,new SnEvent.EventBuilder("event").withParams(params).build().getParams().size());

    }


}
