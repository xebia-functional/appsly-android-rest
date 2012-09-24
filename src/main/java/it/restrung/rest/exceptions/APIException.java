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

package it.restrung.rest.exceptions;

/**
 * Represents a checked exception due to an API communication, authentication or operation exception
 */
public class APIException extends Exception {

    /**
     * status code for errors that are unknown
     */
    public static int UNTRACKED_ERROR = -1;

    /**
     * the error code usually equivalent to the http status code
     */
    private int errorCode;

    /**
     * Constructor that constructs an APIException from an error code
     *
     * @param errorCode the error code usually equivalent to the http status code
     */
    public APIException(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Constructor that constructs an APIException from an error code and message
     *
     * @param message   the exception message
     * @param errorCode the error code usually equivalent to the http status code
     */
    public APIException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructor that encapsulates another exception as an APIException
     *
     * @param message   the exception message
     * @param throwable another exception or error
     * @param errorCode the error code usually equivalent to the http status code
     */
    public APIException(String message, Throwable throwable, int errorCode) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    /**
     * Constructor that encapsulates another exception as an APIException
     *
     * @param throwable another exception or error
     * @param errorCode the error code usually equivalent to the http status code
     */
    public APIException(Throwable throwable, int errorCode) {
        super(throwable);
        this.errorCode = errorCode;
    }

    /**
     * the error code usually equivalent to the http status code
     *
     * @return the error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * sets the error code
     *
     * @param errorCode the error code usually equivalent to the http status code
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
