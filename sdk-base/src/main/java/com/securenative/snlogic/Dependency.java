package com.securenative.snlogic;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Dependency {
    @JsonProperty("key") String name;
    @JsonProperty("value") String version;

    public Dependency(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }
}
