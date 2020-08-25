<p align="center">
  <a href="https://www.securenative.com"><img src="https://user-images.githubusercontent.com/45174009/77826512-f023ed80-7120-11ea-80e0-58aacde0a84e.png" alt="SecureNative Logo"/></a>
</p>

<p align="center">
  <b>A Cloud-Native Security Monitoring and Protection for Modern Applications</b>
</p>
<p align="center">
  <a href="https://github.com/securenative/securenative-node">
    <img alt="Github Actions" src="https://github.com/securenative/securenative-java/workflows/CI/badge.svg">
  </a>
  <a href="https://codecov.io/gh/securenative/securenative-java">
    <img src="https://codecov.io/gh/securenative/securenative-java/branch/master/graph/badge.svg" />
  </a>
  <a href="https://search.maven.org/artifact/com.securenative.java/securenative-java">
    <img src="https://img.shields.io/maven-central/v/com.securenative.java/securenative-java.svg" alt="npm version" height="20">
  </a>
</p>
<p align="center">
  <a href="https://docs.securenative.com">Documentation</a> |
  <a href="https://docs.securenative.com/quick-start">Quick Start</a> |
  <a href="https://blog.securenative.com">Blog</a> |
  <a href="">Chat with us on Slack!</a>
</p>
<hr/>


[SecureNative](https://www.securenative.com/) performs user monitoring by analyzing user interactions with your application and various factors such as network, devices, locations and access patterns to stop and prevent account takeover attacks.

## Install the SDK

When using Maven, add the following dependency to your `pom.xml` file:
```xml
<dependency>
    <groupId>com.securenative.java</groupId>
    <artifactId>securenative-java</artifactId>
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
// Options 1: Use default config file path
SecureNative securenative =  SecureNative.init();

// Options 2: Use specific config file path
Path path = Paths.get("/path/to/securenative.properties");
SecureNative securenative =  SecureNative.init(path);
```
### Option 2: Initialize via API Key

```java
SecureNative securenative =  SecureNative.init("YOUR_API_KEY");
```

### Option 3: Initialize via ConfigurationBuilder
```java
SecureNative securenative = SecureNative.init(SecureNative.configBuilder()
                                        .withApiKey("API_KEY")
                                        .withMaxEvents(10)
                                        .withLogLevel("error")
                                        .build()); 
```

## Getting SecureNative instance
Once initialized, sdk will create a singleton instance which you can get: 
```java
SecureNative securenative = SecureNative.getInstance();
```

## Tracking events

Once the SDK has been initialized, tracking requests sent through the SDK
instance. Make sure you build event with the EventBuilder:

 ```java
SecureNative securenative = SecureNative.getInstance();

SecureNativeContext context = SecureNative.contextBuilder()
        .withIp("127.0.0.1")
        .withClientToken("SECURED_CLIENT_TOKEN")
        .withHeaders(Maps.defaultBuilder()
                    .put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405")
                    .build())
        .build();

EventOptions eventOptions = EventOptionsBuilder.builder(EventTypes.LOG_IN)
        .userId("USER_ID")
        .userTraits("USER_NAME", "USER_EMAIL", "+01234566789")
        .context(context)
        .properties(Maps.builder()
                .put("prop1", "CUSTOM_PARAM_VALUE")
                .put("prop2", true)
                .put("prop3", 3)
                .build())
        .timestamp(new Date())
        .build();

securenative.track(eventOptions);
 ```

You can also create request context from HttpServletRequest:

```java
@RequestMapping("/track")
public void track(HttpServletRequest request, HttpServletResponse response) {
    SecureNativeContext context = SecureNative.contextBuilder()
                                              .fromHttpServletRequest(request)
                                              .build();

    EventOptions eventOptions = EventOptionsBuilder.builder(EventTypes.LOG_IN)
            .userId("USER_ID")
            .userTraits("USER_NAME", "USER_EMAIL", "+01234566789")
            .context(context)
            .properties(Maps.builder()
                    .put("prop1", "CUSTOM_PARAM_VALUE")
                    .put("prop2", true)
                    .put("prop3", 3)
                    .build())
            .timestamp(new Date())
            .build();
    
    securenative.track(eventOptions);
}
```

## Verify events

**Example**

```java
@RequestMapping("/verify")
public void verify(HttpServletRequest request, HttpServletResponse response) {
    SecureNativeContext context = SecureNative.contextBuilder()
                                              .fromHttpServletRequest(request)
                                              .build();

    EventOptions eventOptions = EventOptionsBuilder.builder(EventTypes.LOG_IN)
            .userId("USER_ID")
            .userTraits("USER_NAME", "USER_EMAIL", "+01234566789")
            .context(context)
            .properties(Maps.builder()
                    .put("prop1", "CUSTOM_PARAM_VALUE")
                    .put("prop2", true)
                    .put("prop3", 3)
                    .build())
            .timestamp(new Date())
            .build();
    
    VerifyResult verifyResult = securenative.verify(eventOptions);
    verifyResult.getRiskLevel(); // Low, Medium, High
    verifyResult.score(); // Risk score: 0 -1 (0 - Very Low, 1 - Very High)
    verifyResult.getTriggers(); // ["TOR", "New IP", "New City"]
}
```

## Webhook signature verification

Apply our filter to verify the request is from us, example in spring:

```java
@RequestMapping("/webhook")
public void webhookEndpoint(HttpServletRequest request, HttpServletResponse response) {
    SecureNative securenative = SecureNative.getInstance();
    
    // Checks if request is verified
    Boolean isVerified = securenative.verifyRequestPayload(request);
}
 ```
