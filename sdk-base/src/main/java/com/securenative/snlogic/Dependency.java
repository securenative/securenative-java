package com.securenative.snlogic;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Dependency {
    @JsonProperty("name") String name;
    @JsonProperty("version") String version;

    public Dependency(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
