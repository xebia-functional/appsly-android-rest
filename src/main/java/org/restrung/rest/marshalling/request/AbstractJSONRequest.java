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

package org.restrung.rest.marshalling.request;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Abstract class for JSONSerializable instance that relies upon Google's gson library to auto serialize beans to
 * a json string
 */
public abstract class AbstractJSONRequest implements JSONSerializable {

    /**
     * the shared gson builder
     */
    private static GsonBuilder gsonBuilder = new GsonBuilder();

    /**
     * Register dates to be serialized as timestamps with seconds precision
     */
    static {
        gsonBuilder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
                return date == null ? null : new JsonPrimitive(date.getTime() / 1000);
            }
        });
    }

    /**
     * @see org.restrung.rest.marshalling.request.JSONSerializable#toJSON()
     */
    @Override
    public String toJSON() {
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }

}
