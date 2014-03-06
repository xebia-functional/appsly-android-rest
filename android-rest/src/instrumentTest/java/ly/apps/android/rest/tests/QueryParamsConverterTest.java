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

package ly.apps.android.rest.tests;

import android.test.InstrumentationTestCase;
import ly.apps.android.rest.converters.QueryParamsConverter;
import ly.apps.android.rest.converters.impl.JacksonQueryParamsConverter;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;

public class QueryParamsConverterTest extends InstrumentationTestCase {

    private QueryParamsConverter converter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        converter = new JacksonQueryParamsConverter();
    }

    public void testParsePathParams() throws UnsupportedEncodingException {
        assertEquals(
                "/api/path/47/test/other",
                converter.parsePathParams(
                        "/api/path/{id}/test/{other}",
                        new LinkedHashMap<Integer, String>(){{
                            put(0, "id");
                            put(1, "other");
                        }}
                        , new Object[]{47, "other"}
                )
        );
    }

    public void testParseBundledQueryParams() throws UnsupportedEncodingException {
        assertEquals(
                "/api/path/test?id=47&other=other",
                converter.parseBundledQueryParams(
                        "/api/path/test",
                        new LinkedHashMap<String, Object>(){{
                            put("id", 47);
                            put("other", "other");
                        }}
                )
        );
    }

    public void testParseQueryParams() throws UnsupportedEncodingException {
        assertEquals(
                "/api/path/test?id=47&other=other",
                converter.parseQueryParams(
                        "/api/path/test",
                        new LinkedHashMap<Integer, String>() {{
                            put(0, "id");
                            put(1, "other");
                        }}
                        , new Object[]{47, "other"}
                )
        );
    }
}
