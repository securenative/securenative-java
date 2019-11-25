package com.securenative.snlogic;


import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class SecureNativeTest {
    static Utils utils;
    static ISDK sn;
    static String API_KEY = "ApiKey";

    @BeforeClass
    public static void runOnceBeforeClass() throws SecureNativeSDKException {
        utils = mock(Utils.class);
        sn = SecureNative.init(new SecureNativeOptions(API_KEY));
    }

    @Test
    public void initializatiionTest() throws Exception {
        ISDK sn0 = SecureNative.getInstance();
        Assert.assertEquals(sn0.getApiKey(), API_KEY);
    }

    @Test
    public void makeSureItsTheSameInstanceTest() throws Exception {
        ISDK sn0 = SecureNative.getInstance();
        Assert.assertEquals(sn0.getApiKey(), API_KEY);
    }

    @Test
    public void TestBasicTrack() throws Exception {
        ISDK sn1 = SecureNative.getInstance();
        Event event = new SnEvent.EventBuilder(EventTypes.LOG_OUT.getType()).withCookieValue("ewoJImNpZCI6ICJjaWRWYWx1ZSIsCgkiZnAiOiAidiIKfQ==").withDevice(new Device("id")).withIp("ip").build();
        sn1.track(event);
    }

    @Test(expected = SecureNativeSDKException.class)
    public void callTrackWith7CustomParams() throws SecureNativeSDKException {
        Map params = new HashMap();
        params.put("param_1", "one");
        params.put("param_2", "two");
        params.put("param_3", "three");
        params.put("param_4", "four");
        params.put("param_5", "five");
        params.put("param_6", "six");
        params.put("param_7", "seven");
        new SnEvent.EventBuilder("event").withParams(params).build();
    }

    @Test
    public void callTrackWithNullCustomParams() throws SecureNativeSDKException {
        Event event = new SnEvent.EventBuilder("event").withParams(null).build();
        Assert.assertEquals(event.getParams().size(), 6);
        Assert.assertTrue(event.getParams().keySet().contains("param_1"));
        Assert.assertTrue(event.getParams().keySet().contains("param_6"));
    }

    @Test
    public void testDecryption() throws Exception {
        String cookie = "821cb59a6647f1edf597956243e564b00c120f8ac1674a153fbd707da0707fb236ea040d1665f3d294aa1943afbae1b26b2b795a127f883ec221c10c881a147bb8acb7e760cd6f04edc21c396ee1f6c9627d9bf1315c484a970ce8930c2ed1011af7e8569325c7edcdf70396f1abca8486eabec24567bf215d2e60382c40e5c42af075379dacdf959cb3fef74f9c9d15";
        String apikey = "6EA4915349C0AAC6F6572DA4F6B00C42DAD33E75";
        Utils utils = new Utils();
        String a = utils.decrypt(cookie, apikey);
        String e = "{\"cid\":\"198a41ff-a10f-4cda-a2f3-a9ca80c0703b\",\"fp\":\"6d8cabd95987f8318b1fe01593d5c2a5.24700f9f1986800ab4fcc880530dd0ed\"}";
        Assert.assertEquals(e, a);
    }

    @Test
    public void testEncryptionDecryption() throws Exception {
        String apikey = "6EA4915349C0AAC6F6572DA4F6B00C42DAD33E75";
        String e = "{\"cid\":\"198a41ff-a10f-4cda-a2f3-a9ca80c0703b\",\"fp\":\"6d8cabd95987f8318b1fe01593d5c2a5.24700f9f1986800ab4fcc880530dd0ed\"}";
        Utils utils = new Utils();
        String a = utils.decrypt(utils.encrypt(e, apikey), apikey);
        Assert.assertEquals(e, a);
    }
}