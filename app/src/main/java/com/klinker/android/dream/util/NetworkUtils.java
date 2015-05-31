/*
 * Copyright (C) 2015 Jacob Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klinker.android.dream.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Helper class for managing network interactions
 */
public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    /**
     * Load a bitmap from the given location url
     * @param location the url of the bitmap
     * @return the bitmap from the url
     * @throws Throwable
     */
    public static Bitmap loadBitmap(String location) throws Throwable {
        if (location.equals("")) {
            return null;
        } else if (!(location.startsWith("http:") || location.startsWith("https:") || location.startsWith("www."))) {
            location = "http:" + location;
        }

        location = location.replace(" ", "%20");
        URL url = new URL(location);

        // use a recycled bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferQualityOverSpeed = true;
        options.inBitmap = BitmapHelper.getCurrentBitmap();

        return BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
    }

    /**
     * Get a JSON string from the given url
     * @param url the url where the JSON is located
     * @return the String in the format of a JSON
     */
    public static String getJsonString(String url) {
        InputStream inputStream;
        String result = "";

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

}
