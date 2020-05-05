package com.securenative;

import com.securenative.exceptions.SecureNativeInvalidUriException;
import com.securenative.exceptions.SecureNativeParseException;
import com.securenative.models.Event;

import java.io.IOException;

public interface EventManager {
    <T> T sendSync(Class<T> clazz, Event event, String url) throws IOException, SecureNativeParseException;
    void sendAsync(Event event, String url, Boolean retry);
    void startEventsPersist();
    void stopEventsPersist();
}
