package com.securenative.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientToken {
    private String cid;
    private String vid;
    private String fp;

    public ClientToken() {
    }

    @JsonCreator
    public ClientToken(@JsonProperty("cid") String cid, @JsonProperty("vid") String vid, @JsonProperty("fp") String fp) {
        this.cid = cid;
        this.fp = fp;
        this.vid = vid;
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
}
