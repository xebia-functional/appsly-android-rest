/*
 * Copyright (C) 2012 47 Degrees, LLC
 * http://47deg.com
 * hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.restrung.rest.client;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.restrung.rest.async.runnables.DeleteRunnable;
import org.restrung.rest.async.runnables.GetRunnable;
import org.restrung.rest.async.runnables.PostRunnable;
import org.restrung.rest.async.runnables.PutRunnable;
import org.restrung.rest.cache.RequestCache;
import org.restrung.rest.exceptions.APIException;
import org.restrung.rest.exceptions.InvalidCredentialsException;
import org.restrung.rest.marshalling.request.HttpMessageRequestOperationImpl;
import org.restrung.rest.marshalling.request.JSONSerializable;
import org.restrung.rest.marshalling.response.HttpMessageResponseOperationImpl;
import org.restrung.rest.marshalling.response.JSONResponse;
import org.restrung.rest.misc.CountingMultipartEntity;
import org.restrung.rest.misc.HttpClientFactory;
import org.restrung.rest.utils.Base64;
import org.restrung.rest.utils.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

/**
 * Default thread safe impl of the RestClient that delegates calls in AsyncTask and Loader for its async operations.
 */
public class DefaultRestClientImpl implements RestClient {

    /**
     * Hack to avoid initialization exception in certain versions of Android
     */
    static {
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see RestClient#getAsync
     */
    @Override
    public <T extends JSONResponse> void getAsync(final APIDelegate<T> delegate, final String path, final Object... args) {
        new GetRunnable<T>(delegate, path, args).run();
    }

    /**
     * @see RestClient#deleteAsync
     */
    @Override
    public <T extends JSONResponse> void deleteAsync(final APIDelegate<T> delegate, final String path, final Object... args) {
        new DeleteRunnable<T>(delegate, path, args).run();
    }

    /**
     * @see RestClient#postAsync
     */
    @Override
    public <T extends JSONResponse> void postAsync(final APIDelegate<T> delegate, final String path, final JSONSerializable body, final Object... args) {
        new PostRunnable<T>(delegate, path, body, null, null, args).run();
    }

    /**
     * @see RestClient#postAsync
     */
    @Override
    public <T extends JSONResponse> void postAsync(final APIDelegate<T> delegate, final String path, final File file, final JSONSerializable body, final APIPostParams postParams, final Object... args) {
        new PostRunnable<T>(delegate, path, body, file, postParams, args).run();
    }

    /**
     * @see RestClient#putAsync
     */
    @Override
    public <T extends JSONResponse> void putAsync(final APIDelegate<T> delegate, final String path, final JSONSerializable body, final Object... args) {
        new PutRunnable<T>(delegate, path, body, args).run();
    }

    /**
     * @see RestClient#post(APIDelegate, String, org.restrung.rest.marshalling.request.JSONSerializable, int)
     */
    @Override
    public <T extends JSONResponse> T post(final APIDelegate<T> apiDelegate, final String url, final JSONSerializable body, final int timeout) throws APIException {
        return executeCacheableOperation(new Callable<T>() {
            @Override
            public T call() throws Exception {
                String result = performPost(url, null, null, body != null ? body.toJSON() : null, null, timeout, null, apiDelegate);
                return serializeResultForDelegate(result, apiDelegate);
            }
        }, apiDelegate, url, null, null, timeout);
    }

    /**
     * @see RestClient#post(APIDelegate, String, org.restrung.rest.marshalling.request.JSONSerializable, java.io.File, int, APIPostParams)
     */
    @Override
    public <T extends JSONResponse> T post(final APIDelegate<T> apiDelegate, final String url, final JSONSerializable body, final File file, final int timeout, final APIPostParams apiPostParams) throws APIException {
        return executeCacheableOperation(new Callable<T>() {
            @Override
            public T call() throws Exception {
                String result = performPost(url, null, null, body != null ? body.toJSON() : null, file, timeout, apiPostParams, apiDelegate);
                return serializeResultForDelegate(result, apiDelegate);
            }
        }, apiDelegate, url, null, null, timeout);
    }

    /**
     * @see RestClient#put(APIDelegate, String, org.restrung.rest.marshalling.request.JSONSerializable, int)
     */
    @Override
    public <T extends JSONResponse> T put(final APIDelegate<T> apiDelegate, final String url, final JSONSerializable body, final int timeout) throws APIException {
        return executeCacheableOperation(new Callable<T>() {
            @Override
            public T call() throws Exception {
                String result = performPut(url, null, null, body != null ? body.toJSON() : null, timeout, apiDelegate);
                return serializeResultForDelegate(result, apiDelegate);
            }
        }, apiDelegate, url, null, null, timeout);
    }

    /**
     * @see RestClient#get(APIDelegate, String, int)
     */
    @Override
    public <T extends JSONResponse> T get(final APIDelegate<T> apiDelegate, final String url, final int timeout) throws APIException {
        return executeCacheableOperation(new Callable<T>() {
            @Override
            public T call() throws Exception {
                String result = performGet(url, null, null, timeout, apiDelegate);
                return serializeResultForDelegate(result, apiDelegate);
            }
        }, apiDelegate, url, null, null, timeout);
    }

    /**
     * @see RestClient#delete(APIDelegate, String, int)
     */
    @Override
    public <T extends JSONResponse> T delete(final APIDelegate<T> apiDelegate, final String url, final int timeout) throws APIException {
        return executeCacheableOperation(new Callable<T>() {
            @Override
            public T call() throws Exception {
                String result = performDelete(url, null, null, timeout, apiDelegate);
                return serializeResultForDelegate(result, apiDelegate);
            }
        }, apiDelegate, url, null, null, timeout);
    }

    /**
     * Private helper that executes an operation that may be cacheable
     *
     * @param callable    the operation callable
     * @param apiDelegate the api delegate
     * @param url         the url
     * @param user        the user
     * @param password    the password
     * @param timeout     a request timeout
     * @param <T>         something that extends @see JSONResponse
     * @return the result from executing the remote operation as a @see JSONResponse
     * @throws APIException
     */
    private static <T extends JSONResponse> T executeCacheableOperation(Callable<T> callable, final APIDelegate<T> apiDelegate, final String url, final String user, final String password, final int timeout) throws APIException {
        RequestCache.CacheAwareOperation<T> operation = new RequestCache.CacheAwareOperation<T>(apiDelegate, callable, url, user, password, timeout);
        T result;
        try {
            result = operation.execute();
        } catch (Exception e) {
            if (APIException.class.isAssignableFrom(e.getClass())) {
                throw (APIException) e;
            } else {
                throw new APIException(e, APIException.UNTRACKED_ERROR);
            }
        }
        return result;
    }

    /**
     * Private helper for result serialization //todo consider moving to serialization stack
     *
     * @param result   the result as a string
     * @param delegate the api delegate
     * @return the serialized object of type <T>
     */
    private static <T extends JSONResponse> T serializeResultForDelegate(String result, APIDelegate<T> delegate) throws InstantiationException, IllegalAccessException, JSONException {
        T serializedResult = delegate.getExpectedResponseType().newInstance();
        serializedResult.fromJSON(IOUtils.stringToJSON(result));
        return serializedResult;
    }

    /**
     * Performs a GET request enforcing basic auth
     *
     * @param url      the url to be requested
     * @param user     the user
     * @param password the password
     * @param timeout  a request timeout
     * @return the response body as a String
     * @throws APIException
     */
    public static String performGet(String url, String user, String password, int timeout, APIDelegate<?> delegate) throws APIException {
        Log.d(RestClient.class.getSimpleName(), "GET: " + url);
        String responseBody;
        try {
            DefaultHttpClient httpclient = HttpClientFactory.getClient();
            HttpGet httpget = new HttpGet(url);
            setupTimeout(timeout, httpclient);
            setupCommonHeaders(httpget);
            setupBasicAuth(user, password, httpget);
            handleRequest(httpget, delegate);
            HttpResponse response = httpclient.execute(httpget);
            responseBody = handleResponse(response, delegate);
        } catch (ClientProtocolException e) {
            throw new APIException(e, APIException.UNTRACKED_ERROR);
        } catch (IOException e) {
            throw new APIException(e, APIException.UNTRACKED_ERROR);
        }
        return responseBody;
    }

    /**
     * Performs a POST request to the given url
     *
     * @param url           the url
     * @param user          an optional user for basic auth
     * @param password      an optional password for basic auth
     * @param body          an optional body to send with the request
     * @param file          an optional file that would result in a multipart request
     * @param timeout       the request timeout
     * @param apiPostParams an optional callback to get progress
     * @param delegate      the api delegate that will receive callbacks regarding progress and results
     * @return the response body as a string
     * @throws APIException
     */
    public static String performPost(String url, String user, String password, String body, final File file, int timeout, APIPostParams apiPostParams, APIDelegate<?> delegate) throws APIException {
        Log.d(RestClient.class.getSimpleName(), "POST: " + url);
        String responseBody;
        try {
            DefaultHttpClient httpclient = HttpClientFactory.getClient();
            HttpPost httppost = new HttpPost(url);
            setupTimeout(timeout, httpclient);
            setupCommonHeaders(httppost);
            setupBasicAuth(user, password, httppost);
            setupRequestBody(httppost, body, apiPostParams, file);
            handleRequest(httppost, delegate);
            HttpResponse response = httpclient.execute(httppost);
            responseBody = handleResponse(response, delegate);
        } catch (ClientProtocolException e) {
            throw new APIException(e, APIException.UNTRACKED_ERROR);
        } catch (IOException e) {
            throw new APIException(e, APIException.UNTRACKED_ERROR);
        }
        return responseBody;
    }

    /**
     * Performs a PUT request to the given url
     *
     * @param url      the url
     * @param user     an optional user for basic auth
     * @param password an optional password for basic auth
     * @param body     an optional body to send with the request
     * @param timeout  the request timeout
     * @param delegate the api delegate that will receive callbacks regarding progress and results
     * @return the response body as a string
     * @throws APIException
     */
    public static String performPut(String url, String user, String password, String body, int timeout, APIDelegate<?> delegate) throws APIException {
        Log.d(RestClient.class.getSimpleName(), "PUT: " + url);
        String responseBody;
        try {
            DefaultHttpClient httpclient = HttpClientFactory.getClient();
            HttpPut httpPut = new HttpPut(url);
            setupTimeout(timeout, httpclient);
            setupCommonHeaders(httpPut);
            setupBasicAuth(user, password, httpPut);
            setupRequestBody(httpPut, body, null, null);
            handleRequest(httpPut, delegate);
            HttpResponse response = httpclient.execute(httpPut);
            responseBody = handleResponse(response, delegate);
        } catch (ClientProtocolException e) {
            throw new APIException(e, APIException.UNTRACKED_ERROR);
        } catch (IOException e) {
            throw new APIException(e, APIException.UNTRACKED_ERROR);
        }
        return responseBody;
    }

    /**
     * Performs a DELETE request to the given url
     *
     * @param url      the url
     * @param user     an optional user for basic auth
     * @param password an optional password for basic auth
     * @param timeout  the request timeout
     * @param delegate the api delegate that will receive callbacks regarding progress and results
     * @return the response body as a string
     * @throws APIException
     */
    public static String performDelete(String url, String user, String password, int timeout, APIDelegate<?> delegate) throws APIException {
        Log.d(RestClient.class.getSimpleName(), "DELETE: " + url);
        String responseBody;
        try {
            DefaultHttpClient httpclient = HttpClientFactory.getClient();
            HttpDelete httpDelete = new HttpDelete(url);
            setupTimeout(timeout, httpclient);
            setupCommonHeaders(httpDelete);
            setupBasicAuth(user, password, httpDelete);
            handleRequest(httpDelete, delegate);
            HttpResponse response = httpclient.execute(httpDelete);
            responseBody = handleResponse(response, delegate);
        } catch (ClientProtocolException e) {
            throw new APIException(e, APIException.UNTRACKED_ERROR);
        } catch (IOException e) {
            throw new APIException(e, APIException.UNTRACKED_ERROR);
        }
        return responseBody;
    }

    /**
     * Private helper to setup teh request body
     */
    protected static void setupRequestBody(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase, String body, APIPostParams apiPostParams, File file) throws UnsupportedEncodingException {
        if (body != null && apiPostParams != null && apiPostParams.isMultipart()) {
            setupMultipartBodyWithFile(httpEntityEnclosingRequestBase, apiPostParams, body, file);
        } else {
            setupSimpleRequestBody(httpEntityEnclosingRequestBase, body);
        }
    }

    /**
     * Private helper to setup a simple request body
     */
    private static void setupSimpleRequestBody(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase, String body) throws UnsupportedEncodingException {
        if (body != null) {
            httpEntityEnclosingRequestBase.setHeader("Content-Type", "application/json; charset=utf-8");
            httpEntityEnclosingRequestBase.setEntity(new StringEntity(body, "UTF-8"));
        }
    }

    /**
     * Private helper to setup a multipart request body
     */
    private static void setupMultipartBodyWithFile(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase, APIPostParams apiPostParams, String body, File file) throws UnsupportedEncodingException {
        //MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        CountingMultipartEntity mpEntity = new CountingMultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"),
                file != null ? body.length() + file.length() : body.length(), apiPostParams);
        mpEntity.addPart("body", new StringBody(body, Charset.forName("UTF-8")));
        if (file != null) {
            FileBody uploadFilePart = new FileBody(file, "application/octet-stream");
            mpEntity.addPart("file", uploadFilePart);
        }
        httpEntityEnclosingRequestBase.setHeader(mpEntity.getContentType());
        httpEntityEnclosingRequestBase.setEntity(mpEntity);
    }

