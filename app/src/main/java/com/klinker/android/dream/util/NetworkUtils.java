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

import java.net.URL;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    public Bitmap loadBitmap(String location) throws Throwable {
        if (location.equals("")) {
            return null;
        } else if (!(location.startsWith("http:") || location.startsWith("https:") || location.startsWith("www."))) {
            location = "http:" + location;
        }

        location = location.replace(" ", "%20");
        URL url = new URL(location);
        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        return image;
    }

}