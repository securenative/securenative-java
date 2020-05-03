package com.securenative.models;

import java.util.Map;

public class RequestContext {
    private String cid;
    private String vid;
    private String fp;
    private String ip;
    private String remoteIp;
    private Map<String, String> headers;
    private String url;
    private String method;

    public RequestContext() { }

    public RequestContext(String cid, String vid, String fp, String ip, String remoteIp, Map<String, String> headers, String url, String method) {
        this.cid = cid;
        this.vid = vid;
        this.fp = fp;
        this.ip = ip;
        this.remoteIp = remoteIp;
        this.headers = headers;
        this.url = url;
        this.method = method;
    }

    public String getCid() {
        return cid;
    }

    public String getVid() {
        return vid;
    }

    public String getFp() {
        return fp;
    }

    public String getIp() {
        return ip;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public static class RequestContextBuilder {
        private String cid;
        private String vid;
        private String fp;
        private String ip;
        private String remoteIp;
        private Map<String, String> headers;
        private String url;
        private String method;

        public RequestContextBuilder withCid(String cid) {
            this.cid = cid;
            return this;
        }

        public RequestContextBuilder withVid(String vid) {
            this.vid = vid;
            return this;
        }

        public RequestContextBuilder withFp(String fp) {
            this.fp = fp;
            return this;
        }

        public RequestContextBuilder withIp(String ip) {
            this.ip = ip;
            return this;
        }

        public RequestContextBuilder withRemoteIp(String remoteIp) {
            this.remoteIp = remoteIp;
            return this;
        }

        public RequestContextBuilder witHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public RequestContextBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public RequestContextBuilder withMethod(String method) {
            this.method = method;
            return this;
        }

        public RequestContext build(){
            return new RequestContext(cid, vid, fp, ip, remoteIp, headers, url, method);
        }
    }

}

