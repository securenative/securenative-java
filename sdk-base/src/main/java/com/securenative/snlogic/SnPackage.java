package com.securenative.snlogic;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SnPackage {
    @JsonProperty("name") String name;
    @JsonProperty("version")  String version;
    @JsonProperty("dependencies")  Dependency[] dependencies;
    @JsonProperty("dependenciesHash")  String dependenciesHash;

    public SnPackage(String name, String version, Dependency[] dependencies, String dependenciesHash) {
        this.name = name;
        this.version = version;
        this.dependencies = dependencies;
        this.dependenciesHash = dependenciesHash;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Dependency[] getDependencies() {
        return dependencies;
    }

    public String getDependenciesHash() {
        return dependenciesHash;
    }
}
