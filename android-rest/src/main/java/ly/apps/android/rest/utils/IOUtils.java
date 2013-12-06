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

package ly.apps.android.rest.utils;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * IO Utils to save and retrieve serialized objects from disk
 */
public class IOUtils {

    /**
     * Prevents from instantiation
     */
    private IOUtils() {
    }

    /**
     * Serializes ans saves a Serializable object to a file
     *
     * @param object the source object
     * @param file   the target file
     */
    static public void saveSerializableObjectToDisk(Object object, File file) {
        try {
            file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            GZIPOutputStream gzos = new GZIPOutputStream(fos);
            ObjectOutputStream out = new ObjectOutputStream(gzos);
            out.writeObject(object);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            Log.d(IOUtils.class.getName(), "Error, file not found for save serializable object to disk");
            throw new RuntimeException(e);
        } catch (IOException e) {
            Log.d(IOUtils.class.getName(), "Error on save serializable object");
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a serialized object from a file
     *
     * @param file the file where the object was serialized
     * @return the serialized object in its real in memory object representation
     */
    @SuppressWarnings("unchecked")
    static public <T> T loadSerializableObjectFromDisk(File file) {
        T result = null;
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                GZIPInputStream gzis = new GZIPInputStream(fis);
                ObjectInputStream in = new ObjectInputStream(gzis);
                result = (T) in.readObject();
                in.close();
            } catch (FileNotFoundException e) {
                Log.d(IOUtils.class.getName(), "Error, file not found for load serializable object from disk");
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * Converts an input stream into a String
     *
     * @param inputStream the input stream
     * @return the String
     */
    public static String convertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder resultBuilder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultBuilder.toString();
    }

    /**
     * Converts a string response body into a JSONObject
     *
     * @param responseBody the response body
     * @return the json object
     */
    public static JSONObject stringToJSON(String responseBody) {
        JSONObject json = null;
        try {
            // A Simple JSONObject Creation
            if (responseBody != null && !responseBody.equals("")) {
                json = new JSONObject(responseBody);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

}
