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

package com.klinker.android.dream.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.klinker.android.dream.util.BitmapHelper;
import com.klinker.android.dream.util.NetworkUtils;

import java.io.File;

/**
 * Task helper for loading images into an ImageView from a given url. Handles caching the image
 * so we don't need to continuously download them.
 */
public class NetworkImageLoader extends AbstractImageLoader {

    private static final String TAG = "NetworkImageLoader";

    private String location;

    /**
     * Load an image from the location into the imageView
     * @param context the current application context
     * @param location the url of the image
     * @param imageView the imageview to display the image in
     */
    public NetworkImageLoader(Context context, String location, ImageView imageView) {
        super(context, imageView);
        this.location = location;
    }

    @Override
    public void run() {
        if (location == null || location.equals("")) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                // check if the file is already cached so we don't download again
                File f = cacheHelper.getCacheFileForImage(getContext(), location);

                if (f != null && f.exists()) {
                    try {
                        // set up options to reuse a bitmap and not waste too much memory
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferQualityOverSpeed = true;
                        options.inBitmap = BitmapHelper.getCurrentBitmap();

                        Bitmap image = BitmapFactory.decodeFile(f.getPath(), options);
                        setImage(image);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        setImage(null);
                    }
                } else {
                    try {
                        // load the bitmap from a network and cache it
                        Bitmap image = NetworkUtils.loadBitmap(location);
                        if (image != null) {
                            cacheHelper.cacheBitmap(image, f);
                            setImage(image);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        setImage(null);
                    }
                }
            }
        }).start();
    }

    /**
     * set the bitmap to the imageview with an animation
     */
    private void setImage(final Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled() && getImageView() != null) {
            animateImageView(bitmap);
        }
    }
}