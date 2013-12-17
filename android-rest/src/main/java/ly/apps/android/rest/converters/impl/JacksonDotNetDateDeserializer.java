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

package ly.apps.android.rest.converters.impl;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Convenience deserializer for dotNet style dates in the form of /Date(utctimestamp)/
 */
public class JacksonDotNetDateDeserializer extends JsonDeserializer<Date> {

    /** Pattern for parsing date time values sent by a .NET web service. */
    private static Pattern pattern = Pattern.compile("^/Date\\([0-9]*\\)/$");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();
        if (!pattern.matcher(value).matches()) {
            return null;
        }
        // get UTC millisecond value
        long utcMillisecond = Long.parseLong(value.substring(value.indexOf("(") + 1, value.indexOf(")")));

        // new Date(long) takes milliseconds since 1970 in UTC
        return new Date(utcMillisecond);
    }

}
