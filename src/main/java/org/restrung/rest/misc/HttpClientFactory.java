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

package org.restrung.rest.misc;

import org.apache.http.HttpVersion;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * http client factory with a fake trust to enable communication with server that contained self signed certs
 */
public class HttpClientFactory {

    private static DefaultHttpClient instance = initializeClient();

	/**
	 * Private helper to initialize an http client
	 * @return the initialize http client
	 */
    private static synchronized DefaultHttpClient initializeClient() {
        //prepare for the https connection
        //call this in the constructor of the class that does the connection if
        //it's used multiple times
        SchemeRegistry schemeRegistry = new SchemeRegistry();

        // http scheme
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        // https scheme
        schemeRegistry.register(new Scheme("https", new FakeSocketFactory(), 443));

        HttpParams params;
        params = new BasicHttpParams();
        params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 1);
        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(1));
        params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
        HttpClientParams.setRedirecting(params, true);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf8");

        // ignore that the ssl cert is self signed
        ThreadSafeClientConnManager clientConnectionManager = new ThreadSafeClientConnManager(params, schemeRegistry);

        instance = new DefaultHttpClient(clientConnectionManager, params);
        instance.setRedirectHandler(new DefaultRedirectHandler()); //If the link has a redirect
        return instance;
    }

	/**
	 * Factory method that gets the singleton thread safe instance of the http client
	 * @return the http client instance
	 */
    public static DefaultHttpClient getClient() {
        return instance;
    }

}
