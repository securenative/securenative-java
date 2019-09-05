package com.securenative.snlogic;


import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.Device;
import com.securenative.models.Event;
import com.securenative.models.EventTypes;
import com.securenative.models.SnEvent;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class SecureNativeTest{
    static Utils utils;
    static SecureNative sn;
    static String API_KEY = "ApiKey";

    @BeforeClass
    public static void runOnceBeforeClass() throws SecureNativeSDKException {
        utils = mock(Utils.class);
        sn = SecureNative.init(API_KEY, null);
    }

    @Test
    public void makeSureItsTheSameInstanceTest() throws Exception {
        SecureNative sn0 = SecureNative.getInstance();
        Assert.assertEquals(sn0.getApiKey(),API_KEY);
    }

    @Test
    public void TestBasicTrack() throws Exception {
        SecureNative sn1 = SecureNative.getInstance();
        Event event = new SnEvent.EventBuilder(EventTypes.LOG_OUT.getType()).withCookieValue("ewoJImNpZCI6ICJjaWRWYWx1ZSIsCgkiZnAiOiAidiIKfQ==").withDevice(new Device("id")).withIp("ip").build();
        sn1.track(event);
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

    @Test
    public void testDecryption() throws Exception {
        String cookie = "821cb59a6647f1edf597956243e564b00c120f8ac1674a153fbd707da0707fb236ea040d1665f3d294aa1943afbae1b26b2b795a127f883ec221c10c881a147bb8acb7e760cd6f04edc21c396ee1f6c9627d9bf1315c484a970ce8930c2ed1011af7e8569325c7edcdf70396f1abca8486eabec24567bf215d2e60382c40e5c42af075379dacdf959cb3fef74f9c9d15";
        String apikey = "6EA4915349C0AAC6F6572DA4F6B00C42DAD33E75";
        Utils utils = new Utils();
        String a = utils.decryptAES(cookie,apikey);
        String e = "{\"cid\":\"198a41ff-a10f-4cda-a2f3-a9ca80c0703b\",\"fp\":\"6d8cabd95987f8318b1fe01593d5c2a5.24700f9f1986800ab4fcc880530dd0ed\"}";
        Assert.assertEquals(e,a);
    }
}