# Restrung

Restrung is an Android client library for RESTful and HTTP based web services.

# Introduction

Restrung was born out of the need to provide a clear and easy interface for Android apps @ 47 degrees to RESTful and HTTP based web services.
Contributions and constructive feedback are welcome.

## 1. Download

### 1.1. Maven Dependency

Restrung may be automatically imported in your project if you already use [Maven](http://maven.apache.org/). Just declare restrung as a maven dependency.

```xml
<dependency>
    <groupId>org.restrung</groupId>
    <artifactId>restrung</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
### 1.2. APKLib and others

You can get releases, snapshots and other binary forms in which Restrung is distributed in the [Downloads](https://github.com/47deg/restrung/downloads) page.

## 2. Usage

Whether you plan to use Restrung for simple http requests to a RESTful service or you need more control over requests, serialization, etc.
We encourage you to read this short guide to fully understand what RESTRung is and what's not.

### 2.1. Simple

The main interface to send requests and receive serialize responses is through the RestClient which default implementation you can access with the RestClientFactory
The RestClient exposes both Asynchronous and Synchronous operations for the most commons [HTTP verbs](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html).

#### 2.1.1. Async

#### 2.1.1.1 GET

```java
RestClientFactory.getClient().getAsync(new ContextAwareAPIDelegate<Target>(context, Target.class) {

    @Override
	public void onResults(Target response) {
		//handle results here in the main thread
	}

	@Override
	public void onError(Throwable e) {
		//handle error here in the main thread
	}

}, "http://url/%s/%s", "param1", "param2");
```

#### 2.1.1.2 POST

*Simple POST*

```java
//An object that implements JSONSerializable
AnyBeanObject sourceObject = new AnyBeanObject();
sourceObject.setName("name");

RestClientFactory.getClient().postAsync(new ContextAwareAPIDelegate<Target>(context, Target.class) {

    @Override
	public void onResults(Target response) {
		//handle results here in the main thread
	}

	@Override
	public void onError(Throwable e) {
		//handle error here in the main thread
	}

}, "http://url/%s/%s", sourceObject , "param1", "param2");
```

*Multipart POST*

```java
File file = ....;

//An object that implements JSONSerializable
AnyBeanObject sourceObject = new AnyBeanObject();
sourceObject.setName("name");

RestClientFactory.getClient().postAsync(new ContextAwareAPIDelegate<Target>(context, Target.class) {

    @Override
	public void onResults(Target response) {
		//handle results here in the main thread
	}

	@Override
	public void onError(Throwable e) {
		//handle error here in the main thread
	}

}, "http://url/%s/%s", sourceObject, file , "param1", "param2");
```

#### 2.1.1.3 PUT

```java
//An object that implements JSONSerializable
AnyBeanObject sourceObject = new AnyBeanObject();
sourceObject.setName("name");

RestClientFactory.getClient().putAsync(new ContextAwareAPIDelegate<Target>(context, Target.class) {

    @Override
	public void onResults(Target response) {
		//handle results here in the main thread
	}

	@Override
	public void onError(Throwable e) {
		//handle error here in the main thread
	}

}, "http://url/%s/%s", sourceObject , "param1", "param2");
```

#### 2.1.1.4 DELETE

```java
RestClientFactory.getClient().deleteAsync(new ContextAwareAPIDelegate<Target>(context, Target.class) {

    @Override
	public void onResults(Target response) {
		//handle results here in the main thread
	}

	@Override
	public void onError(Throwable e) {
		//handle error here in the main thread
	}

}, "http://url/%s/%s", "param1", "param2");
```

### 2.2. Advanced

If you don't wish to use the RestClientFactory and RestClient interfaces you can get finer control by directly utilizing any of the
loaders, asynctasks or runnable classes for each one of the operations.

#### 2.2.1. Loaders

##### 2.2.1.1 GET

[org.restrung.rest.async.loaders.APIGetLoader](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/async/loaders/APIGetLoader.java)

##### 2.2.1.2 POST

[org.restrung.rest.async.loaders.APIPostLoader](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/async/loaders/APIPostLoader.java)

##### 2.2.1.3 PUT

[org.restrung.rest.async.loaders.APIPutLoader](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/async/loaders/APIPutLoader.java)

##### 2.2.1.4 DELETE

[org.restrung.rest.async.loaders.APIDeleteLoader](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/async/loaders/APIDeleteLoader.java)

#### 2.2.2. AsyncTasks

##### 2.2.2.1 GET

[org.restrung.rest.async.asynctasks.APIGetAsyncTask](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/async/asynctasks/APIGetAsyncTask.java)

##### 2.2.2.2 POST

[org.restrung.rest.async.asynctasks.APIPostAsyncTask](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/async/asynctasks/APIPostAsyncTask.java)

##### 2.2.2.3 PUT

[org.restrung.rest.async.asynctasks.APIPutAsyncTask](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/async/asynctasks/APIPutAsyncTask.java)

##### 2.2.2.4 DELETE

[org.restrung.rest.async.asynctasks.APIDeleteAsyncTask](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/async/asynctasks/APIDeleteAsyncTask.java)

#### 2.2.3. Runnables

##### 2.2.2.1 GET

[org.restrung.rest.async.runnables.GetRunnable](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/async/runnables/GetRunnable.java)

##### 2.2.2.2 POST

[org.restrung.rest.async.runnables.PostRunnable](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/async/runnables/PostRunnable.java)

##### 2.2.2.3 PUT

[org.restrung.rest.async.runnables.PutRunnable](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/async/runnables/PutRunnable.java)

##### 2.2.2.4 DELETE

[org.restrung.rest.async.runnables.DeleteRunnable](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/async/runnables/DeleteRunnable.java)

#### 2.2.4. Cache

Restrung includes a cache mechanism to help fast retrieval of GET request responses.
Restrung uses the request parameters and components to create a unique ID used as cache key.

e.g.

*Load always from cache*

```java
RestClientFactory.getClient().getAsync(
    new ContextAwareAPIDelegate<Target>(context, Target.class, RequestCache.LoadPolicy.ENABLED) {

...

}, "http://url/%s/%s", "param1", "param2");
```

*Load always from cache if there is no internet connection*

```java
RestClientFactory.getClient().getAsync(
    new ContextAwareAPIDelegate<Target>(context, Target.class, RequestCache.LoadPolicy.LOAD_IF_OFFLINE) {

...

}, "http://url/%s/%s", "param1", "param2");
```

#### 2.2.4.1. Load Policies

The cache load policies available are:

* *NEVER* - Never use the cache.
* *LOAD_IF_OFFLINE* - Load from the cache when the app is offline.
* *LOAD_ON_ERROR* - Load from cache if there is an error.
* *ETAG* - Load from the cache if we have data stored and the server returns a 304 (not modified) response.
* *ENABLED* - Load always from the cache if available.
* *LOAD_IF_TIMEOUT* - Load from the cache when the request times out.
* *NETWORK_ENABLED* - Load from the cache then refresh the cache with a network call (calls onResult in the APIDelegate twice)

#### 2.2.4.2. Direct access

Access objects in the cache, invalidate, put and perform many other operations directly via the static methods at
[org.restrung.rest.cache.RequestCache](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/cache/RequestCache.java) class.

#### 2.2.5. Serialization

Restrung comes with abstract classes that implement most of the tedious work related to serialize/deserialize [javabeans](http://en.wikipedia.org/wiki/JavaBeans) from and to [JSON](http://en.wikipedia.org/wiki/JSON).
To have your beans autoserialized when being sent as a request body in both POST and PUT request make your class extend from
[org.restrung.rest.marshalling.request.AbstractJSONRequest](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/marshalling/request/AbstractJSONRequest.java) or provide your
own implementation of [org.restrung.rest.marshalling.request.JSONSerializable](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/marshalling/request/JSONSerializable.java)

To have your beans autoserialized when receiving a response body in make your class extend from
[org.restrung.rest.marshalling.response.AbstractJSONResponse](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/marshalling/response/AbstractJSONResponse.java) or provide your
own implementation of [org.restrung.rest.marshalling.response.JSONResponse](https://github.com/47deg/restrung/blob/master/src/main/java/org/restrung/rest/marshalling/response/JSONResponse.java)

#### 2.2.6. Interceptors

You can customize the request before being sent by overriding **onRequest(RequestOperation)** in your APIDelegate.
Same applies for the response before being serialized. Simply override **onResponse(ResponseOperation)** in your APIDelegate.

e.g.

```java
RestClientFactory.getClient().getAsync(new ContextAwareAPIDelegate<Target>(context, Target.class) {

    ...

    public void onRequest(RequestOperation operation) {
        //add request headers, setup basic auth, ...
    }

    public void onResponse(ResponseOperation operation) {
        // intercept status code, ...
    }

}, "http://url/%s/%s", "param1", "param2");
```



