package com.securenative.utils;

import com.securenative.ResourceStream;
import com.securenative.ResourceStreamImpl;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

public class VersionUtils {
    private static ResourceStream resourceStream = new ResourceStreamImpl();

    public static String getVersion() {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            String POM_RESOURCE = "/META-INF/maven/com.securenative.java/pom.xml";
            Model read = reader.read(resourceStream.getInputStream(POM_RESOURCE));
            return read.getParent().getVersion();
        } catch (Exception e) {
            return "unknown";
        }
    }
}



