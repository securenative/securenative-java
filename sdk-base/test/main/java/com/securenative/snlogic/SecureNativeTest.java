package com.securenative.snlogic;


import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.Device;
import com.securenative.models.Event;
import com.securenative.models.EventTypes;
import com.securenative.models.SnEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class SecureNativeTest{
    Utils utils;


    @Before
    public void setup() {
        utils = mock(Utils.class);
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
    public void TestBasicTrack() throws Exception {
        SecureNative sn = new SecureNative("api", null);
        Event event = new SnEvent.EventBuilder(EventTypes.LOG_OUT.getType()).withCookieValue("ewoJImNpZCI6ICJjaWRWYWx1ZSIsCgkiZnAiOiAidiIKfQ==").withDevice(new Device("id")).withIp("ip").build();
        sn.track(event);
    }

    @Test
    public void TestBasicVerify() throws Exception {
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