package com.securenative;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.securenative.exceptions.SecureNativeHttpException;
import com.securenative.exceptions.SecureNativeInvalidUriException;
import com.securenative.exceptions.SecureNativeParseException;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.http.HttpClient;
import com.securenative.http.HttpResponse;
import com.securenative.models.Event;
import com.securenative.models.RequestOptions;
import com.securenative.config.SecureNativeOptions;

import java.io.IOException;
import java.util.concurrent.*;


public class SecureNativeEventManager implements EventManager {
    private static final Logger logger = Logger.getLogger(SecureNativeEventManager.class);
    private final int[] coefficients = new int[] { 1, 1, 2, 3, 5, 8, 13 };
    private int attempt = 0;
    private Boolean sendEnabled = false;
    private ScheduledExecutorService scheduler;
    private final ConcurrentLinkedQueue<RequestOptions> events;
    private final ObjectMapper mapper;
    private final SecureNativeOptions options;
    private final HttpClient httpClient;

    public SecureNativeEventManager(HttpClient httpClient, SecureNativeOptions options) throws SecureNativeSDKException {
        this.options = options;
        this.httpClient = httpClient;

        this.events = new ConcurrentLinkedQueue<>();
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public <T> T sendSync(Class<T> clazz, Event event, String url) throws IOException, SecureNativeParseException {
        if (this.options.getDisabled()) {
            logger.warn("SDK is disabled, no operation will be performed");
            return null;
        }

        String body = mapper.writeValueAsString(event);
        logger.debug("Attempting to send event", body);
        HttpResponse response = this.httpClient.post(url, body);
        if (!response.isOk()) {
            logger.info(String.format("Secure Native http call failed to end point: %s  with event type %s. adding back to queue.", url, event.getEventType()));
            throw new IOException(String.valueOf(response.getStatusCode()));
        }

        String responseBody = response.getBody();
        try {
            return mapper.readValue(responseBody, clazz);
        }catch (Exception ex){
            logger.error("Failed to parse response body", ex.getMessage());
            throw new SecureNativeParseException(ex.getMessage());
        }
    }


    @Override
    public void sendAsync(Event event, String url, Boolean retry) {
        if (this.options.getDisabled()) {
            return;
        }

        try {
            String body = mapper.writeValueAsString(event);
            this.events.add(new RequestOptions(url, body, retry));
        } catch (JsonProcessingException e) {
            logger.error("Failed to deserialize event", e.getMessage());
        }
    }

    private void sendEvents() throws InterruptedException {
        if (!this.events.isEmpty() && this.sendEnabled) {
            RequestOptions requestOptions =  events.peek();
            try {
                String body = requestOptions.getBody();
                HttpResponse resp =  this.httpClient.post(requestOptions.getUrl(), body);
                if (resp.getStatusCode() == 401) {
                    requestOptions.setRetry(false);
                }
                if (!resp.isOk()) {
                    throw new SecureNativeHttpException(String.valueOf(resp.getStatusCode()));
                }
                logger.debug("Event successfully sent", body);
                // remove the event from queue
                events.remove(requestOptions);
            } catch (Exception ex) {
                logger.error("Failed to send event", ex.getMessage());
                if (requestOptions.getRetry()) {
                    if(attempt++ == coefficients.length){
                        attempt = 0;
                    }
                    int backoff = coefficients[attempt] * this.options.getInterval();
                    logger.debug("BackOff automatic sending by", backoff);
                    this.sendEnabled = false;
                    Thread.sleep(backoff);
                    this.sendEnabled = true;
                }else{
                    // remove the event from queue, retry: false
                    events.remove(requestOptions);
                }
            }
        }
    }

    public void startEventsPersist() {
        logger.debug("Starting automatic event persistence");
        if (!this.options.getAutoSend() || this.sendEnabled) {
            logger.debug("Automatic event persistence disabled, you should manually persist events");
            return;
        }

        this.sendEnabled = true;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.scheduler.scheduleWithFixedDelay(() -> {
            try { sendEvents(); } catch (InterruptedException ignored) { }
        }, 0, this.options.getInterval(), TimeUnit.MILLISECONDS);
    }

    public void stopEventsPersist() {
        if (this.sendEnabled) {
            logger.debug("Attempting to stop automatic event persistence");

            try {
                this.scheduler.shutdown();
                // drain event queue
                this.scheduler.awaitTermination(this.options.getTimeout(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException ignored) { }

            logger.debug("Stopped event persistence");
        }
    }
}