    /**
     * Private helper to setup the request timeout
     */
    private static void setupTimeout(int timeout, HttpClient client) {
        if (timeout > 0) {
            HttpConnectionParams.setConnectionTimeout(client.getParams(), timeout);
        }
    }

    /**
     * Private helper to notify the request is about to be sent
     */
    private static void handleRequest(HttpRequestBase request, APIDelegate<?> delegate) throws IOException, APIException {
        if (delegate != null) {
            delegate.onRequest(new HttpMessageRequestOperationImpl(request));
        }
    }

    /**
     * Private helper to notify the response has been received
     */
    private static String handleResponse(HttpResponse response, APIDelegate<?> delegate) throws IOException, APIException {
        if (delegate != null) {
            delegate.onResponse(new HttpMessageResponseOperationImpl(response));
        }
        String responseBody = null;
        int statusCode = response.getStatusLine().getStatusCode();
        // Examine the response status
        //Log.i(RestClient.class.getName(), response.getStatusLine().toString());

        // Get hold of the response entity
        HttpEntity entity = response.getEntity();
        // If the response does not enclose an entity, there is no need
        // to worry about connection release

        if (entity != null) {
            // A Simple JSON Response Read
            InputStream instream = entity.getContent();
            responseBody = IOUtils.convertStreamToString(instream);
            //Log.i(RestClient.class.getName(), result);

            // Closing the input stream will trigger connection release
            instream.close();
        }
        handleStatusCode(statusCode, responseBody);
        return responseBody;
    }

