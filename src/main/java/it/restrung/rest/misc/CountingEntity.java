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

package it.restrung.rest.misc;

import it.restrung.rest.client.APIPostParams;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * An implementation of MultipartEntity entity that allows the caller to get upload progress
 */
public class CountingEntity extends FileEntity {

    /**
     * The listener receiving progress updates
     */
    private final APIPostParams listener;

    /**
     * The file's length
     */
    private long fileLength = 0;

    /**
     * Constructor that creates a multi part entity for a POST or PUT request
     * and accepts a listener for upload progress
     *
     * @param file the file to upload
     * @param fileLength the known file length
     * @param listener   a listener to receive upload progress
     */
    public CountingEntity(final File file, long fileLength, APIPostParams listener) {
        super(file, listener.getContentType());
        this.fileLength = fileLength;
        this.listener = listener;
    }

    /**
     * @see org.apache.http.entity.mime.MultipartEntity#writeTo(java.io.OutputStream)
     */
    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, this.listener, fileLength));
    }

}
