package com.securenative.models;

import org.jetbrains.annotations.Nullable;

public class ActionItem {
    private String item;
    private Long timeout;

    public ActionItem(String item, @Nullable Long timeout) {
        this.item = item;
        this.timeout = timeout;
    }

    public String getItem() {
        return item;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
