package com.securenative;

import com.fasterxml.jackson.databind.JsonNode;
import com.securenative.config.ConfigurationManager;
import com.securenative.exceptions.SecureNativeParseException;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.http.HTTPServerMock;
import com.securenative.models.Event;
import com.securenative.models.SampleEvent;
import okhttp3.mockwebserver.RecordedRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class EventManagerTest extends HTTPServerMock {
    private final Event event = new SampleEvent();

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should successfully send async event with status code 200")
    public void SendAsyncEventWithStatusCode200Test() throws SecureNativeSDKException, InterruptedException, JSONException {
        configBuilder =  ConfigurationManager.configBuilder()
                                             .withApiKey("YOUR_API_KEY")
                                             .withAutoSend(true)
                                             .withInterval(10);

        client = sandbox().mock(200);
        eventManager = new SecureNativeEventManager(client, options);
        eventManager.startEventsPersist();

        eventManager.sendAsync( event, "some-path/to-api",true);

        try{
            RecordedRequest lastRequest =  server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);
            String body = lastRequest != null ? lastRequest.getBody().readUtf8() : null;
            String expected = "{\"eventType\":\"custom-event\"}";

            JSONAssert.assertEquals(expected, body, false);
            assertThat(new JSONObject(body).has("timestamp")).isTrue();
        }finally {
            eventManager.stopEventsPersist();
        }
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should handle async event invalid json response with status 200")
    public void ShouldHandleInvalidJsonResponseWithStatus200Test() throws InterruptedException, JSONException, SecureNativeSDKException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY")
                .withAutoSend(true)
                .withInterval(10);

        client = sandbox().mock(200, "bla bla");
        eventManager = new SecureNativeEventManager(client, options);
        eventManager.startEventsPersist();

        try {
            // track async event
            eventManager.sendAsync(event, "some-path/to-api", true);

            RecordedRequest lastRequest = server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);
            String body = lastRequest != null ? lastRequest.getBody().readUtf8() : null;

            assertThat(body).isNotNull();
            //timestamp
            assertThat(new JSONObject(body).has("timestamp")).isTrue();
            //event type
            assertThat(new JSONObject(body).get("eventType")).isEqualTo(event.getEventType());
        } finally {
            eventManager.stopEventsPersist();
        }
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should not retry sending async event when status code 200")
    public void ShouldNotRetrySendingAsyncEventWhenStatusCode200Test() throws SecureNativeSDKException, InterruptedException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY")
                .withAutoSend(true)
                .withInterval(10);


        client = sandbox().mock(200);
        eventManager = new SecureNativeEventManager(client, options);
        eventManager.startEventsPersist();

        try {
            // track async event
            eventManager.sendAsync(event, "some-path/to-api", true);

            // ensure event to be sent
            RecordedRequest lastRequest = server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);

            // first attempt should be called
            assertThat(lastRequest).isNotNull();

            // let's give option to send again
            Thread.sleep(2 * options.getInterval());

            // should be called only once
            assertThat(server.getRequestCount()).isEqualTo(1);
        } finally {
            eventManager.stopEventsPersist();
        }
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should not retry sending async event when status code 401")
    public void ShouldNotRetrySendingAsyncEventWhenStatusCode401Test() throws SecureNativeSDKException, InterruptedException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY")
                .withAutoSend(true)
                .withInterval(10);


        client = sandbox().mock(401);
        eventManager = new SecureNativeEventManager(client, options);
        eventManager.startEventsPersist();

        try {
            // track async event
            eventManager.sendAsync(event, "some-path/to-api", true);

            // ensure event to be sent
            RecordedRequest lastRequest = server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);


            assertThat(lastRequest).isNotNull();

            // let give client time to retry
            Thread.sleep(10 * options.getInterval());

            // should be called only once
            assertThat(server.getRequestCount()).isEqualTo(1);
        } finally {
             eventManager.stopEventsPersist();
        }
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should retry sending async event when status code 500")
    public void ShouldRetrySendingAsyncEventWhenStatusCode500Test() throws SecureNativeSDKException, InterruptedException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY")
                .withAutoSend(true)
                .withInterval(10);

        client = sandbox().mock(500);
        eventManager = new SecureNativeEventManager(client, options);
        eventManager.startEventsPersist();

        try {
            // track async event
            eventManager.sendAsync(event, "some-path/to-api", true);

            // ensure event to be sent
            RecordedRequest lastRequest = server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);

            assertThat(lastRequest).isNotNull();

            //set mock to successful attempt
            mock(200);

            Thread.sleep(20 * options.getInterval());
            assertThat(server.getRequestCount()).isEqualTo(2);
        } finally {
            eventManager.stopEventsPersist();
        }
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should successfully send sync event with status code 200")
    public void ShouldSuccessfullySendSyncEventWithStatusCode200Test() throws SecureNativeSDKException, JSONException, InterruptedException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY");

        String resBody = "{ \"data\": true }";
        client = sandbox().mock(200, resBody);
        eventManager = new SecureNativeEventManager(client, options);

        // track async event
        JsonNode data = null;
        try {
            data = eventManager.sendSync(JsonNode.class, event, "some-path/to-api");
        } catch (Exception ignored) { }

        JSONAssert.assertEquals(resBody, data.toString(), false);

        RecordedRequest lastRequest = server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);
        String body = lastRequest != null ? lastRequest.getBody().readUtf8() : null;
        String expected = "{\"eventType\":\"custom-event\"}";

        JSONAssert.assertEquals(expected, body, false);
        assertThat(new JSONObject(body).has("timestamp")).isTrue();
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should send sync event and handle invalid json response")
    public void ShouldSendSyncEventAndHandleInvalidJsonResponseTest() throws SecureNativeSDKException, InterruptedException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY");

        client = sandbox().mock(200, "bla bla");
        eventManager = new SecureNativeEventManager(client, options);


        // track async event
        String resp = null;
        try {
            resp = eventManager.sendSync(String.class, event, "some-path/to-api");
        } catch (Exception ignored) { }

        RecordedRequest lastRequest = server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);
        //invalid json response will turn into null
        assertThat(resp).isNull();
        assertThat(lastRequest).isNotNull();
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should send sync event and handle invalid request url")
    public void ShouldSendSyncEventAndHandleInvalidRequestUrlTest() throws SecureNativeSDKException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY");

        client = sandbox().mock(500);
        eventManager = new SecureNativeEventManager(client, options);

        try {
            Object obj  = eventManager.sendSync(Object.class, event, "path what");
            assertThat(obj).isNull();
        } catch (Exception ignored) {

        }
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should send sync event and fail when status code 401")
    public void ShouldSendSyncEventAndFailWhenStatusCode401Test() throws SecureNativeSDKException, InterruptedException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY");


        client = sandbox().mock(401);
        eventManager = new SecureNativeEventManager(client, options);

        try {
            // track async event
             eventManager.sendSync(Object.class, event, "some-path/to-api");
        } catch (SecureNativeParseException | IOException ex) {
            RecordedRequest lastRequest =  server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);
            assertThat(lastRequest).isNotNull();
            assertThat(ex.getMessage()).contains("401");
        }
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should send sync event and fail when status code 500")
    public void ShouldSendSyncEventAndFailWhenStatusCode500Test() throws SecureNativeSDKException, InterruptedException {
        configBuilder = ConfigurationManager.configBuilder()
                .withApiKey("YOUR_API_KEY");

        client = sandbox().mock(500);
        eventManager = new SecureNativeEventManager(client, options);

        try {
            // track async event
             eventManager.sendSync(Object.class, event, "some-path/to-api");
        } catch (Exception ex) {
            RecordedRequest lastRequest =  server.takeRequest(10 * options.getInterval(), TimeUnit.MILLISECONDS);
            assertThat(lastRequest).isNotNull();
            assertThat(ex.getMessage()).contains("500");
        }
    }
}
