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

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import com.klinker.android.dream.util.CacheHelper;
import com.klinker.android.dream.util.IoUtils;
import com.klinker.android.dream.util.NetworkUtils;

abstract class AbstractImageLoader implements Runnable {

    private static final String TAG = "AbstractImageLoader";
    private static final int ANIMATION_DURATION = 500;

    private Handler handler;
    private Context context;
    private ImageView imageView;
    public NetworkUtils networkUtils;
    public CacheHelper cacheHelper;
    public IoUtils ioUtils;

    private static LruCache<String, Bitmap> mIconCache;

    public AbstractImageLoader(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
        try {
            Looper.prepare();
        } catch (Throwable e) {
        }
        this.handler = new Handler();
        this.networkUtils = new NetworkUtils();
        this.cacheHelper = new CacheHelper();
        this.ioUtils = new IoUtils();

        if (mIconCache == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            mIconCache = new LruCache<String, Bitmap>(maxMemory) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
    }

    public void animateImageView(String location, final Bitmap image) {
        addBitmapToMemoryCache(location, image);
        handler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setAlpha(1.0f);
                imageView.animate()
                        .alpha(0.0f)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                Drawable drawable = imageView.getDrawable();

                                if (drawable instanceof BitmapDrawable) {
                                    ((BitmapDrawable) drawable).getBitmap().recycle();
                                }

                                imageView.setImageBitmap(image);
                                imageView.setAlpha(0.0f);
                                imageView.animate()
                                        .alpha(1.0f)
                                        .setDuration(ANIMATION_DURATION)
                                        .setListener(null)
                                        .start();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        })
                        .start();
            }
        });
    }

    public Context getContext() {
        return context;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Bitmap getBitmapFromMemCache(String key) {
        if (key == null) {
            return null;
        } else {
            return mIconCache.get(key);
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (mIconCache != null && key != null && bitmap != null) {
            mIconCache.put(key, bitmap);
        }
    }

    public Handler getHandler() {
        return handler;
    }

}
