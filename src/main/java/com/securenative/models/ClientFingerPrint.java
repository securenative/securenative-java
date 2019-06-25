package com.securenative.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientFingerPrint {
    private String cid;
    private String fp;

    @JsonCreator
    public ClientFingerPrint(@JsonProperty("cid") String cid, @JsonProperty("fp") String fp) {
        this.cid = cid;
        this.fp = fp;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFp() {
        return fp;
    }

    public void setFp(String fp) {
        this.fp = fp;
    }
}
