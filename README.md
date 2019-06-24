
# Java SDK for SecureNative


**[SecureNative](https://www.securenative.com/) SecureNative is rethinking-security-as-a-service, disrupting the cyber security space and the way enterprises consume and implement security solutions.**

# Quickstart

When using Maven, add the following dependency to your `pom.xml` file:
```xml
        <dependency>
            <groupId>com.securenative.java</groupId>
            <artifactId>com.securenative.java</artifactId>
            <version>0.1.1</version>
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
instance.

**Example**

```java
   @RequestMapping("/track")
    public String track( HttpServletRequest request, HttpServletResponse response) {
        try {
            secureNative = new SecureNative(API_KEY,new SecureNativeOptions());
        } catch (SecureNativeSDKException e) {
            e.printStackTrace();
            return "Api key is not valid";
        }
        try {
            secureNative.track(new EventOptions(ip,remoteIP,userAgent,device,user,cookie,loginEvent, Collections.singletonMap("param", "paramValue")),request);
        } catch (SecureNativeSDKException e) {
            e.printStackTrace();
            return "You can only specify maximum of 6 params";
        }
        return "tracked";
    }

```
## Verification events

**Example**

```java
     @RequestMapping("/verify")
        public String verify( HttpServletRequest request, HttpServletResponse response) {
            try {
                secureNative = new SecureNative(API_KEY,new SecureNativeOptions());
            } catch (SecureNativeSDKException e) {
                e.printStackTrace();
                return "Api key is not valid";
            }
            secureNative.verify(new EventOptions(ip,remoteIP,userAgent,device,user,cookie,passwordResetType, Collections.singletonMap("param", "paramValue")),request);
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
            secureNative.flow(flowId,new EventOptions(ip,remoteIP,userAgent,device,user,cookie, logoutEvent, Collections.singletonMap("param", "paramValue")),request);
            return "flow";
        }
```