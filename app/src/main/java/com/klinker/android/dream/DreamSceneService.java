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

package com.klinker.android.dream;

import android.os.Handler;
import android.service.dreams.DreamService;
import android.widget.ImageView;

import com.klinker.android.dream.loader.NetworkImageLoader;

import java.util.Random;

public class DreamSceneService extends DreamService {

    private static final String TAG = "DreamSceneService";

    private static final String[] BACKGROUNDS = new String[] {
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/390823.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/456548.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/456965.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/458532.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/463395.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/468561.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/468569.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/475841.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/475843.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/476288.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/478066.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/479128.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/480730.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/484717.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/491050.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/546077.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/550965.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/558952.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/562771.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/568653.jpg",
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds/569143.jpg"
    };

    private static final int MAX_SWITCH_TIME = 40000;       // 40 seconds
    private static final int MIN_SWITCH_TIME = 20000;       // 20 seconds

    private Handler handler;
    private ImageView background;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        // add initial options
        setInteractive(false);
        setFullscreen(true);
        setScreenBright(true);

        // show the view on the screen
        setContentView(R.layout.daydream_service);

        // set up the background image
        background = (ImageView) findViewById(R.id.imageView);

        // set the initial background
        handler = new Handler();
        switchBackground();
    }

    private void switchBackground() {
        new NetworkImageLoader(this, getRandomBackgroundUrl(), background).run();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switchBackground();
            }
        }, getRandomSwitchTime());
    }

    private String getRandomBackgroundUrl() {
        Random r = new Random();
        int num = r.nextInt(BACKGROUNDS.length);
        return BACKGROUNDS[num];
    }

    private int getRandomSwitchTime() {
        Random r = new Random();
        return r.nextInt(MAX_SWITCH_TIME - MIN_SWITCH_TIME) + MIN_SWITCH_TIME;

    }

}