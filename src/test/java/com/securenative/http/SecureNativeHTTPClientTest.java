package com.securenative.http;
import com.securenative.config.ConfigurationManager;
import com.securenative.exceptions.SecureNativeInvalidUriException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class SecureNativeHTTPClientTest extends HTTPServerMock {
    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should make simple http post call")
    public void shouldMakeSimplePostCallTest() throws IOException, SecureNativeInvalidUriException {
        configBuilder =  ConfigurationManager.configBuilder()
                                             .withApiKey("YOUR_API_KEY");

        client = sandbox().mock(200, "SOME_BODY");

        String payload = "{\"event\":\"SOME_EVENT_NAME\"}";

        HttpResponse response = client.post("track", payload);

        assertThat(response.isOk()).isEqualTo(true);
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("SOME_BODY");
    }
}
