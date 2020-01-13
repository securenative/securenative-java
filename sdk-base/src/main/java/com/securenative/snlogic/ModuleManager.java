package com.securenative.snlogic;


public class ModuleManager {
    public String framework;
    public String frameworkVersion;

    public ModuleManager(SnPackage snPackage) {
        for (int i=0; i < snPackage.dependencies.length; i++) {
            if (snPackage.dependencies[i].name.toLowerCase().contains("spring")) {
                this.framework = snPackage.dependencies[i].name;
                this.frameworkVersion = snPackage.dependencies[i].version;
            }

            // Default framework
            if (this.framework == null) {
                this.framework = "spring";
            }
        }
    }

    public String getFramework() {
        return this.framework;
    }

    public String getFrameworkVersion() {
        return this.frameworkVersion;
    }
}
