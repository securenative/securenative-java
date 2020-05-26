package com.securenative.http;

import com.securenative.config.SecureNativeOptions;
import com.securenative.utils.VersionUtils;
import okhttp3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SecureNativeHTTPClient implements HttpClient {
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String VERSION_HEADER = "SN-Version";
    private final String USER_AGENT_HEADER = "User-Agent";
    private final String USER_AGENT_HEADER_VALUE = "SecureNative-java";
    private final OkHttpClient client;
    private final SecureNativeOptions options;
    public static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    public SecureNativeHTTPClient(SecureNativeOptions options) {
        this.options = options;
        this.client = new OkHttpClient
                .Builder()
                .readTimeout(options.getTimeout(), TimeUnit.MILLISECONDS)
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
                .addInterceptor((chain) ->{
                        Request request = chain.request();
                        Request authenticatedRequest = request.newBuilder()
                                .header(USER_AGENT_HEADER, USER_AGENT_HEADER_VALUE)
                                .header(VERSION_HEADER, VersionUtils.getVersion())
                                .header(AUTHORIZATION_HEADER, options.getApiKey()).build();
                        return chain.proceed(authenticatedRequest);
                }).build();
    }


    @Override
    public HttpResponse post(String path, String payload) throws IOException {
        RequestBody body = RequestBody.create(payload, JSON_MEDIA_TYPE);
        
        String url = String.format("%s/%s", this.options.getApiUrl(), path);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = this.client.newCall(request).execute()) {
           int statusCode = response.code();
           String responseBody = response.body().string();
           return new HttpResponse(response.isSuccessful(), statusCode, responseBody);
        }
    }
}
