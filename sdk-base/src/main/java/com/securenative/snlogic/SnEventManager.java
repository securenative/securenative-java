package com.securenative.snlogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.*;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.asynchttpclient.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SnEventManager implements EventManager {
    private final String USER_AGENT_VALUE = "com.securenative.snlogic.SecureNative-java";
    private final String SN_VERSION = "SN-Version";
    private BoundRequestBuilder asyncClient;
    private String apiKey;
    private Utils utils;
    private ExecutorService executor;
    private ConcurrentLinkedQueue<Message> events;
    private ObjectMapper mapper;
    private int HTTP_STATUS_OK = 201;
    private String AUTHORIZATION = "Authorization";
    private SecureNativeOptions options;
    RiskResult defaultRiskResult = new RiskResult(RiskLevel.low.name(), 0.0, new String[0]);

    public SnEventManager(String apiKey, SecureNativeOptions options) throws SecureNativeSDKException {
        this.utils = new Utils();
        this.options = options;
        events = new ConcurrentLinkedQueue<>();
        if (this.utils.isNullOrEmpty(apiKey) || options == null) {
            throw new SecureNativeSDKException("You must pass a valid api key");
        }
        this.asyncClient = initializeAsyncHttpClient(options);
        this.apiKey = apiKey;

        if (this.options.getSdkEnabled() != null && !this.options.getSdkEnabled()) {
            executor = Executors.newSingleThreadScheduledExecutor();
            Logger.getLogger().info(String.format("Starting thread listening to messages queue"));
            executor.execute(() -> {
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                    Message msg = events.poll();
                    if (msg != null) {
                        sendSync(msg.getSnEvent(), msg.getUrl());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @Override
    public RiskResult sendSync(Event event, String url) {
        if (this.options.getSdkEnabled() != null && !this.options.getSdkEnabled()) {
            return defaultRiskResult;
        }

        this.asyncClient.setHeader(AUTHORIZATION, this.apiKey).setUrl(url);

        try {
            this.asyncClient.setBody(mapper.writeValueAsString(event));
            Response response = this.asyncClient.execute().get();
            if (response == null || response.getStatusCode() > HTTP_STATUS_OK) {
                Logger.getLogger().info(String.format("Secure Native http call failed to end point: %s  with event type %s. adding back to queue.", url, event.getEventType()));
                events.add(new Message(event, response.getUri().toUrl()));
            }
            String responseBody = response.getResponseBody();
            if (utils.isNullOrEmpty(responseBody)) {
                Logger.getLogger().info(String.format("Secure Native http call to %s returned with empty response. returning default risk result.", url));
                return defaultRiskResult;
            }
            Logger.getLogger().info(String.format("Secure Native http call to %s was successful.", url));
            return mapper.readValue(responseBody, RiskResult.class);
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }
        return defaultRiskResult;

    }

    @Override
    public void sendAsync(Event event, String url) {
        if (this.options.getSdkEnabled() != null && !this.options.getSdkEnabled()) {
            return;
        }
        this.asyncClient.setUrl(url).setHeader(AUTHORIZATION, this.apiKey);
        try {
            this.asyncClient.setBody(mapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            Logger.getLogger().info(String.format("Secure Native async http call failed to end point: %s  with event type %s. error: %s", url, event.getEventType(), e));
        }

        this.asyncClient.execute(
                new AsyncCompletionHandler<Object>() {
                    @Override
                    public Object onCompleted(Response response) {
                        if (response.getStatusCode() > HTTP_STATUS_OK) {
                            Logger.getLogger().info(String.format("Secure Native http call failed to end point: %s  with event type %s. adding back to queue.", url, event.getEventType()));
                            events.add(new Message(event, response.getUri().toUrl()));
                        }
                        return response;
                    }
                });

    }

    // TODO implement this
    @Override
    public void stopEventsPersist() {

    }

    @Override
    public void startEventsPersist() {

    }

    @Override
    public String sendAgentEvent(Event event, String requestUrl) {
        return null;
    }

    private BoundRequestBuilder initializeAsyncHttpClient(SecureNativeOptions options) {
        DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
                .setConnectTimeout((int) options.getTimeout())
                .setUserAgent(USER_AGENT_VALUE);
        AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);
        Logger.getLogger().info("Initialized Http client");
        return client.preparePost(options.getApiUrl())
                .addHeader(SN_VERSION, this.getVersion()).addHeader("Accept", "application/json");

    }

    private String getVersion() {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            String pomResource = "/META-INF/maven/com.securenative.java/sdk-base/pom.xml";
            Model read = reader.read(new InputStreamReader(SnEventManager.class.getResourceAsStream(pomResource)));
            return read.getParent().getVersion();
        } catch (Exception e) {
            return "unknown";
        }
    }

}