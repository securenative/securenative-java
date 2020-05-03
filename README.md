# Java SDK for SecureNative

[SecureNative](https://www.securenative.com/) performs user monitoring by analyzing user interactions with your application and various factors such as network, devices, locations and access patterns to stop and prevent account takeover attacks.

## Install the SDK

When using Maven, add the following dependency to your `pom.xml` file:
```xml
<dependency>
    <groupId>com.securenative.java</groupId>
    <artifactId>sdk-base</artifactId>
    <version>LATEST</version>
</dependency>
```

When using Gradle, add the following dependency to your `build.gradle` file:
```gradle
compile group: 'com.securenative.java', name: 'sdk-parent', version: '0.3.1', ext: 'pom'
```

When using SBT, add the following dependency to your `build.sbt` file:
```sbt
libraryDependencies += "com.securenative.java" % "sdk-parent" % "0.3.1" pomOnly()
```

## Initialize the SDK

To get your *API KEY*, login to your SecureNative account and go to project settings page:

### Option 1: Initialize via Config file
SecureNative can automatically load your config from *securenative.properties* file or from the file that is specified in your *SECURENATIVE_CONFIG_FILE* env variable:

```java
SecureNative secureNative =  SecureNative.init();
```
### Option 2: Initialize via API Key

```java
SecureNative secureNative =  SecureNative.init("YOUR_API_KEY");
```

### Option 3: Initialize via ConfigurationBuilder
```java
SecureNative secureNative = SecureNative.init(SecureNative.configBuilder()
                                        .withApiKey("API_KEY")
                                        .withMaxEvents(10)
                                        .withLogLevel("error")
                                        .build()); 
```

## Getting SecureNative instance
Once initialized, sdk will create a singleton instance which you can get: 
```java
SecureNative secureNative = SecureNative.getInstance();
```

## Tracking events

Once the SDK has been initialized, tracking requests sent through the SDK
instance. Make sure you build event with the EventBuilder:

 ```java
SecureNative secureNative = SecureNative.getInstance();

SecureNativeContext context = SecureNative.contextBuilder()
        .withIp("127.0.0.1")
        .withClientToken("SECURED_CLIENT_TOKEN")
        .withHeaders(Maps.defaultBuilder()
                    .put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405")
                    .build())
        .build();

EventOptions eventOptions = EventOptionsBuilder.builder(EventTypes.LOG_IN)
        .userId("USER_ID")
        .userTraits(new UserTraits("'USER_NAME'", "'USER_EMAIL'"))
        .context(context)
        .properties(Maps.builder()
                .put("prop1", "CUSTOM_PARAM_VALUE")
                .put("prop2", true)
                .put("prop3", 3)
                .build())
        .timestamp(new Date())
        .build();

secureNative.track(eventOptions);
 ```

You can also create request context from HttpServletRequest:

```java
@RequestMapping("/track")
public String track(HttpServletRequest request, HttpServletResponse response) {
    SecureNativeContext context = SecureNative.contextBuilder()
                                              .fromHttpServletRequest(request);

    EventOptions eventOptions = EventOptionsBuilder.builder(EventTypes.LOG_IN)
            .userId("USER_ID")
            .userTraits(new UserTraits("'USER_NAME'", "'USER_EMAIL'"))
            .context(context)
            .properties(Maps.builder()
                    .put("prop1", "CUSTOM_PARAM_VALUE")
                    .put("prop2", true)
                    .put("prop3", 3)
                    .build())
            .timestamp(new Date())
            .build();
    
    secureNative.track(eventOptions);
}
```

## Verify events

**Example**

```java
@RequestMapping("/track")
public String track(HttpServletRequest request, HttpServletResponse response) {
    SecureNativeContext context = SecureNative.contextBuilder()
                                              .fromHttpServletRequest(request);

    EventOptions eventOptions = EventOptionsBuilder.builder(EventTypes.LOG_IN)
            .userId("USER_ID")
            .userTraits(new UserTraits("'USER_NAME'", "'USER_EMAIL'"))
            .context(context)
            .properties(Maps.builder()
                    .put("prop1", "CUSTOM_PARAM_VALUE")
                    .put("prop2", true)
                    .put("prop3", 3)
                    .build())
            .timestamp(new Date())
            .build();
    
    VerifyResult verifyResult = secureNative.verify(eventOptions);
    verifyResult.getRiskLevel() // Low, Medium, High
    verifyResult.score() // Risk score: 0 -1 (0 - Very Low, 1 - Very High)
    verifyResult.getTriggers() // ["TOR", "New IP", "New City"]
}
```

## Webhook signature verification

Apply our filter to verify the request is from us, example in spring:

```java
@RequestMapping("/webhook")
public String webhookEndpoint(HttpServletRequest request, HttpServletResponse response) {
    SecureNative secureNative = SecureNative.getInstance();
    
    // Checks if request if verified
    Boolean isVerified = secureNative.verifyRequestPayload(request);
}
 ```