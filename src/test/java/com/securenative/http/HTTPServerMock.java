package com.securenative.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.EventManager;
import com.securenative.config.SecureNativeConfigurationBuilder;
import com.securenative.config.SecureNativeOptions;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class HTTPServerMock {
    private HttpUrl serverUrl;
    protected SecureNativeConfigurationBuilder configBuilder;
    protected MockWebServer server;
    protected SecureNativeOptions options;
    protected HttpClient client;
    protected EventManager eventManager;
    protected final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    protected void start() throws IOException {
        server = new MockWebServer();
        server.start();
        serverUrl = server.url("/api");
    }

    @AfterEach
    protected void shutdown() throws Exception {
        server.shutdown();
    }

    public HttpClient mock(int code){
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(code);
        server.enqueue(mockResponse);

        return this.client;
    }

    public HttpClient mock(int code, String body){
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(code);
        mockResponse.setBody(body);
        server.enqueue(mockResponse);

        return this.client;
    }

    protected HTTPServerMock sandbox(){
        this.options = configBuilder.withApiUrl(serverUrl.toString())
                                    .build();

        this.client = new SecureNativeHTTPClient(options);

        return this;
    }
}
