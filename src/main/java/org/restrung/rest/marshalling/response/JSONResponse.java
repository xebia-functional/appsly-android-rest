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

package org.restrung.rest.marshalling.response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Interface implemented by beans that may be inflated from a response
 */
public interface JSONResponse extends Serializable {

	/**
	 * Handles a json object representation of the response body
	 * @param jsonObject the json object
	 */
    void fromJSON(JSONObject jsonObject) throws JSONException;

}
