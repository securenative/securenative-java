package com.securenative.snlogic;

import com.securenative.models.Event;
import com.securenative.models.EventTypes;
import com.securenative.models.SnEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.nio.charset.Charset;
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
        Assert.assertEquals(utils.remoteIpFromServletRequest(null),"");
    }


    @Test
    public void testBase64(){
        String encoded = "eyJpZCI6Im9iamVjdC05MTQ3ODo4MCIsImRlc2NyaXB0aW9uIjoiIiwic3ViamVjdHMiOlsiMkU4RTg0RjI5MEVFQjAwOTc2RDZFMTg0MjE5NkVFM0E0OEUxQjBCRiJdLCJyZXNvdXJjZXMiOlsib2JqZWN0LTkxNDc4OioiXSwiYWN0aW9ucyI6WyIqIl0sImVmZmVjdCI6ImFsbG93IiwiY29uZGl0aW9ucyI6bnVsbH0K";
        String expected = "{\"id\":\"object-91478:80\",\"description\":\"\",\"subjects\":[\"2E8E84F290EEB00976D6E1842196EE3A48E1B0BF\"],\"resources\":[\"object-91478:*\"],\"actions\":[\"*\"],\"effect\":\"allow\",\"conditions\":null}\n";
        Assert.assertEquals(new String(Base64.getDecoder().decode(encoded), Charset.forName("UTF-8")),expected);
    }

    @Test
    public void testSetCookieValue() {

        String encodedCookie = "eyJjaWQiOiJkYzgyYjdhZS00ODFkLTQyODItYTMyZC0xZTU1Njk2ZjNmZTQiLCJmcCI6Ijk5NGYzZjVjZTRiYWUwODQzMTRhOTFkNzgyN2I1MWYuMjQ3MDBmOWYxOTg2ODAwYWI0ZmNjODgwNTMwZGQwZWQifQ==";
        String expectedCid = "dc82b7ae-481d-4282-a32d-1e55696f3fe4";
        String expectedFp = "994f3f5ce4bae084314a91d7827b51f.24700f9f1986800ab4fcc880530dd0ed";

        Event event = new SnEvent.EventBuilder(EventTypes.AUTH_CHALLENGE.getType()).withCookieValue(encodedCookie).build();
        Assert.assertEquals(expectedCid, event.getCid());
        Assert.assertEquals(expectedFp, event.getFp());
    }

}
