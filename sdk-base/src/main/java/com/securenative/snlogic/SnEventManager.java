package com.securenative.snlogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.*;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.asynchttpclient.*;

import java.io.FileReader;
import java.io.IOException;
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

    public SnEventManager(String apiKey, SecureNativeOptions options) throws SecureNativeSDKException {
        this.utils = new Utils();
        events = new ConcurrentLinkedQueue<>();
        if (this.utils.isNullOrEmpty(apiKey) || options == null) {
            throw new SecureNativeSDKException("You must pass your com.securenative.snlogic.SecureNative api key");
        }
        this.asyncClient = initializeAsyncHttpClient(options);
        this.apiKey = apiKey;

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.execute(() -> {
            try {
                Thread.sleep((long)(Math.random() * 1000));
                Message msg = events.poll();
                if (msg != null) {
                    sendSync(msg.getSnEvent(), msg.getUrl());
                }
            }
             catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @Override
    public RiskResult sendSync(Event event, String url) {
        this.asyncClient.addHeader(AUTHORIZATION, this.apiKey).setUrl(url);

        try {
            this.asyncClient.setBody(mapper.writeValueAsString(event));
            Response response = this.asyncClient.execute().get();
            if (response == null || response.getStatusCode() > HTTP_STATUS_OK) {
                events.add(new Message(event, response.getUri().toUrl()));
            }
            String responseBody = response.getResponseBody();
            if (utils.isNullOrEmpty(responseBody)){
                return new RiskResult(RiskLevel.low.name(), 0.0, new String[0]);
            }
            return mapper.readValue(responseBody, RiskResult.class);
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }
        return new RiskResult(RiskLevel.low.name(), 0.0, new String[0]);

    }

    @Override
    public void sendAsync(Event event, String url) {
        this.asyncClient.setUrl(url).addHeader(AUTHORIZATION, this.apiKey);
        try {
            this.asyncClient.setBody(mapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        this.asyncClient.execute(
                new AsyncCompletionHandler<Object>() {
                    @Override
                    public Object onCompleted(Response response) {
                        if (response.getStatusCode() > HTTP_STATUS_OK) {
                            events.add(new Message(event, response.getUri().toUrl()));
                        }
                        return response;
                    }
                });

    }

    private BoundRequestBuilder initializeAsyncHttpClient(SecureNativeOptions options) {
        DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
                .setConnectTimeout((int) options.getTimeout())
                .setUserAgent(USER_AGENT_VALUE);
        AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);
        return client.preparePost(options.getApiUrl())
                .addHeader(SN_VERSION, this.getVersion()).addHeader("Accept", "application/json");

    }

    private String getVersion() {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model read = reader.read(new FileReader("pom.xml"));
            return read.getVersion();
        } catch (Exception e) {
            return "unknown";
        }
    }

}