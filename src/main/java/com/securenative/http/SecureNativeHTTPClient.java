package com.securenative.http;

import com.securenative.config.SecureNativeOptions;
import com.securenative.exceptions.SecureNativeInvalidUriException;
import com.securenative.http.HttpClient;
import com.securenative.http.HttpResponse;
import okhttp3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SecureNativeHTTPClient implements HttpClient {
    private final String USER_AGENT_VALUE = "SecureNative-java";
    private final String SN_VERSION = "SN-Version";
    private final OkHttpClient client;
    private final SecureNativeOptions options;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public SecureNativeHTTPClient(SecureNativeOptions options) {
        this.options = options;
        this.client = new OkHttpClient
                .Builder()
                .readTimeout(options.getTimeout(), TimeUnit.MILLISECONDS)
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
                .addInterceptor((chain) ->{
                        Request request = chain.request();
                        Request authenticatedRequest = request.newBuilder()
                                .header("Authorization", options.getApiKey()).build();
                        return chain.proceed(authenticatedRequest);
                }).build();
    }


    @Override
    public HttpResponse post(String path, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);

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
