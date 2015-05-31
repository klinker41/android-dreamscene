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

import com.klinker.android.dream.util.NetworkUtils;

import java.io.File;

public class NetworkImageLoader extends AbstractImageLoader {

    private static final String TAG = "NetworkImageLoader";

    private String location;

    public NetworkImageLoader(Context context, String location, ImageView imageView) {
        super(context, imageView);
        this.location = location;
    }

    @Override
    public void run() {
        if (location == null || location.equals("")) {
            return;
        }

        final Bitmap bitmap = getBitmapFromMemCache(location);

        if (bitmap != null) {
            ImageView image = getImageView();
            if (image != null) {
                image.setImageBitmap(bitmap);
            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File f = cacheHelper.getCacheFileForImage(getContext(), location);

                    if (f != null && f.exists()) {
                        try {
                            Bitmap image = BitmapFactory.decodeFile(f.getPath());
                            setImage(image);
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                            setImage(null);
                        }
                    } else {
                        try {
                            Bitmap image = NetworkUtils.loadBitmap(location);
                            if (image != null) {
                                ioUtils.cacheBitmap(image, f);
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
    }

    private void setImage(final Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled() && getImageView() != null) {
            animateImageView(location, bitmap);
        }
    }
}