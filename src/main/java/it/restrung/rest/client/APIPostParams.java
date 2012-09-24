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

package it.restrung.rest.client;

/**
 * A set of post related params such as if this request is a multipart
 */
public interface APIPostParams {

    /**
     * Invoked while a file upload is in progress on a multipart POST request
     *
     * @param bytes    how many bytes have been transferred this far
     * @param progress the current progress from 0 to 100
     */
    void onProgress(long bytes, int progress);

    /**
     * Determines if the POST request is multipart
     *
     * @return true or false
     */
    boolean isMultipart();

}
