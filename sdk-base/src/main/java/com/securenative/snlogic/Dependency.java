package com.securenative.snlogic;

public class Dependency {
    String groupId;
    String artifactId;
    String version;

    public Dependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }
}
