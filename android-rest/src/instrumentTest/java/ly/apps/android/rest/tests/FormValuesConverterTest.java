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
import ly.apps.android.rest.converters.BodyConverter;
import ly.apps.android.rest.converters.impl.FileFormField;
import ly.apps.android.rest.converters.impl.JacksonHttpFormValuesConverter;
import ly.apps.android.rest.converters.impl.MultipartEntity;
import ly.apps.android.rest.utils.FileUtils;
import ly.apps.android.rest.utils.HeaderUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

public class FormValuesConverterTest extends InstrumentationTestCase {

    private BodyConverter converter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        converter = new JacksonHttpFormValuesConverter();
    }

    public void testToRequestBody() throws IOException {
        Map<String, String> testParams = new LinkedHashMap<String, String>() {{
            put("a", "b");
            put("c", "d");
            put("e", "x");
        }};
        assertEquals(
                "a=b&c=d&e=x",
                FileUtils.convertStreamToString(converter.toRequestBody(
                        testParams,
                        HeaderUtils.CONTENT_TYPE_FORM_URL_ENCODED
                ).getContent())
        );
    }

    public void testFromResponseBody() throws UnsupportedEncodingException {
        Exception ne = null;
        try {
            converter.fromResponseBody(null, null, null, null);
        } catch (Exception e) {
            ne = e;
        }
        assertNotNull(ne);
        assertEquals(UnsupportedOperationException.class, ne.getClass());
    }

    public void testFromResponseBodyMultipartWithFile() throws IOException {
        final File file = new File(".");
        Map<String, Object> testParams = new LinkedHashMap<String, Object>() {{
            put("file", new FileFormField(file, null));
            put("c", "d");
        }};
        MultipartEntity entity = (MultipartEntity) converter.toRequestBody(testParams, HeaderUtils.CONTENT_TYPE_MULTIPART_FORM_DATA);
        assertNotNull(entity);
        assertEquals(file, entity.getFileParts().get(0).getFile());
    }

    public void testSupportsRequestContentType() {
        assertTrue(converter.supportsRequestContentType(HeaderUtils.CONTENT_TYPE_FORM_URL_ENCODED));
    }

    public void testSupportsResponseContentType() {
        assertFalse(converter.supportsResponseContentType(null));
    }
}
