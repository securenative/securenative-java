package com.securenative.utils;

import com.securenative.ResourceStreamImpl;
import com.securenative.config.ConfigurationManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionTest {

    @Test
    public void testVersionExtraction() throws IOException {

        String props = String.join(System.getProperty("line.separator"),
                "version=1.0.0",
                        "groupId=com.securenative.java",
                        "artifactId=securenative-java");

        InputStream inputStream = new ByteArrayInputStream(props.getBytes());
        ResourceStreamImpl resourceStream = Mockito.spy(new ResourceStreamImpl());
        Mockito.when(resourceStream.getInputStream("/META-INF/maven/com.securenative.java/securenative-java/pom.properties")).thenReturn(inputStream);

        VersionUtils.setResourceStream(resourceStream);

        assertThat(VersionUtils.getVersion()).isEqualTo("1.0.0");

        // restore resource stream
        VersionUtils.setResourceStream(new ResourceStreamImpl());
    }
}
