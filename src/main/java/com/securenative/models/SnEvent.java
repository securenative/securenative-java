package com.securenative.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public class SnEvent implements Event{

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

    public static class EventBuilder {

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
        private String cookieName;
        private String cookieValue;


        public EventBuilder(String eventType){
            this.eventType = eventType;
        }

        public EventBuilder withIp(String ip){
            this.ip = ip;
            return this;
        }

        public EventBuilder withRemoteIP(String remoteIP){
            this.remoteIP = remoteIP;
            return this;
        }

        public EventBuilder withUserAgent(String userAgent){
            this.userAgent = userAgent;
            return this;
        }

        public EventBuilder withUser(User user){
            this.user = user;
            return this;
        }


        public EventBuilder withDevice(Device device){
            this.device = device;
            return this;
        }

        public EventBuilder withCookieValue(String cookieBase64Value){
            if (Strings.isNullOrEmpty(cookieBase64Value)){
                return this;
            }
            ClientFingerPrint clientFP = this.parseClientFP( Base64.getDecoder().decode(cookieBase64Value).toString());
            this.cookieValue = cookieBase64Value;
            this.cid = clientFP != null ? clientFP.getCid() : "";
            this.fp = clientFP != null ? clientFP.getFp() : "";
            return this;
        }

        public Event build(){
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
            return event;
        }
        private ClientFingerPrint parseClientFP(String json) {
            if(Strings.isNullOrEmpty(json)){
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
            if (Strings.isNullOrEmpty(encodedString)){
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



}
