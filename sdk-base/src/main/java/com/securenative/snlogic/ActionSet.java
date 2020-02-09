package com.securenative.snlogic;

import com.google.common.net.InetAddresses;
import com.securenative.models.ActionItem;
import com.securenative.models.SetType;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ActionSet {
    private String name;
    private Set<ActionItem> ip;
    private Set<ActionItem> user;
    private Set<ActionItem> country;

    public ActionSet(String name) {
        this.name = name;
        this.ip = new HashSet<>();
        this.user = new HashSet<>();
        this.country = new HashSet<>();
    }

    public void add(String type, String item, @Nullable Long timeout) {
        if (timeout != null) {
            CompletableFuture.delayedExecutor(timeout, TimeUnit.SECONDS).execute(() -> {
                this.delete(type, item, timeout);
            });
        }

        if (type.equals(SetType.IP.name())) {
            if (this.isValidIP(item)) {
                this.ip.add(new ActionItem(item, timeout));
            }
        } else if (type.equals(SetType.USER.name())) {
            this.user.add(new ActionItem(item, timeout));
        } else {
            this.country.add(new ActionItem(item, timeout));
        }
    }

    public boolean has(String type, String item, @Nullable Long timeout) {
        if (type.equals(SetType.IP.name())) {
            if (!this.isValidIP(item)) {
                return false;
            }
            return this.ip.contains(new ActionItem(item, timeout));
        } else if (type.equals(SetType.USER.name())) {
            return this.user.contains(new ActionItem(item, timeout));
        } else {
            return this.country.contains(new ActionItem(item, timeout));
        }
    }

    public void delete(String type, String item, @Nullable Long timeout) {
        if (type.equals(SetType.IP.name())) {
            this.ip.remove(new ActionItem(item, timeout));
        } else if (type.equals(SetType.USER.name())) {
            this.user.remove(new ActionItem(item, timeout));
        } else {
            this.country.remove(new ActionItem(item, timeout));
        }
    }

    private boolean isValidIP(String ip) {
        return InetAddresses.isInetAddress(ip);
    }

    public String getName() {
        return name;
    }

    public Set<ActionItem> getIp() {
        return ip;
    }

    public Set<ActionItem> getUser() {
        return user;
    }

    public Set<ActionItem> getCountry() {
        return country;
    }
}

