package com.securenative;


import com.securenative.context.SecureNativeContext;
import com.securenative.exceptions.SecureNativeInvalidOptionsException;
import com.securenative.models.EventOptions;
import com.securenative.enums.EventTypes;
import com.securenative.models.UserTraits;

import java.util.Date;
import java.util.Map;

public class EventOptionsBuilder {
    private final int MAX_PROPERTIES_SIZE = 10;
    private static final Logger logger = Logger.getLogger(EventOptionsBuilder.class);
    private final EventOptions eventOptions;

    public static EventOptionsBuilder builder(String eventType){
        return new EventOptionsBuilder(eventType);
    }

    public static EventOptionsBuilder builder(EventTypes eventType){
        return new EventOptionsBuilder(eventType.getType());
    }

    private EventOptionsBuilder(String eventType) {
        eventOptions = new EventOptions(eventType);
    }

    public EventOptionsBuilder userId(String userId) {
        this.eventOptions.setUserId(userId);
        return this;
    }

    public EventOptionsBuilder userTraits(UserTraits userTraits) {
        this.eventOptions.setUserTraits(userTraits);
        return this;
    }

    public EventOptionsBuilder context(SecureNativeContext context) {
        this.eventOptions.setContext(context);
        return this;
    }

    public EventOptionsBuilder properties(Map<Object, Object> properties) {
        this.eventOptions.setProperties(properties);
        return this;
    }

    public EventOptionsBuilder timestamp(Date timestamp) {
        this.eventOptions.setTimestamp(timestamp);
        return this;
    }

    public EventOptions build() throws SecureNativeInvalidOptionsException {
        if(this.eventOptions.getProperties().size() > MAX_PROPERTIES_SIZE){
            throw new SecureNativeInvalidOptionsException(String.format("You can have only up to %d custom properties", MAX_PROPERTIES_SIZE));
        }
        return this.eventOptions;
    }
}
