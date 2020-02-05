package com.securenative.snlogic;

import com.amazonaws.services.waf.model.IPSet;
import com.securenative.models.SetType;

import java.util.HashSet;
import java.util.Set;

public class ActionSet {
    private String name;
    private IPSet ip; // Todo add aws.waf ip set
    private Set<String> user;
    private Set<String> country;

    public ActionSet(String name) {
        this.name = name;
        this.ip = new IPSet();
        this.user = new HashSet<>();
        this.country = new HashSet<>();
    }

    public void add(String type, String item, double timeout) {
        if (type.equals(SetType.IP.name())) {
            this.ip.add(item);
        } else if (type.equals(SetType.USER.name())) {
            this.user.add(item);
        } else {
            this.country.add(item);
        }
    }

    public boolean has(String type, String item) {
        if (type.equals(SetType.IP.name())) {
            return this.ip.contains(item);
        } else if (type.equals(SetType.USER.name())) {
            return this.user.contains(item);
        } else {
            return this.country.contains(item);
        }
    }

    public void delete(String type, String item) {
        if (type.equals(SetType.IP.name())) {
            this.ip.remove(item);
        } else if (type.equals(SetType.USER.name())) {
            this.user.remove(item);
        } else {
            this.country.remove(item);
        }
    }

    private boolean isValidIP(Set<String> IPSet, String ip) {
        return false;
    }

    public String getName() {
        return name;
    }
}

