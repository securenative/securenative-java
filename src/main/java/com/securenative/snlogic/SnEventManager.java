package com.securenative.snlogic;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.models.*;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Strings.isNullOrEmpty;

public class SnEventManager implements EventManager {
    private final String SN_COOKIE_NAME = "_sn";
    private final String USERAGENT_HEADER = "user-agent";
    private final String EMPTY = "";
    private final String USER_AGENT_VALUE = "com.securenative.snlogic.SecureNative-java";
    private final String SN_VERSION = "SN-Version";
    private CloseableHttpClient client;
    private String apiKey;
    private Utils utils;
    private ExecutorService executor;
    private ConcurrentLinkedQueue<Message> events;
    private ObjectMapper mapper;

    public SnEventManager(String apiKey, SecureNativeOptions options) throws SecureNativeSDKException {
        events = new ConcurrentLinkedQueue<>();
        if (isNullOrEmpty(apiKey) || options == null) {
            throw new SecureNativeSDKException("You must pass your com.securenative.snlogic.SecureNative api key");
        }

        this.client = initializeHttpClient(options);
        this.apiKey = apiKey;
        utils = new Utils();
        executor =   Executors.newSingleThreadScheduledExecutor();
        executor.execute(() -> {
            Message msg = events.poll();
            if (msg != null){
                sendSync(msg.getSnEvent(),msg.getUrl());
            }
        });
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Event buildEventFromHttpServletRequest(HttpServletRequest request, Event event) {
        String encodedCookie = utils.getCookie(request, event != null && !Strings.isNullOrEmpty(event.getCookieName()) ? event.getCookieName() : SN_COOKIE_NAME);
        String eventype =  event == null || Strings.isNullOrEmpty(event.getEventType()) ? EventTypes.LOG_IN.name() : event.getEventType();
        String ip = event != null && event.getIp() != null ? event.getIp() : utils.remoteIpFromServletRequest(request);
        String remoteIP = request.getRemoteAddr();
        String userAgent = event != null && event.getUserAgent() != null ? event.getUserAgent() : request.getHeader(USERAGENT_HEADER);
        User user = event != null && event.getUser() != null ? event.getUser() : new User("anonymous", null, null);
        Device device = event != null && event.getDevice() != null ? event.getDevice() : null;
        return new SnEvent.EventBuilder(eventype).withCookieValue(encodedCookie).withIp(ip).withRemoteIP(remoteIP).withUserAgent(userAgent).withUser(user).withDevice(device).build();
    }

    @Override
    public RiskResult sendSync(Event event, String requestUrl) {
        String stringEvent = null;
        String line;
        try {
            stringEvent = mapper.writeValueAsString(event);

            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.addHeader(HttpHeaders.AUTHORIZATION,this.apiKey);
            httpPost.addHeader(USERAGENT_HEADER,USER_AGENT_VALUE);
            httpPost.addHeader(SN_VERSION,getVersion());
            httpPost.addHeader("Accept","application/json");

            httpPost.setEntity(new StringEntity(stringEvent));

            HttpResponse response = this.client.execute(httpPost);
            if(response.getStatusLine().getStatusCode() > 210){
                events.add(new Message(event,requestUrl));
                return new RiskResult(ActionType.ALLOW.name(), 0.0, new String[0]);
            }
            BufferedReader rd = new BufferedReader(
                   new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            try{
                return mapper.readValue(result.toString(), RiskResult.class);
            }
            catch (Exception e){
                return new RiskResult(RiskLevel.low.name(), 0.0, new String[0]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new RiskResult(ActionType.ALLOW.name(), 0.0, new String[0]);
    }

    @Override
    public void sendAsync(SnEvent event, String url) {
        //TODO: will be implemented in future version
    }

    private void setTimeout(Runnable runnable, int delay) { // Will be used in sendAsync
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    private CloseableHttpClient initializeHttpClient(SecureNativeOptions options){
        return HttpClients.custom().setUserAgent(USER_AGENT_VALUE)
                .setConnectionTimeToLive(options.getTimeout(), TimeUnit.MILLISECONDS)
                .setDefaultHeaders(Arrays.asList(new BasicHeader(SN_VERSION, this.getVersion()),
                        new BasicHeader(HttpHeaders.AUTHORIZATION, this.apiKey)))
                .build();
    }

    private String getVersion(){
        try{
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model read = reader.read(new FileReader("pom.xml"));
            return read.getVersion();
        }
        catch (Exception e){
            return "unknown";
        }
    }

}