/*
 * Copyright (C) 2013 47 Degrees, LLC
 * http://47deg.com
 * http://apps.ly
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

package ly.apps.android.rest.client;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import ly.apps.android.rest.cache.CacheAwareHttpClient;
import ly.apps.android.rest.cache.ContextPersistentCacheManager;
import ly.apps.android.rest.converters.impl.*;

import java.io.File;

/**
 * Factory to obtain default @see RestClient instances
 * You may want to skip and create instances of the restclient yourself if you want to customize aspect such as serialization
 * and service overrides.
 */
public class RestClientFactory {

    /**
     * Static factory method to obtain RestClient instances
     *
     * @return a singleton instance of a RestClient based on @see DefaultRestClientImpl
     */
    public static RestClient defaultClient(final Context context) {
        return new DefaultRestClientImpl(
                new CacheAwareHttpClient(new ContextPersistentCacheManager(context)) {{
                    enableHttpResponseCache(10 * 1024 * 1024, new File(context.getCacheDir(), "android-rest-http"));
                }},
                new JacksonQueryParamsConverter(),
                new DelegatingConverterService(){{
                    addConverter(new JacksonBodyConverter());
                    addConverter(new JacksonHttpFormValuesConverter());
                }}
        );
    }

}
