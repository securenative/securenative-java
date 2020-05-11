package com.securenative.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class VersionTest {

    @Test
    public void testManifest() throws IOException {
        URL res = org.junit.Assert.class.getResource(org.junit.Assert.class.getSimpleName() + ".class");
        JarURLConnection conn = (JarURLConnection) res.openConnection();
        Manifest mf = conn.getManifest();

        Attributes atts = mf.getMainAttributes();
        for (Object v : atts.values()) {
            System.out.println(v);
        }
    }
}
