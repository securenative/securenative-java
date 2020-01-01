package com.securenative.utils;

import java.util.HashMap;

public class SnPackage {
    String name;
    String description;
    String version;
    HashMap<String, String> dependencies;
    String dependenciesHash;

    public SnPackage(String name, String description, String version, HashMap<String, String> dependencies, String dependenciesHash) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.dependencies = dependencies;
        this.dependenciesHash = dependenciesHash;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public HashMap<String, String> getDependencies() {
        return dependencies;
    }

    public String getDependenciesHash() {
        return dependenciesHash;
    }
}
