package com.securenative.snlogic;


import com.securenative.models.Device;
import com.securenative.models.Event;
import com.securenative.models.EventTypes;
import com.securenative.models.SnEvent;
import com.securenative.spring.VerifyWebHookMiddleware;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VerifyWebHookMiddlewareTest {

    HttpServletRequest request;
    Utils utils;
    VerifyWebHookMiddleware v;
    String key = "6EA4915349C0AAC6F6572DA4F6B00C42DAD33E75";
    String encrypted = "821cb59a6647f1edf597956243e564b00c120f8ac1674a153fbd707da0707fb236ea040d1665f3d294aa1943afbae1b26b2b795a127f883ec221c10c881a147bb8acb7e760cd6f04edc21c396ee1f6c9627d9bf1315c484a970ce8930c2ed1011af7e8569325c7edcdf70396f1abca8486eabec24567bf215d2e60382c40e5c42af075379dacdf959cb3fef74f9c9d15";
    String a = "{\"cid\":\"198a41ff-a10f-4cda-a2f3-a9ca80c0703b\",\"fp\":\"6d8cabd95987f8318b1fe01593d5c2a5.24700f9f1986800ab4fcc880530dd0ed\"}";

    @Before
    public void setup() {
        v = new VerifyWebHookMiddleware(key);
        request = mock(HttpServletRequest.class);
        utils = mock(Utils.class);

    }

    @Test
    public void buildEventFromHttpServletWhenEventNullTest() throws Exception {
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("_sn",encrypted),new Cookie("n","v")});
        when(request.getHeader("header")).thenReturn("header");
        when(request.getHeader(this.utils.USERAGENT_HEADER)).thenReturn("user_agent_header_test");
        when(request.getRemoteAddr()).thenReturn("address");
        Event event = v.buildEventFromHttpServletRequest(request, null);
        Assert.assertEquals(event.getEventType(),"sn.user.login");
        Assert.assertEquals(event.getCid(),"198a41ff-a10f-4cda-a2f3-a9ca80c0703b");
        Assert.assertEquals(event.getFp(),"6d8cabd95987f8318b1fe01593d5c2a5.24700f9f1986800ab4fcc880530dd0ed");
        Assert.assertEquals(event.getIp(),"127.0.0.1");
        Assert.assertEquals(event.getRemoteIP(),"address");
        Assert.assertEquals(event.getUserAgent(),"user_agent_header_test");
        Assert.assertEquals(event.getUser().getEmail(),"anonymous");
        Assert.assertEquals(event.getCookieValue(),encrypted);
    }

    @Test
    public void buildEventFromhttpServletWhenEventValidTest() throws Exception {
        when(request.getHeader("header")).thenReturn("header");
        when(request.getHeader(this.utils.USERAGENT_HEADER)).thenReturn("user_agent_header_test");
        when(request.getRemoteAddr()).thenReturn("address");
        Event event = v.buildEventFromHttpServletRequest(request, new SnEvent.EventBuilder(EventTypes.LOG_OUT.getType()).withCookieValue(encrypted).withDevice(new Device("id")).withIp("ip").build());
        Assert.assertEquals(event.getEventType(),"sn.user.logout");
        Assert.assertEquals(event.getCid(),"198a41ff-a10f-4cda-a2f3-a9ca80c0703b");
        Assert.assertEquals(event.getFp(),"6d8cabd95987f8318b1fe01593d5c2a5.24700f9f1986800ab4fcc880530dd0ed");
        Assert.assertEquals(event.getIp(),"ip");
        Assert.assertEquals(event.getDevice().getId(),"id");
        Assert.assertEquals(event.getCookieValue(),encrypted);
    }

    @Test
    public void testEncryptionDecryption() throws NoSuchPaddingException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        Utils utilsEnc = new Utils();
        String enc = utilsEnc.encrypt(a,key);
        String dec = utilsEnc.decrypt(enc,key);
        Assert.assertEquals(a,dec);

    }
}
