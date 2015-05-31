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

import android.content.Context;
import android.graphics.Bitmap;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Help with caching images on external storage so we only need to download them once.
 *
 * We will hash the location url for the filename and then store it in the external cache
 * directory.
 */
public class CacheHelper {

    private static final String TAG = "CacheHelper";

    /**
     * Get the cache file path for a given location url
     * @param context the current application context
     * @param location the location url we will hash for the file name
     * @return the full path to the cached image
     */
    public String getCacheLocationForImage(Context context, String location) {
        if (location == null) {
            return null;
        } else {
            return context.getExternalCacheDir() + "/" + hash(location);
        }
    }

    /**
     * Get the cache file for a given location url
     * @param context the current application context
     * @param location the location url we will hash for the file name
     * @return the file for the cached image
     */
    public File getCacheFileForImage(Context context, String location) {
        String cacheLocation = getCacheLocationForImage(context, location);
        if (cacheLocation == null) {
            return null;
        } else {
            return new File(cacheLocation);
        }
    }

    /**
     * Cache the bitmap at a given file location
     * @param bitmap the bitmap to cache
     * @param file the file where to cache the bitmap
     * @throws Exception
     */
    public void cacheBitmap(Bitmap bitmap, File file) throws Exception {
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileOutputStream output = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        output.close();
    }

    /**
     * Hash the location url string so that it isn't too long
     * @param location the url to hash for a filename
     * @return the hashed string
     */
    private String hash(String location) {
        return DigestUtils.shaHex(location);
    }

}
