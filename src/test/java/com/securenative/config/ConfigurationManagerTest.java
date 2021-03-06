package com.securenative.config;

import com.securenative.ResourceStreamImpl;
import com.securenative.enums.FailoverStrategy;
import com.securenative.exceptions.SecureNativeConfigException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConfigurationManagerTest {
    @SuppressWarnings({"unchecked"})
    private static void setEnv(String name, String val) throws ReflectiveOperationException {
        Map<String, String> env = System.getenv();
        Field field = env.getClass().getDeclaredField("m");
        field.setAccessible(true);
        ((Map<String, String>) field.get(env)).put(name, val);
    }

    @Test
    @Order(1)
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should parse config file correctly")
    public void ParseConfigFileCorrectlyTest() throws SecureNativeConfigException {
        String config = String.join(System.getProperty("line.separator"),
                "SECURENATIVE_API_KEY=SOME_API_KEY",
                "SECURENATIVE_APP_NAME=SOME_APP_NAME",
                "SECURENATIVE_API_URL=SOME_API_URL",
                "SECURENATIVE_INTERVAL=1000",
                "SECURENATIVE_HEARTBEAT_INTERVAL=5000",
                "SECURENATIVE_MAX_EVENTS=100",
                "SECURENATIVE_TIMEOUT=1500",
                "SECURENATIVE_AUTO_SEND=true",
                "SECURENATIVE_DISABLE=false",
                "SECURENATIVE_LOG_LEVEL=fatal",
                "SECURENATIVE_FAILOVER_STRATEGY=fail-closed",
                "SECURENATIVE_PROXY_HEADERS=CF-Connecting-IP,Some-Random-Ip",
                "SECURENATIVE_PII_HEADERS=authentication,apiKey",
                "SECURENATIVE_PII_REGEX_PATTERN=/http_auth_/i");

        InputStream inputStream = new ByteArrayInputStream(config.getBytes());

        ResourceStreamImpl resourceStream = Mockito.spy(new ResourceStreamImpl());
        Mockito.when(resourceStream.getInputStream("securenative.properties")).thenReturn(inputStream);

        ConfigurationManager.setResourceStream(resourceStream);
        SecureNativeOptions options = ConfigurationManager.loadConfig();

        assertThat(options).isNotNull();
        assertThat(options.getApiKey()).isEqualTo("SOME_API_KEY");
        assertThat(options.getApiUrl()).isEqualTo("SOME_API_URL");
        assertThat(options.getAutoSend()).isEqualTo(true);
        assertThat(options.getDisabled()).isEqualTo(false);
        assertThat(options.getFailoverStrategy()).isEqualTo(FailoverStrategy.FAIL_CLOSED);
        assertThat(options.getInterval()).isEqualTo(1000);
        assertThat(options.getLogLevel()).isEqualTo("fatal");
        assertThat(options.getMaxEvents()).isEqualTo(100);
        assertThat(options.getTimeout()).isEqualTo(1500);
        assertThat(options.getProxyHeaders().size() == 0);
        assertThat(options.getPiiHeaders().size() == 0);
        assertThat(options.getPiiRegexPattern()).isEqualTo("/http_auth_/i");

        // restore resource stream
        ConfigurationManager.setResourceStream(new ResourceStreamImpl());
    }

    @Test
    @Order(2)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should ignore unknown config file entries")
    public void IgnoreUnknownConfigInPropertiesFileTest() throws SecureNativeConfigException {
        String config = String.join(System.getProperty("line.separator"),
                "SECURENATIVE_UNKNOWN_KEY=SOME_UNKNOWN_KEY",
                "SECURENATIVE_TIMEOUT=7500");

        InputStream inputStream = new ByteArrayInputStream(config.getBytes());

        ResourceStreamImpl resourceStream = Mockito.spy(new ResourceStreamImpl());
        Mockito.when(resourceStream.getInputStream("securenative.properties")).thenReturn(inputStream);

        ConfigurationManager.setResourceStream(resourceStream);
        SecureNativeOptions options = ConfigurationManager.loadConfig();

        assertThat(options).isNotNull();
        assertThat(options.getTimeout()).isEqualTo(7500);
        // restore resource stream
        ConfigurationManager.setResourceStream(new ResourceStreamImpl());
    }


    @Test
    @Order(3)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should not throw when parsing invalid config file")
    public void handleInvalidConfigFileTest() throws SecureNativeConfigException {
        String config = "{bla bla bla}";

        InputStream inputStream = new ByteArrayInputStream(config.getBytes());

        ResourceStreamImpl resourceStream = Mockito.spy(new ResourceStreamImpl());
        Mockito.when(resourceStream.getInputStream("securenative.properties")).thenReturn(inputStream);

        ConfigurationManager.setResourceStream(resourceStream);
        SecureNativeOptions options = ConfigurationManager.loadConfig();

        assertThat(options).isNotNull();

        // restore resource stream
        ConfigurationManager.setResourceStream(new ResourceStreamImpl());
    }

    @Test
    @Order(4)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should ignore invalid config file entries")
    public void IgnoreInvalidConfigFileEntriesTest() throws SecureNativeConfigException {
        String config = String.join(System.getProperty("line.separator"),
                "SECURENATIVE_API_KEY=1",
                "SECURENATIVE_API_URL=3",
                "SECURENATIVE_TIMEOUT=bad timeout",
                "SECURENATIVE_FAILOVER_STRATEGY=fail-what");

        InputStream inputStream = new ByteArrayInputStream(config.getBytes());

        ResourceStreamImpl resourceStream = Mockito.spy(new ResourceStreamImpl());
        Mockito.when(resourceStream.getInputStream("securenative.properties")).thenReturn(inputStream);

        ConfigurationManager.setResourceStream(resourceStream);
        SecureNativeOptions options = ConfigurationManager.loadConfig();

        assertThat(options).isNotNull();
        assertThat(options.getFailoverStrategy()).isEqualTo(FailoverStrategy.FAIL_OPEN);
        // restore resource stream
        ConfigurationManager.setResourceStream(new ResourceStreamImpl());
    }

    @Test
    @Order(5)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should get default config when config file and env variables are missing")
    public void loadDefaultConfigTest() throws SecureNativeConfigException {
        ResourceStreamImpl resourceStream = Mockito.spy(new ResourceStreamImpl());
        Mockito.when(resourceStream.getInputStream("securenative.properties")).thenReturn(null);

        //set resource stream
        ConfigurationManager.setResourceStream(resourceStream);

        SecureNativeOptions options = ConfigurationManager.loadConfig();

        assertThat(options.getApiKey()).isNull();
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
        assertThat(options.getProxyHeaders().size() == 0);
        assertThat(options.getPiiHeaders().size() == 0);
        assertThat(options.getPiiRegexPattern()).isEqualTo(null);

        ConfigurationManager.setResourceStream(new ResourceStreamImpl());
    }

    @Test
    @Order(6)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should get config via env variables")
    public void getConfigFromEnvVariablesTest() throws SecureNativeConfigException, ReflectiveOperationException {
        setEnv("SECURENATIVE_API_KEY", "SOME_ENV_API_KEY");
        setEnv("SECURENATIVE_API_URL", "SOME_API_URL");
        setEnv("SECURENATIVE_INTERVAL", "6000");
        setEnv("SECURENATIVE_MAX_EVENTS", "700");
        setEnv("SECURENATIVE_TIMEOUT", "1700");
        setEnv("SECURENATIVE_AUTO_SEND", "false");
        setEnv("SECURENATIVE_DISABLE", "true");
        setEnv("SECURENATIVE_LOG_LEVEL", "fatal");
        setEnv("SECURENATIVE_FAILOVER_STRATEGY", "fail-closed");
        setEnv("SECURENATIVE_PROXY_HEADERS", "CF-Connecting-IP,Some-Random-Ip");

        SecureNativeOptions options = ConfigurationManager.loadConfig();

        assertThat(options.getApiKey()).isEqualTo("SOME_API_KEY");
        assertThat(options.getApiUrl()).isEqualTo("SOME_API_URL");
        assertThat(options.getInterval()).isEqualTo(6000);
        assertThat(options.getTimeout()).isEqualTo(2000);
        assertThat(options.getMaxEvents()).isEqualTo(700);
        assertThat(options.getAutoSend()).isEqualTo(false);
        assertThat(options.getDisabled()).isEqualTo(true);
        assertThat(options.getLogLevel()).isEqualTo("fatal");
        assertThat(options.getFailoverStrategy()).isEqualTo(FailoverStrategy.FAIL_CLOSED);
        assertThat(options.getProxyHeaders().size() == 2);

        setEnv("SECURENATIVE_API_KEY", "");
        setEnv("SECURENATIVE_API_URL", "");
        setEnv("SECURENATIVE_INTERVAL", "");
        setEnv("SECURENATIVE_MAX_EVENTS", "");
        setEnv("SECURENATIVE_TIMEOUT", "");
        setEnv("SECURENATIVE_AUTO_SEND", "");
        setEnv("SECURENATIVE_DISABLE", "");
        setEnv("SECURENATIVE_LOG_LEVEL", "");
        setEnv("SECURENATIVE_FAILOVER_STRATEGY", "");
        setEnv("SECURENATIVE_PROXY_HEADERS", "");
    }

    @Test
    @Order(7)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should overwrite env variables with vales from config file")
    public void overwriteEnvVariablesWithConfigFileTest() throws SecureNativeConfigException, ReflectiveOperationException {
        String config = String.join(System.getProperty("line.separator"),
                "SECURENATIVE_API_KEY=API_KEY_FROM_FILE",
                "SECURENATIVE_API_URL=API_URL_FROM_FILE",
                "SECURENATIVE_INTERVAL=1000",
                "SECURENATIVE_MAX_EVENTS=100",
                "SECURENATIVE_TIMEOUT=1500",
                "SECURENATIVE_AUTO_SEND=false",
                "SECURENATIVE_DISABLE=false",
                "SECURENATIVE_LOG_LEVEL=fatal",
                "SECURENATIVE_FAILOVER_STRATEGY=fail-closed",
                "SECURENATIVE_PROXY_HEADERS=CF-Connecting-IP,Some-Random-Ip");

        setEnv("SECURENATIVE_API_KEY", "API_KEY_FROM_ENV");
        setEnv("SECURENATIVE_API_URL", "API_URL_ENV");
        setEnv("SECURENATIVE_INTERVAL", "2000");
        setEnv("SECURENATIVE_MAX_EVENTS", "200");
        setEnv("SECURENATIVE_TIMEOUT", "3000");
        setEnv("SECURENATIVE_AUTO_SEND", "true");
        setEnv("SECURENATIVE_DISABLE", "true");
        setEnv("SECURENATIVE_LOG_LEVEL", "error");
        setEnv("SECURENATIVE_FAILOVER_STRATEGY", "fail-open");
        setEnv("SECURENATIVE_PROXY_HEADERS", "CF-Connecting-IP,Some-Random-Ip");

        InputStream inputStream = new ByteArrayInputStream(config.getBytes());

        ResourceStreamImpl resourceStream = Mockito.spy(new ResourceStreamImpl());
        Mockito.when(resourceStream.getInputStream("securenative.properties")).thenReturn(inputStream);

        ConfigurationManager.setResourceStream(resourceStream);
        SecureNativeOptions options = ConfigurationManager.loadConfig();
        ArrayList<String> proxyHeaders = new ArrayList<>();
        proxyHeaders.add("CF-Connecting-IP");
        proxyHeaders.add("Some-Random-Ip");

        assertThat(options).isNotNull();
        assertThat(options.getApiKey()).isEqualTo("API_KEY_FROM_FILE");
        assertThat(options.getApiUrl()).isEqualTo("API_URL_FROM_FILE");
        assertThat(options.getInterval()).isEqualTo(1000);
        assertThat(options.getTimeout()).isEqualTo(1500);
        assertThat(options.getMaxEvents()).isEqualTo(100);
        assertThat(options.getAutoSend()).isEqualTo(false);
        assertThat(options.getDisabled()).isEqualTo(false);
        assertThat(options.getLogLevel()).isEqualTo("fatal");
        assertThat(options.getFailoverStrategy()).isEqualTo(FailoverStrategy.FAIL_CLOSED);
        assertThat(options.getProxyHeaders()).isEqualTo(proxyHeaders);

        setEnv("SECURENATIVE_API_KEY", "");
        setEnv("SECURENATIVE_API_URL", "");
        setEnv("SECURENATIVE_INTERVAL", "");
        setEnv("SECURENATIVE_MAX_EVENTS", "");
        setEnv("SECURENATIVE_TIMEOUT", "");
        setEnv("SECURENATIVE_AUTO_SEND", "");
        setEnv("SECURENATIVE_DISABLE", "");
        setEnv("SECURENATIVE_LOG_LEVEL", "");
        setEnv("SECURENATIVE_FAILOVER_STRATEGY", "");
        setEnv("SECURENATIVE_PROXY_HEADERS", "");

        // restore resource stream
        ConfigurationManager.setResourceStream(new ResourceStreamImpl());
    }

    @Test
    @Order(8)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should set defaults for invalid enum properties")
    public void defaultValuesForInvalidEnumConfigPropsTest() throws SecureNativeConfigException {
        String config = String.join(System.getProperty("line.separator"),
                "SECURENATIVE_FAILOVER_STRATEGY=fail-something");

        InputStream inputStream = new ByteArrayInputStream(config.getBytes());

        ResourceStreamImpl resourceStream = Mockito.spy(new ResourceStreamImpl());
        Mockito.when(resourceStream.getInputStream("securenative.properties")).thenReturn(inputStream);

        ConfigurationManager.setResourceStream(resourceStream);
        SecureNativeOptions options = ConfigurationManager.loadConfig();

        assertThat(options).isNotNull();
        assertThat(options.getFailoverStrategy()).isEqualTo(FailoverStrategy.FAIL_OPEN);

        // restore resource stream
        ConfigurationManager.setResourceStream(new ResourceStreamImpl());
    }
}
