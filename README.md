[![Build Status](https://clinker.47deg.com/desktop/plugin/public/status/appsly-android-rest.png?branch=master)](https://clinker.47deg.com/jenkins/view/Appsly/job/appsly-android-rest/)

THIS PROJECT IS DISCONTINUED — USE AT YOUR OWN RISK

It has been a fun and great project but it's time for us to move on. Check out our recent work that we are doing with Scala on Android. http://47deg.github.io/translate-bubble-android/ and follow us on Github and Twitter for new and exciting open source projects. Thanks for your continuing support. If you wish to take on maintenance of this library please contact us through the issue tracker.

# Appsly Android REST

Async Android client library for [RESTful](http://en.wikipedia.org/wiki/Representational_state_transfer) services.

# Introduction

Appsly Android REST (AKA RESTrung) is a annotation based client library to connect to RESTful services on Android with an emphasis in simplicity and performance that automatically handles the
implementation for most common REST use cases. Easily handle GET, POST, PUT and DELETE requests in an elegant way.

# Getting Started

Appsly Android REST allows you to declare your operations as annotated Java methods on a Java interface that you don't have to implement yourself.
The interface is used to generate a automatically a proxy instance that does the actual work and taking care of all the implementation details.

Let's say we were implementing the Open Weather Forecast API to get the weather forecast for the [47 Degrees](http://47deg.com) office in Ballard, Seattle.
We would need to fetch json from this http endpoint and convert it's response to our Java model.
This is how we'd do it.

http://api.openweathermap.org/data/2.5/weather?lat=47.663267&lon=-122.384187

```java

@RestService
public interface OpenWeatherAPI {

    @GET("/weather")
    void getForecast(@QueryParam("lat") double latitude, @QueryParam("lon") double longitude, Callback<ForecastResponse> callback);

}

```

```java

public class ForecastResponse {

    private String name;

    ... getters and setters ...

}

```

```java

RestClient client = RestClientFactory.defaultClient(context);
OpenWeatherAPI api = RestServiceFactory.getService(baseUrl, OpenWeatherAPI.class, client);
// hold on to the api object for the lifecycle of your app or activity context

```

```java
// somewhere else in your code

api.getForecast(47.663267, -122.384187, new Callback<ForecastResponse>() {

    @Override
    public void onResponse(Response<ForecastResponse> response) {
        // This will be invoke in the UI thread after serialization with your objects ready to use
    }

});

```

# Download

## Gradle  Dependency

Appsly Android REST may be automatically imported into your project if you already use the [Android Gradle Build System](http://tools.android.com/tech-docs/new-build-system/user-guide). Just declare Appsly Android REST as a gradle dependency.
If you wish to always use the latest unstable snapshots, add the Clinker Snapshots repository where the Appsly Android REST snapshot artifacts are being deployed.
Appsly Android REST official releases will be made available at Clinker and Maven Central as they become available


**SNAPSHOTS**


*Gradle*

```groovy

maven { url 'http://clinker.47deg.com/nexus/content/groups/public' }

dependencies {
    compile ('ly.apps:android-rest:1.2.1-SNAPSHOT@aar') {
        transitive = true
        changing = true
    }
}
```

# Configuration

## Annotations

### @RestService

Indicates an interface is to be proxied and its methods mapped to rest operations

```java
@RestService
public interface MyAwesomeService {
...
}
```

### @GET

Indicates a method is mapped to an HTTP GET request

```java
@GET("/resource/{id}")
void fetch(@Path("id") String id);
```

### @POST

Indicates a method is mapped to an HTTP POST request

```java
@POST("/resource")
void add(@Body MyRequest request);
```

### @PUT

Indicates a method is mapped to an HTTP PUT request

```java
@PUT("/resource")
void fetch(@Body MyRequest request);
```

### @DELETE

Indicates a method is mapped to an HTTP DELETE request

```java
@DELETE("/resource/{id}")
void remove(@Path("id") String id);
```

### @Path

Indicates a method arg is a path parameter in the url

```java
@GET("/resource/{id}")
void fetc(@Path("id") String id);
```

### @QueryParam

Indicates a method arg is a query parameter in the url query string

```java
@GET("/resource/list")
void list(@QueryParam("limit") int limit);
```

### @QueryParams

Indicates a method arg is a bean including properties that will be mapped to query parameters in the url query string

```java
@GET("/resource/list")
void list(@QueryParams PaginationInfo paginationInfo);
```

### @FormField

Indicates a method arg is mapped to a post parameter

```java
@POST("/account")
void update(@FormField String username);
```

### @FormData

Indicates a method arg is a bean including properties that will be mapped to post parameters

```java
@POST("/account/settings")
void updateSettings(@FormData AccountSettings settings);
```

### @Header

Indicates a method arg should be sent as a request header

```java
@GET("/resource/list")
void list(@Header("X-AuthToken") String token);
```

### @Cached

Indicates a method is mapped to a REST operation whose response may be cached.
For more details on caching see [Cache](#Cache)

```java
@Cached
@GET("/resource/list")
void list();
```

## Cache

Appsly Android REST is built with performance and flexibility in mind.
It includes a cache mechanism to help with fast retrieval, caching and reusing of serialized responses.

To mark a method as @Cached you can simply annotate it providing the optional type of cache and time to live for the cached serialized response.

In the following example the Forecast information will be loaded from the cache not hitting the server if requested within 10 minutes of the first time it was requested.
After the 10 minutes pass the cache will be invalidated and reloaded. This means your users will not have to wait for your Activity to load and connect to the internet each
time a user visits the weather forecast for that location. Think battery saving on your application and a better end user experience.

```java

@GET("/weather")
@Cached(policy = CachePolicy.ENABLED, timeToLive = 10 * 60 * 1000)
void getForecast(@QueryParam("lat") double latitude, @QueryParam("lon") double longitude, Callback<ForecastResponse> callback);

```

### Policies

The cache policies available are:

* NEVER - Never use the cache.
* LOAD_IF_OFFLINE - Load from the cache when the app is offline.
* LOAD_ON_ERROR - Load from cache if there is an error.
* ENABLED - Load always from the cache if available.
* LOAD_IF_TIMEOUT - Load from the cache when the request times out.
* NETWORK_ENABLED - Load from the cache then refresh the cache with a network call (may call onResponse in the callback twice)

The NETWORK_ENABLED policy is particularly useful for use cases where you may display stale data on a Activity or Fragment but immediately refresh it
if the content has changed since it was last requested.

## Serialization

Appsly Android REST ships with the popular library [Jackson]() for serialization and deserialization by default but provides
overridable service definitions to allow for customization or plugin in different implementations

### Path &amp; Query Params Converter

The query and path param converter serializes method args into the final url and query string used to perform the
request.

To provide a custom implementation override [ly.apps.android.rest.converters.QueryParamsConverter]() and construct the RestClient
by passing it as argument.

```java
QueryParamsConverter customQueryParamsConverter = new MyCustomQueryParamsConverter();
RestClient client = new DefaultRestClientImpl(httpClient, customQueryParamsConverter, bodyConverter);
```

### Request and Response Body Converters

The Body Converter serializes and deserializes Beans and Objects into JSON, Post params, etc. both for Request and Response bodies

To provide a custom implementation override [ly.apps.android.rest.converters.BodyConverter]() and construct the RestClient
by passing it as argument to the DelegatingConverterService which is also the default body converter.
The DelegatingConverterService evaluates all added converter in orders looking for candidates to handle the body.

```java
DelegatingConverterService bodyConverter = new DelegatingConverterService(){{
    addConverter(new MyCustomBodyConverter());
    addConverter(new JacksonBodyConverter());
    addConverter(new JacksonHttpFormValuesConverter());
}};
RestClient client = new DefaultRestClientImpl(httpClient, queryParamsConverter, bodyConverter);
```

## Async http client

Appsly Android REST uses the popular [AsyncHttpClient]() under the hood.

You may also provide customization to the http client such as providing default headers, enabling a lower level http response cache, etc.

E.g. To customize the http client configuration you may simply pass your customized instance to the RestClient.
In the case below, the default in the lib, the lower level http client [http response](http://developer.android.com/reference/org/apache/http/HttpResponse.html) cache is enabled.
Also the serialized entity cache is enabled as well and the Persistent Cache Manager based on [LRUDiskCache]

```java
RestClient client = new DefaultRestClientImpl(
    new CacheAwareHttpClient(new ContextPersistentCacheManager(context)) {{
        enableHttpResponseCache(10 * 1024 * 1024, new File(context.getCacheDir(), "android-rest-http"));
    }},
    new JacksonQueryParamsConverter(),
    new DelegatingConverterService(){{
        addConverter(new JacksonBodyConverter());
        addConverter(new JacksonHttpFormValuesConverter());
    }}
);
```

## Request &amp; Response Lifecycle

Callback are instances of the [ResponseHandlerInterface]() and as such they allow for overriding the response lifecycle methods
to get progress notifications, intercepting serialization at a lower level, manipulating headers, results and failures.


# Credits

Appsly Android REST is a library maintained by the [47 Degrees] team and part of the OS Framework [apps.ly Reaktor](http://reaktor.apps.ly)
Appsly Android REST is inspired by other libs and projects such as [Android Async Http Client](), [Jackson](Jackson), [Retrofit](), [LRUDiskCache](), etc.

# Continuous Integration

CI and Artifact Repository hosted in ClinkerHQ.com 

[![ClinkerHQ][1]][2]

# License

Copyright (C) 2014 47 Degrees, LLC
http://47deg.com
hello@47deg.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[1]: http://dl.clinkerhq.com/assets/badge/clinker-badge_125x125.png
[2]: http://clinkerhq.com
[4]: https://clinker.47deg.com/jenkins/view/Appsly/job/appsly-android-rest/



