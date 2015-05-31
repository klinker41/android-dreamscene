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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A helper class for allocating memory for large bitmaps. We create 2 very large (4k resolution)
 * bitmaps and store them in memory. Then we keep a reference to one of them, load the new image
 * into that allocated memory and switch the reference to the other one and so on.
 *
 * We require 2 bitmaps so that we can fade out of one and into the next. Without an animation,
 * one would suffice and take less space.
 */
public class BitmapHelper {

    private static final Bitmap inBitmap1;
    private static final Bitmap inBitmap2;

    private static final int BITMAP_WIDTH = 3840;
    private static final int BITMAP_HEIGHT = 2160;

    private static AtomicInteger currentBitmap = new AtomicInteger(1);

    /**
     * Create 2 bitmaps in memory with a resolution of 3840x2160 (4k Ultra HD)
     */
    static {
        inBitmap1 = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.ARGB_8888);
        inBitmap2 = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.ARGB_8888);
    }

    /**
     * Get a reference to the current bitmap that we want to overwrite with a new image
     * @return the bitmap reference
     */
    public static Bitmap getCurrentBitmap() {
        if (currentBitmap.get() == 1) {
            currentBitmap.set(2);
            return inBitmap1;
        } else {
            currentBitmap.set(1);
            return inBitmap2;
        }
    }

}
