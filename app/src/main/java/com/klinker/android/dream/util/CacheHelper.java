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

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;

public class CacheHelper {

    private static final String TAG = "CacheHelper";

    // make sure that we are always getting the cache for Source, even on the launcher page
    public String getCacheLocationForImage(Context context, String location) {
        if (location == null) {
            return null;
        } else {
            return context.getExternalCacheDir() + "/" + hash(location);
        }
    }

    public File getCacheFileForImage(Context context, String location) {
        String cacheLocation = getCacheLocationForImage(context, location);
        if (cacheLocation == null) {
            return null;
        } else {
            return new File(cacheLocation);
        }
    }

    public String hash(String location) {
        return DigestUtils.shaHex(location);
    }

}
