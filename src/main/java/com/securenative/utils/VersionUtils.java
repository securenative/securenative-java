package com.securenative.utils;

import com.securenative.ResourceStream;
import com.securenative.ResourceStreamImpl;
import com.securenative.SecureNative;
import com.securenative.config.ConfigurationManager;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.InputStream;
import java.util.Properties;

public class VersionUtils {
    private static ResourceStream resourceStream = new ResourceStreamImpl();

    public static void setResourceStream(ResourceStream resourceStream) {
        VersionUtils.resourceStream = resourceStream;
    }

    public static synchronized String getVersion() {
        String version = null;

        // try to load from maven properties first
        try {
            Properties p = new Properties();
            InputStream is = resourceStream.getInputStream("/META-INF/maven/com.securenative.java/securenative-java/pom.properties");
            if (is != null) {
                p.load(is);
                version = p.getProperty("version", "");
            }
        } catch (Exception e) {
            // ignore
        }

        // fallback to using Java API
        if (version == null) {
            Package aPackage = VersionUtils.class.getPackage();
            if (aPackage != null) {
                version = aPackage.getImplementationVersion();
                if (version == null) {
                    version = aPackage.getSpecificationVersion();
                }
            }
        }

        if (version == null) {
            // we could not compute the version so use a blank
            version = "unknown";
        }

        return version;
    }
}



