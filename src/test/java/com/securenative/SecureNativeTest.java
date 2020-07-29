package com.securenative;

import com.securenative.config.SecureNativeOptions;
import com.securenative.enums.FailoverStrategy;
import com.securenative.exceptions.SecureNativeConfigException;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.exceptions.SecureNativeSDKIllegalStateException;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.SetSystemProperty;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecureNativeTest {
    @AfterEach
    public void cleanUp() throws NoSuchFieldException, IllegalAccessException {
        Field instance = SecureNative.class.getDeclaredField("secureNative");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @Order(1)
    @DisplayName("Should init SDK with all public methods defined")
    public void initSDKWithPublicMethodsDefinedTest() {
        assertThat(SecureNative.class).hasDeclaredMethods("track", "verify");
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @Order(2)
    @DisplayName("Should throw when getting SDK instance without init")
    public void getSDKInstanceWithoutInitThrowsTest() {
        assertThrows(SecureNativeSDKIllegalStateException.class, SecureNative::getInstance);
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @SetSystemProperty(key = "SECURENATIVE_CONFIG_FILE", value = "FILE_THAT_MISSING")
    @Order(3)
    @DisplayName("Should throw when try init sdk without api key")
    public void initSDKWithoutApiKeyShouldThrowTest() throws SecureNativeConfigException, SecureNativeSDKException {
        assertThrows(SecureNativeSDKException.class, () -> {
            SecureNative secureNative = SecureNative.init();
        });
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @Order(4)
    @DisplayName("Should throw when try init sdk with empty api key")
    public void initSDKWithEmptyApiKeyShouldThrowTest() {
        assertThrows(SecureNativeConfigException.class, () -> {
            SecureNative.init("");
        });
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @Order(5)
    @DisplayName("Should init SDK with API key and default options")
    public void initSDKWithApiKeyAndDefaultsTest() throws SecureNativeConfigException, SecureNativeSDKException {
        final String apiKey = "API_KEY";
        SecureNative secureNative = SecureNative.init(apiKey);
        SecureNativeOptions options = secureNative.getOptions();

        assertThat(options.getApiKey()).isEqualTo(apiKey);
        assertThat(options.getApiUrl()).isEqualTo("https://api.securenative.com/collector/api/v1");
        assertThat(options.getInterval()).isEqualTo(1000);
        assertThat(options.getTimeout()).isEqualTo(1500);
        assertThat(options.getTimeout()).isEqualTo(1500);
        assertThat(options.getMaxEvents()).isEqualTo(1000);
        assertThat(options.getAutoSend()).isEqualTo(true);
        assertThat(options.getAutoSend()).isEqualTo(true);
        assertThat(options.getDisabled()).isEqualTo(false);
        assertThat(options.getLogLevel()).isEqualTo("fatal");
        assertThat(options.getFailoverStrategy()).isEqualTo(FailoverStrategy.FAIL_OPEN);
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @Order(6)
    @DisplayName("Should throw exception when SDK initialized twice")
    public void initSDKTwiceWillThrowTest() {
        assertThrows(SecureNativeSDKException.class, () -> {
            SecureNative.init();
            SecureNative.init();
        });
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @Order(7)
    @DisplayName("Initialized SDK instance should match singleton")
    public void initSDKWithApiKeyAndGetInstanceShouldMatchTest() throws SecureNativeConfigException, SecureNativeSDKException {
        final String apiKey = "API_KEY";
        SecureNative secureNative = SecureNative.init(apiKey);

        assertThat(secureNative).isEqualTo(SecureNative.getInstance());
        assertThat(secureNative).isEqualTo(SecureNative.getInstance());
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @Order(8)
    @DisplayName("Should init SDK with builder correctly")
    public void initSDKWithBuilderTest() throws SecureNativeConfigException, SecureNativeSDKException {
        SecureNative secureNative = SecureNative.init(SecureNative.configBuilder()
                .withApiKey("API_KEY")
                .withMaxEvents(10)
                .withLogLevel("error")
                .build());

        SecureNativeOptions options = secureNative.getOptions();

        assertThat(options).extracting("apiKey", "maxEvents", "logLevel")
                .containsExactly("API_KEY", 10, "error");
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @Order(9)
    @DisplayName("Should init SDK with property file correctly")
    public void initSDKAndLoadFromPropertiesFileTest() throws SecureNativeConfigException, SecureNativeSDKException {
        SecureNative secureNative = SecureNative.init();
        SecureNativeOptions options = secureNative.getOptions();
        assertThat(options).extracting("apiKey", "timeout")
                .containsExactly("SOME_API_KEY", 2000);
    }
}
