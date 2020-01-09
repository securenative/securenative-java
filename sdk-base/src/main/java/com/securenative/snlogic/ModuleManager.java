package com.securenative.snlogic;


public class ModuleManager {
    public String framework;
    public String frameworkVersion;

    public ModuleManager(String modulePath) {
        SnPackage snPackage = PackageManager.getPackage(modulePath);

        for (int i=0; i < snPackage.dependencies.length; i++) {
            if (snPackage.dependencies[i].groupId.toLowerCase().contains("spring")) {
                this.framework = snPackage.dependencies[i].groupId;
                this.frameworkVersion = snPackage.dependencies[i].version;
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
