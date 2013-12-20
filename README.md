# Appsly Android REST

Android client library for [RESTful](http://en.wikipedia.org/wiki/Representational_state_transfer) services

# Introduction

Appsly Android REST is a client library for RESTful services with an emphasis in simplicity and performance

# Usage

Appsly Android REST allows you to declare your operations as annotated Java methods on a Java interface.
For example if we were implementing the Open Weather Forecast API to get the weather forecast for our office in Ballard, Seattle.

http://api.openweathermap.org/data/2.5/weather?lat=47.663267&amp;lon=-122.384187

## Service Definition

```java

@RestService
public interface OpenWeatherAPI {

    @GET("/weather")
    void getForecast(@QueryParam("lat") double latitude, @QueryParam("lon") double longitude, Callback<ForecastResponse> callback);

}

```

## Response Objects

```java

public class ForecastResponse {

    private String name;

    ... getters and setters ...

}

```

## Runtime

```java

RestClient client = RestClientFactory.defaultClient(context);
OpenWeatherAPI api = RestServiceFactory.getService(baseUrl, OpenWeatherAPI.class, client);
// hold on to the api object for the lifecycle of your app or activity context

```

```java
// somewhere else in your code

api.getForecast(47.663267, -122.384187, new Callback<WeatherResponse>() {

    @Override
    public void onResponse(Response<WeatherResponse> response) {
        // This will be invoke in the UI thread after serialization with your objects ready to use
    }

});

```

# Advanced Usage

## Cache

Appsly Android REST includes a cache mechanism to help with fast retrieval of GET responses.
Appsly Android REST uses the request parameters and components to create a unique ID used as the cache key.

To mark a method as @Cached you can simply annotate it providing the type of cache and time to live for the cached serialized response.

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

# Download

## Gradle or Maven Dependency

Appsly Android REST may be automatically imported into your project if you already use [Maven](http://maven.apache.org/) or the [Android Gradle Build System](http://tools.android.com/tech-docs/new-build-system/user-guide). Just declare Appsly Android REST as a maven dependency.
If you wish to always use the latest unstable snapshots, add the Sonatype repository where the Appsly Android REST snapshot artifacts are being deployed.
Appsly Android REST official releases will be made available at Clinker.

Maven

```xml
<repository>
    <id>public-releases</id>
    <url>http://clinker.47deg.com/nexus/content/repositories/releases</url>
    <releases>
        <enabled>true</enabled>
        <updatePolicy>daily</updatePolicy>
        <checksumPolicy>fail</checksumPolicy>
    </releases>
</repository>

<dependency>
    <groupId>ly.apps</groupId>
    <artifactId>android-rest</artifactId>
    <version>1.2</version>
</dependency>
```

Snapshots

```xml
<repository>
    <id>sonatype</id>
    <url>https://oss.sonatype.org/content/groups/public/</url>
    <releases>
        <enabled>true</enabled>
        <updatePolicy>daily</updatePolicy>
        <checksumPolicy>fail</checksumPolicy>
    </releases>
    <snapshots>
        <enabled>true</enabled>
        <updatePolicy>always</updatePolicy>
        <checksumPolicy>ignore</checksumPolicy>
    </snapshots>
</repository>

<dependency>
    <groupId>ly.apps</groupId>
    <artifactId>android-rest</artifactId>
    <version>1.2-SNAPSHOT</version>
</dependency>
```

# Credits

Appsly Android REST is a library maintained by the [47 Degrees] team and part of the OS Framework [apps.ly Reaktor](http://reaktor.apps.ly)
Appsly Android REST is inspired by other libs and projects such as [Android Async Http Client](), [Jackson](Jackson), [Retrofit](), [LRUDiskCache](), etc.

# License

Copyright (C) 2013 47 Degrees, LLC
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



