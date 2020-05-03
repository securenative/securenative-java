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

Once the SDK has been initialized, tracking requests are sent through the SDK
instance. Make sure you build event with the EventBuilder:

 ```java
Event event = new SnEvent.EventBuilder(EventTypes.LOG_IN.getType()).
                            withUser(new User("","","apple@sucks.com")).
                            withIp("35.199.23.1").
                            withCookieValue("eyJjaWQiOiJkYzgyYjdhZS00ODFkLTQyODItYTMyZC0xZTU1Njk2ZjNmZTQiLCJmcCI6Ijk5NGYzZjVjZTRiYWUwODQzMTRhOTFkNzgyN2I1MWYuMjQ3MDBmOWYxOTg2ODAwYWI0ZmNjODgwNTMwZGQwZWQifQ").
                            withRemoteIP("35.199.23.1").
                            withUserAgent("Mozilla/5.0 (Linux; U; Android 4.4.2; zh-cn; GT-I9500 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 MQQBrowser/5.0 QQ-URL-Manager Mobile Safari/537.36").
                            build();
 ```

**Example**

```java
   @RequestMapping("/track")
    public String track( HttpServletRequest request, HttpServletResponse response) {
        try {
            secureNative = new SecureNative(API_KEY,new SecureNativeOptions());
            Event event = new SnEvent.EventBuilder(EventTypes.LOG_IN.getType()).
                                        withUser(new User("","","chuck@norris.com")).
                                        withIp("35.199.23.1").
                                        withCookieValue("eyJjaWQiOiJkYzgyYjdhZS00ODFkLTQyODItYTMyZC0xZTU1Njk2ZjNmZTQiLCJmcCI6Ijk5NGYzZjVjZTRiYWUwODQzMTRhOTFkNzgyN2I1MWYuMjQ3MDBmOWYxOTg2ODAwYWI0ZmNjODgwNTMwZGQwZWQifQ").
                                        withRemoteIP("35.199.23.1").
                                        withUserAgent("Mozilla/5.0 (Linux; U; Android 4.4.2; zh-cn; GT-I9500 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 MQQBrowser/5.0 QQ-URL-Manager Mobile Safari/537.36").
                                        build();
            secureNative.track(event);
            
            
        } catch (SecureNativeSDKException e) {
            e.printStackTrace();
            return "Api key is not valid";
        }
        return "tracked";
    }

```

You can build an event from HttpServletRequest or from combination between event and HttpServletRequest:


```java
   @RequestMapping("/track")
    public String track( HttpServletRequest request, HttpServletResponse response) {
        try {
            secureNative = new SecureNative(API_KEY,new SecureNativeOptions());
            Event e = new SnEvent.EventBuilder(EventTypes.LOG_IN.getType()).
                                                    withUser(new User("","","chuck@norris.com")).
                                                    build();
            Event event = secureNative.buildEventFromHttpServletRequest(request, e);
            secureNative.track(event);
            
            
        } catch (SecureNativeSDKException e) {
            e.printStackTrace();
            return "Api key is not valid";
        }
        return "tracked";
    }

```





## Verification events

**Example**

```java
     @RequestMapping("/verify")
        public String verify(HttpServletRequest request, HttpServletResponse response) {
            try {
                secureNative = new SecureNative(API_KEY,new SecureNativeOptions());
            } catch (SecureNativeSDKException e) {
                e.printStackTrace();
                return "Api key is not valid";
            }
            secureNative.verify(new SnEvent.EventBuilder(EventTypes.LOG_IN.getType()).withUser(new User("1","Dan","Dan@Dan.dan")).withIp(ip).withRemoteIP(remoteIP).withUserAgent(userAgent).build());
);
            return "verify";
        }

```
## Flow events

**Example**

```java
       @RequestMapping("/flow")
          public String flow( HttpServletRequest request, HttpServletResponse response) {
              try {
                  secureNative = new SecureNative(API_KEY,new SecureNativeOptions());
              } catch (SecureNativeSDKException e) {
                  e.printStackTrace();
                  return "Api key is not valid";
              }
              secureNative.flow(1,new SnEvent.EventBuilder(EventTypes.LOG_IN.getType()).withUser(new User("1","Dan","Dan@Dan.dan")).withIp(ip).withRemoteIP(remoteIP).withUserAgent(userAgent).build());
              return "flow";
          }
```

## Webhook entry filter

Apply our filter to verify the request is from us, example in spring:

```java

 @Bean
    public FilterRegistrationBean<VerifyWebHookMiddleware> filterWebhook() throws SecureNativeSDKException {
        FilterRegistrationBean <VerifyWebHookMiddleware> registrationBean = new FilterRegistrationBean();
        VerifyWebHookMiddleware customURLFilter = new VerifyWebHookMiddleware("API KEY");
        registrationBean.setFilter(customURLFilter);
        return registrationBean;
    }
 ```

[Spring](https://github.com/securenative/securenative-java/tree/master/spring) or any web application that uses javax.servlet

[akka-http](https://github.com/securenative/securenative-java/tree/master/akka-http)