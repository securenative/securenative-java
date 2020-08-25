package com.securenative.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.Logger;
import com.securenative.SecureNative;
import com.securenative.config.SecureNativeOptions;
import com.securenative.context.SecureNativeContext;
import com.securenative.context.SecureNativeContextBuilder;
import com.securenative.exceptions.SecureNativeInvalidOptionsException;
import com.securenative.utils.DateUtils;
import com.securenative.utils.EncryptionUtils;

import java.util.*;

public class SDKEvent implements Event {
    private final String rid;
    public String eventType;
    public String userId;
    private final UserTraits userTraits;
    public RequestContext request;
    public String timestamp;
    public Map<Object, Object> properties;
    public static final Logger logger = Logger.getLogger(SecureNative.class);

    public SDKEvent(EventOptions event, SecureNativeOptions options) throws SecureNativeInvalidOptionsException {
        if (event.getUserId() == null || event.getUserId().length() <= 0 || event.getUserId().equals("")) {
            throw new SecureNativeInvalidOptionsException("Invalid event structure; User Id is missing");
        }

        if (event.getEvent() == null || event.getEvent().length() <= 0 || event.getEvent().equals("")) {
            throw new SecureNativeInvalidOptionsException("Invalid event structure; Event Type is missing");
        }

        SecureNativeContext context = event.getContext() != null ? event.getContext() : SecureNativeContextBuilder.defaultContextBuilder().build();

        ClientToken clientToken = decryptToken(context.getClientToken(), options.getApiKey());

        this.rid = UUID.randomUUID().toString();
        this.eventType = event.getEvent();
        this.userId = event.getUserId();
        this.userTraits = event.getUserTraits();
        this.request = new RequestContext.RequestContextBuilder()
                .withCid(clientToken.getCid())
                .withVid(clientToken.getVid())
                .withFp(clientToken.getFp())
                .withIp(context.getIp())
                .withRemoteIp(context.getRemoteIp())
                .withMethod(context.getMethod())
                .withUrl(context.getUrl())
                .witHeaders(context.getHeaders())
                .build();
        this.timestamp = DateUtils.toTimestamp(event.getTimestamp());
        this.properties = event.getProperties();
    }

    private ClientToken decryptToken(String token, String key) {
        if (token == null || token.length() == 0) {
             return new ClientToken();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String decryptedClientToken = EncryptionUtils.decrypt(token, key);
            return mapper.readValue(decryptedClientToken, ClientToken.class);
        } catch (Exception ex) {
            logger.error("Failed to decrypt token");
        }
        return new ClientToken();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public String getRid() {
        return rid;
    }

    public String getUserId() {
        return userId;
    }

    public UserTraits getUserTraits() {
        return userTraits;
    }

    public RequestContext getRequest() {
        return request;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Map<Object, Object> getProperties() {
        return properties;
    }
}