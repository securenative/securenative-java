
# Java SDK for SecureNative


**[SecureNative](https://www.securenative.com/) SecureNative is rethinking-security-as-a-service, disrupting the cyber security space and the way enterprises consume and implement security solutions.**

# Quickstart

When using Maven, add the following dependency to your `pom.xml` file:
```xml
        <dependency>
            <groupId>com.securenative.java</groupId>
            <artifactId>com.securenative.java</artifactId>
            <version>0.1.2</version>
        </dependency>
```

## Initialize the SDK

Go to the settings page of your SecureNative account and find your **API KEY**

**Initialize using API KEY**

```java
 secureNative = new SecureNative(API_KEY,new SecureNativeOptions());
```

You can pass empty SecureNativeOptions object or you can set the following:

   api url - target url the events will be sent (https://api.securenative.com/collector/api/v1).
   interval - minimum interval between sending events (1000ms).
   max events - maximum events that will be sent (1000).
   timeout - (1500 ms).

    ```java
     secureNative = new SecureNative(API_KEY,new SecureNativeOptions(
            "https://other.domain.com/collector/api/v1",
            1200,
            5000,
            2000     
      ));
    ```

## Tracking events

Once the SDK has been initialized, tracking requests are sent through the SDK
instance. Make sure you build event with the EventBuilder:

Event event = new SnEvent.EventBuilder(EventTypes.LOG_IN.getType()).
                            withUser(new User("","","apple@sucks.com")).
                            withIp("35.199.23.1").
                            withCookieValue("eyJjaWQiOiJkYzgyYjdhZS00ODFkLTQyODItYTMyZC0xZTU1Njk2ZjNmZTQiLCJmcCI6Ijk5NGYzZjVjZTRiYWUwODQzMTRhOTFkNzgyN2I1MWYuMjQ3MDBmOWYxOTg2ODAwYWI0ZmNjODgwNTMwZGQwZWQifQ").
                            withRemoteIP("35.199.23.1").
                            withUserAgent("Mozilla/5.0 (Linux; U; Android 4.4.2; zh-cn; GT-I9500 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 MQQBrowser/5.0 QQ-URL-Manager Mobile Safari/537.36").
                            build();

**Example**

```java
   @RequestMapping("/track")
    public String track( HttpServletRequest request, HttpServletResponse response) {
        try {
            secureNative = new SecureNative(API_KEY,new SecureNativeOptions());
            Event event = new SnEvent.EventBuilder(EventTypes.LOG_IN.getType()).
                                        withUser(new User("","","apple@sucks.com")).
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
                                                    withUser(new User("","","apple@sucks.com")).
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
        FilterRegistrationBean < VerifyWebHookMiddleware > registrationBean = new FilterRegistrationBean();
        VerifyWebHookMiddleware customURLFilter = new VerifyWebHookMiddleware("CD70B8F2CF32FEA5ED190C5E630BD6864F144155");
        registrationBean.setFilter(customURLFilter);
        return registrationBean;
    }
 ```