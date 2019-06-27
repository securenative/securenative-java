package com.securenative.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.snlogic.Utils;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class SnEvent implements Event {

    private final static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private String eventType;
    private String cid;
    private String vid;
    private String fp;
    private String ip;
    private String remoteIP;
    private String userAgent;
    private User user;
    private long ts;
    private Device device;
    private String CookieName;
    private String CookieValue;
    private List<AbstractMap.SimpleEntry<String, String>> params;

    public static class EventBuilder {
        private final int MAX_CUSTOM_PARAMS = 6;

        private String eventType;
        private String cid;
        private String fp;
        private String ip;
        private String remoteIP;
        private String userAgent;
        private User user;
        private Device device;
        private String cookieName;
        private String cookieValue;
        private Utils utils;
        private List<AbstractMap.SimpleEntry<String, String>> params;


        public EventBuilder(String eventType) {
            this.eventType = eventType;
            this.utils = new Utils();
        }

        public EventBuilder withIp(String ip) {
            this.ip = ip;
            return this;
        }

        public EventBuilder withRemoteIP(String remoteIP) {
            this.remoteIP = remoteIP;
            return this;
        }

        public EventBuilder withUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public EventBuilder withUser(User user) {
            this.user = user;
            return this;
        }


        public EventBuilder withDevice(Device device) {
            this.device = device;
            return this;
        }

        public EventBuilder withCookieValue(String cookieBase64Value) {
            if (this.utils.isNullOrEmpty(cookieBase64Value)) {
                return this;
            }
            String decodedCookie = new String(Base64.getDecoder().decode(cookieBase64Value), DEFAULT_CHARSET);
            ClientFingerPrint clientFP = this.parseClientFP(decodedCookie);
            this.cookieValue = cookieBase64Value;
            this.cid = clientFP != null ? clientFP.getCid() : "";
            this.fp = clientFP != null ? clientFP.getFp() : "";
            return this;
        }

        public EventBuilder withParams(List<AbstractMap.SimpleEntry<String, String>> params){
            if (params.size() > MAX_CUSTOM_PARAMS){
                this.params = params.subList(0,MAX_CUSTOM_PARAMS);
            }
            else{
                this.params = params;
            }

            return this;
        }

        public Event build() {
            SnEvent event = new SnEvent();
            event.eventType = this.eventType;
            event.cid = this.cid;
            event.vid = UUID.randomUUID().toString();
            event.fp = this.fp;
            event.ip = this.ip;
            event.remoteIP = this.remoteIP;
            event.userAgent = this.userAgent;
            event.user = this.user;
            event.ts = Instant.now().getEpochSecond();
            event.device = this.device;
            event.CookieName = this.cookieName;
            event.CookieValue = this.cookieValue;
            event.params = this.params;
            return event;
        }

        private ClientFingerPrint parseClientFP(String json) {
            if (this.utils.isNullOrEmpty(json)) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(json, ClientFingerPrint.class);
            } catch (Exception e) {
                return null;
            }
        }

        public String base64decode(String encodedString) {
            if (this.utils.isNullOrEmpty(encodedString)) {
                return "";
            }
            return String.valueOf(Base64.getDecoder().decode(encodedString));
        }
    }


    private SnEvent() {

    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getFp() {
        return fp;
    }

    public void setFp(String fp) {
        this.fp = fp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRemoteIP() {
        return remoteIP;
    }

    public void setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }


    public String getCookieName() {
        return CookieName;
    }

    public void setCookieName(String cookieName) {
        CookieName = cookieName;
    }

    public String getCookieValue() {
        return CookieValue;
    }

    public void setCookieValue(String cookieValue) {
        CookieValue = cookieValue;
    }


    public List<AbstractMap.SimpleEntry<String, String>> getParams() {
        return params;
    }

    public void setParams(List<AbstractMap.SimpleEntry<String, String>> params) {
        this.params = params;
    }




}
