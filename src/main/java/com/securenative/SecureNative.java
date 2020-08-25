package com.securenative;

import com.securenative.config.ConfigurationManager;
import com.securenative.config.SecureNativeConfigurationBuilder;
import com.securenative.config.SecureNativeOptions;
import com.securenative.context.SecureNativeContextBuilder;
import com.securenative.exceptions.SecureNativeConfigException;
import com.securenative.exceptions.SecureNativeInvalidOptionsException;
import com.securenative.exceptions.SecureNativeSDKException;
import com.securenative.exceptions.SecureNativeSDKIllegalStateException;
import com.securenative.http.SecureNativeHTTPClient;
import com.securenative.models.EventOptions;
import com.securenative.models.VerifyResult;
import com.securenative.utils.SignatureUtils;
import com.securenative.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static com.securenative.utils.SignatureUtils.SIGNATURE_HEADER;

public class SecureNative implements ApiManager {
    public static final Logger logger = Logger.getLogger(SecureNative.class);
    private static SecureNative secureNative = null;
    private final ApiManager apiManager;
    private final SecureNativeOptions options;

    private SecureNative(SecureNativeOptions options) throws SecureNativeSDKException {
        if (Utils.isNullOrEmpty(options.getApiKey())) {
            throw new SecureNativeSDKException("You must pass your SecureNative api key");
        }
        this.options = options;

        EventManager eventManager = new SecureNativeEventManager(new SecureNativeHTTPClient(options), options);
        if (options.getAutoSend()) {
            eventManager.startEventsPersist();
        }
        this.apiManager = new ApiManagerImpl(eventManager, options);
        Logger.initLogger(options.getLogLevel());
    }

    public static SecureNative init(SecureNativeOptions options) throws SecureNativeSDKException {
        if (secureNative == null) {
            secureNative = new SecureNative(options);
            return secureNative;
        }
        throw new SecureNativeSDKException("This SDK was already initialized");
    }

    public static SecureNative init(String apiKey) throws SecureNativeSDKException, SecureNativeConfigException {
        if (Utils.isNullOrEmpty(apiKey)) {
            throw new SecureNativeConfigException("You must pass your SecureNative api key");
        }
        SecureNativeConfigurationBuilder builder = SecureNativeConfigurationBuilder.defaultConfigBuilder();
        SecureNativeOptions secureNativeOptions = builder.withApiKey(apiKey).build();
        return init(secureNativeOptions);
    }


    public static SecureNative init() throws SecureNativeSDKException, SecureNativeConfigException {
        SecureNativeOptions secureNativeOptions = ConfigurationManager.loadConfig();
        return init(secureNativeOptions);
    }

    public static SecureNative init(Path path) throws SecureNativeSDKException, SecureNativeConfigException {
        SecureNativeOptions secureNativeOptions = ConfigurationManager.loadConfig(path);
        return init(secureNativeOptions);
    }

    public static SecureNative getInstance() throws SecureNativeSDKIllegalStateException {
        if (secureNative == null) {
            throw new SecureNativeSDKIllegalStateException();
        }
        return secureNative;
    }

    public SecureNativeOptions getOptions() {
        return options;
    }

    public static SecureNativeConfigurationBuilder configBuilder() {
        return SecureNativeConfigurationBuilder.defaultConfigBuilder();
    }

    public static SecureNativeContextBuilder contextBuilder() {
        return SecureNativeContextBuilder.defaultContextBuilder();
    }

    public boolean verifyRequestPayload(HttpServletRequest request) throws IOException {
        String requestSignature = request.getHeader(SIGNATURE_HEADER);
        String body = request.getReader().lines().collect(Collectors.joining());

        return SignatureUtils.isValidSignature(requestSignature, body, this.options.getApiKey());
    }

    @Override
    public void track(EventOptions eventOptions) throws SecureNativeInvalidOptionsException {
        this.apiManager.track(eventOptions);
    }

    @Override
    public VerifyResult verify(EventOptions eventOptions) throws SecureNativeInvalidOptionsException {
        return this.apiManager.verify(eventOptions);
    }
}