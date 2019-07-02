package com.securenative.snlogic;


import com.securenative.models.Device;
import com.securenative.models.Event;
import com.securenative.models.EventTypes;
import com.securenative.models.SnEvent;
import com.securenative.spring.VerifyWebHookMiddleware;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VerifyWebHookMiddlewareTest {

    HttpServletRequest request;
    Utils utils;
    VerifyWebHookMiddleware v;
    @Before
    public void setup() {
        v = new VerifyWebHookMiddleware("apikey");
        request = mock(HttpServletRequest.class);
        utils = mock(Utils.class);

    }

    @Test
    public void buildEventFromHttpServletWhenEventNullTest() throws Exception {
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("_sn","ewoJImNpZCI6ICJjaWRWYWx1ZSIsCgkiZnAiOiAidiIKfQ=="),new Cookie("n","v")});
        when(request.getHeader("header")).thenReturn("header");
        when(request.getHeader(this.utils.USERAGENT_HEADER)).thenReturn("user_agent_header_test");
        when(request.getRemoteAddr()).thenReturn("address");
        Event event = v.buildEventFromHttpServletRequest(request, null);
        Assert.assertEquals(event.getEventType(),"sn.user.login");
        Assert.assertEquals(event.getCid(),"cidValue");
        Assert.assertEquals(event.getFp(),"v");
        Assert.assertEquals(event.getIp(),"127.0.0.1");
        Assert.assertEquals(event.getRemoteIP(),"address");
        Assert.assertEquals(event.getUserAgent(),"user_agent_header_test");
        Assert.assertEquals(event.getUser().getEmail(),"anonymous");
        Assert.assertEquals(event.getCookieValue(),"ewoJImNpZCI6ICJjaWRWYWx1ZSIsCgkiZnAiOiAidiIKfQ==");
    }

    @Test
    public void buildEventFromhttpServletWhenEventValidTest() throws Exception {
        when(request.getHeader("header")).thenReturn("header");
        when(request.getHeader(this.utils.USERAGENT_HEADER)).thenReturn("user_agent_header_test");
        when(request.getRemoteAddr()).thenReturn("address");
        Event event = v.buildEventFromHttpServletRequest(request, new SnEvent.EventBuilder(EventTypes.LOG_OUT.getType()).withCookieValue("ewoJImNpZCI6ICJjaWRWYWx1ZSIsCgkiZnAiOiAidiIKfQ==").withDevice(new Device("id")).withIp("ip").build());
        Assert.assertEquals(event.getEventType(),"sn.user.logout");
        Assert.assertEquals(event.getCid(),"cidValue");
        Assert.assertEquals(event.getFp(),"v");
        Assert.assertEquals(event.getIp(),"ip");
        Assert.assertEquals(event.getDevice().getId(),"id");
        Assert.assertEquals(event.getCookieValue(),"ewoJImNpZCI6ICJjaWRWYWx1ZSIsCgkiZnAiOiAidiIKfQ==");
    }
}