    /**
     * Private helper that handles status codes and translates 401 status codes into invalid credentials exceptions
     */
    private static void handleStatusCode(int statusCode, String responseBody) throws APIException {
        if (statusCode == 401) {
            JSONObject json = IOUtils.stringToJSON(responseBody);
            throw new InvalidCredentialsException(json != null ? json.optString("message") : "", statusCode);
        }
        if (statusCode != 200) {
            JSONObject json = IOUtils.stringToJSON(responseBody);
            throw new APIException(json != null ? json.optString("message") : "", statusCode);
        }
    }

    /**
     * Private helper to setup some common headers such as the user agent and the platform headers
     *
     * @param request the user agent
     */
    private static void setupCommonHeaders(HttpRequestBase request) {
        request.addHeader("X-47deg-Platform", "android");
        request.addHeader(CoreProtocolPNames.USER_AGENT, "47deg.com/restrung");
    }

    /**
     * Convenience method to set basic auth on a request that is about to be sent
     *
     * @param user     the user name
     * @param password the user password
     * @param request  the request
     */
    public static void setupBasicAuth(String user, String password, HttpRequestBase request) throws UnsupportedEncodingException {
        if (user != null && password != null) {
            String base64EncodedCredentials = Base64.encodeWebSafe((user + ":" + password).getBytes("UTF-8"), true);
            request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        }
    }


}
