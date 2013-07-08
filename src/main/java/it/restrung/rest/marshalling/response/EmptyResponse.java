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

package it.restrung.rest.marshalling.response;

import it.restrung.rest.marshalling.request.JSONSerializable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Provides an empty response for mutation methods that return empty bodies in some REST API's
 */
public class EmptyResponse implements JSONResponse, JSONSerializable {
    @Override
    public void fromJSON(JSONObject jsonObject) throws JSONException {
    }

    @Override
    public String toJSON() {
        return "";
    }
}
