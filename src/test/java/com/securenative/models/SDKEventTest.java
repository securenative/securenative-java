package com.securenative.models;

import com.securenative.config.SecureNativeConfigurationBuilder;
import com.securenative.config.SecureNativeOptions;
import com.securenative.enums.EventTypes;
import com.securenative.exceptions.SecureNativeInvalidOptionsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertThrows;

public class SDKEventTest {
    @Test
    @DisplayName("Should throw when creating sdk event invalid user-id")
    public void createSDKEventInvalidUserIdThrowTest() {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();
        EventOptions event = new EventOptions(EventTypes.LOG_IN.getType());
        event.setUserId("");

        assertThrows(SecureNativeInvalidOptionsException.class, () -> {
            SDKEvent sdkEvent = new SDKEvent(event, options);
        });
    }

    @Test
    @DisplayName("Should throw when creating sdk event without user-id")
    public void createSDKEventWithoutUserIdThrowTest() {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();
        EventOptions event = new EventOptions(EventTypes.LOG_IN.getType());

        assertThrows(SecureNativeInvalidOptionsException.class, () -> {
            SDKEvent sdkEvent = new SDKEvent(event, options);
        });
    }

    @Test
    @DisplayName("Should throw when creating sdk event without event type")
    public void createSDKEventWithoutEventTypeThrowTest() {
        SecureNativeOptions options = SecureNativeConfigurationBuilder.defaultConfigBuilder().build();
        EventOptions event = new EventOptions("");

        assertThrows(SecureNativeInvalidOptionsException.class, () -> {
            SDKEvent sdkEvent = new SDKEvent(event, options);
        });
    }
}
