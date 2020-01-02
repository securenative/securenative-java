package com.securenative.snlogic;

public class SnPackage {
    String artifactId;
    String groupId;
    String version;
    Dependency[] dependencies;
    String dependenciesHash;

    public SnPackage(String artifactId, String groupId, String version, Dependency[] dependencies, String dependenciesHash) {
        this.artifactId = artifactId;
        this.groupId = groupId;
        this.version = version;
        this.dependencies = dependencies;
        this.dependenciesHash = dependenciesHash;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
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
