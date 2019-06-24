package com.securenative.models;

public class ClientFingerPrint {
    private String cid;
    private String fp;

    public ClientFingerPrint(String cid, String fp) {
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
