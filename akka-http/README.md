# Akka Http for Secure Native
This module adds Akka Http (scala) directives to Secure Native SDK in order to easily integrate with Akka Http applications.

## Quickstart
Download the latest version of the SDK to your favourite scala build system:

#### Maven
```
<dependency>
    <groupId>com.securenative.java</groupId>
    <artifactId>akka-http</artifactId>
    <version>LATEST</version>
</dependency>
```

#### Gradle
`compile group: 'com.securenative.java', name: 'akka-http', version: 'LATEST'`

#### SBT
`libraryDependencies += "com.securenative.java" % "akka-http" % "LATEST"`

### Initialize the SDK
Create an instance (singleton) of the SecureNative object with your Api Key:
```scala
import com.securenative.snlogic.SecureNative
import com.securenative.models.SecureNativeOptions

object Main {
  val apiKey = "YOUR_API_KEY"
  implicit val snSdk: SecureNative = new SecureNative(apiKey, new SecureNativeOptions())
}
```

### Tracking Events
Secure Native let you send Async event that won't impact your regular flows, you may use our built in types (such as login, logout, etc...)
or using your own custom events. Secure Native needs these events in order to learn the user behaviour. 
```scala
object Main {
  val apiKey = "YOUR_API_KEY"
  implicit val snSdk: SecureNative = new SecureNative(apiKey, new SecureNativeOptions())

  def main(args: Array[String]): Unit = {   

    def route = path("login") {
      post {  
          // Add Secure Native event directive to automatically build your event from
          // the http request:      
          SnDirectives.extractSnEvent(EventTypes.LOG_IN)(snSdk) { builder =>
          
            /* YOUR LOGIN BUSINESS LOGIC */
            
            val event = builder // You may change or add your custom fields to the builder
              .withUser(new User("jas723h2", "Jack Jefferson", "jackje@gmail.com"))
              .build() // Finally call the build method to create the event
            
            // Track the event using the SDK:              
            snSdk.track(event)
            complete("SOMETHING")                                        
          }                        
      }
    }
  }
}
```


### Guarding Sensitive Operations
In order to guard sensitive resources you can use the `verify` method, which will return the risk score of the current user. 
Allowing you to take a decision on how to act, for example when trying to delete project from github we can use the verify method before actually deleting the project
when getting the risk score you can decide if you want to allow/block the deletion.

```scala
object Main {
  val apiKey = "YOUR_API_KEY"
  implicit val snSdk: SecureNative = new SecureNative(apiKey, new SecureNativeOptions())

  def main(args: Array[String]): Unit = {   

    def route = path("project") {
      delete {                
          SnDirectives.extractSnEvent(EventTypes.LOG_IN)(snSdk) { builder =>                              
            val event = builder
              .withUser(new User("jas723h2", "Jack Jefferson", "jackje@gmail.com"))
              .build() // Finally call the build method to create the event
            
            val riskScore = snSdk.verify(event)            
            
            if (riskScore.riskLevel == "high") {
              /* Your blocking logic */
            } else {
              /* Your allowing logic */
            }s                                               
            complete("SOMETHING")                                        
          }                        
      }
    }
  }
}
```

### Accepting Webhooks from Secure Native
Secure Native can respond on real time to changes in risk score based on your sent events to get those real-time notification
you can create a new endpoint so we can call it when something happens.
In order to verify that the incoming request originated from Secure Native servers we have another directive that called `verifyWebhook`.
```scala
object Main {  
  val apiKey = "YOUR_API_KEY" 
  def main(args: Array[String]): Unit = {   

    def route = path("riskscore") {
      post { // it has to be a post endpoint                                                   
        SnDirectives.verifyWebhook(apiKey) { body =>
          println(body) // the body will be passed only for Secure Native requests
          complete(body)
        }                        
      }
    }
  }
}
```  