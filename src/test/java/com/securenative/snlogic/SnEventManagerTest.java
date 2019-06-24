package com.securenative.snlogic;

import com.securenative.models.SecureNativeOptions;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;


public class SnEventManagerTest {
    EventManager snEventManager;
    HttpServletRequest request;
    Utils utils;

    @Before
    public void setup() {
        request = mock(HttpServletRequest.class);
        utils = mock(Utils.class);

    }

    @Test(expected = Exception.class)
    public void buildEventMangerWithNullOptions() throws Exception {
        snEventManager = new SnEventManager("1234", null);
    }

    @Test(expected = Exception.class)
    public void buildEventMangerWithNullApiKey() throws Exception {
        snEventManager = new SnEventManager(null, new SecureNativeOptions());
    }


}
