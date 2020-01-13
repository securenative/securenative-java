package com.securenative.snlogic;

import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.SecureNativeOptions;

public class SecureNativeAgent {
    public static void main(String[] args) {
        // Set package information
        String PACKAGE_FILE_NAME = "/pom.xml";
        SnPackage appPkg = PackageManager.getPackage(System.getProperty("user.dir").concat(PACKAGE_FILE_NAME));
        SecureNativeOptions config = ConfigurationManager.getConfig();

        // Set default app name
        config.setAppName(appPkg.groupId.concat(":").concat(appPkg.getArtifactId()));

        // Get relevant module
        ModuleManager moduleManager = new ModuleManager(appPkg);
        SecureNative secureNative = null;

        // Init logger
        Logger.setLoggingEnable(true);
        Logger.getLogger().debug(String.format("Loaded Configurations %s", config.toString()));

        // Start agent
        Logger.getLogger().debug("Starting version compatibility check");

        if (Utils.versionCompare(System.getProperty("java.version"), config.getMinSupportedVersion()) < 0) {
            Logger.getLogger().error(String.format("This version of Java %s isn't supported by SecureNative, minimum required version is %s", appPkg.version, config.getMinSupportedVersion()));
            Logger.getLogger().error("Visit our docs to find out more: https://docs.securenative.com/docs/integrations/sdk/#java");
            System.exit(1);
        }

        // TODO add shutdown hook
        try {
            secureNative = new SecureNative(moduleManager, config);
            secureNative.startAgent();
        } catch (SecureNativeSDKException e) {
            Logger.getLogger().error("Could not find securenative api key. aborting.");
            System.exit(1);
        }

        Logger.getLogger().debug("Received exit signal, exiting..");
        secureNative.stopAgent();
        System.exit(0);
    }
}
