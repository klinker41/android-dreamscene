/*
 * Copyright (C) 2017 Jake Klinker
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
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Random;

/**
 * Dream Service with pulls wallpaper urls from a JSON and displays them randomly on screen
 */
public class DreamSceneService extends DreamService {

    private static final String TAG = "DreamSceneService";

    /**
     * URL for finding all wallpapers. By doing this instead of hardcoding a value, new wallpapers
     * does not require a new install of the app
     */
    private static final String JSON_URL =
            "https://raw.githubusercontent.com/klinker41/android-dreamscene/master/backgrounds.json";

    /**
     * Max time in milliseconds that a switch could occur
     */
    private static final int MAX_SWITCH_TIME = 40000;       // 40 seconds

    /**
     * Min time in milliseconds that a switch could occur
     */
    private static final int MIN_SWITCH_TIME = 20000;       // 20 seconds

    private JSONArray backgrounds;
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
        initBackgrounds();
    }

    /**
     * Start a thread that fetches a JSONArray of all wallpapers, then set the first one
     */
    private void initBackgrounds() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String elements = NetworkUtils.getJsonString(JSON_URL);
                    backgrounds = new JSONArray(elements);

                    Log.v(TAG, "found JSONArray: " + elements);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            switchBackground();
                        }
                    });
                } catch (JSONException e) {
                    Log.wtf(TAG, "something wrong with backgrounds json :(", e);
                }
            }
        }).start();
    }

    /**
     * Find a random background from the list, load it, and set it. Then, delay a certain amount
     * of time and do it again
     */
    private void switchBackground() {
        try {
            Glide.with(this).load(getRandomBackgroundUrl()).into(background);
        } catch (JSONException e) {
            Log.e(TAG, "Error switching backgrounds", e);
        }

        // creates a continuous loop that goes forever, until the daydream is killed
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switchBackground();
            }
        }, getRandomSwitchTime());
    }

    /**
     * Choose a random background URL from the list
     *
     * @return a URL for a background
     * @throws JSONException when the url is out of bounds on the JSON, shouldn't happen.
     */
    private String getRandomBackgroundUrl() throws JSONException {
        // TODO keep a reference to the last wallpaper in memory so that we don't set it twice
        // in a row

        Random r = new Random();
        int num = r.nextInt(backgrounds.length());
        String background = backgrounds.getString(num);
        Log.v(TAG, "displaying new background: " + background);
        return background;
    }

    /**
     * Choose a random time to switch, somewhere in the range [MIN_SWITCH_TIME, MAX_SWITCH_TIME)
     *
     * @return a random time in milliseconds
     */
    private int getRandomSwitchTime() {
        Random r = new Random();
        return r.nextInt(MAX_SWITCH_TIME - MIN_SWITCH_TIME) + MIN_SWITCH_TIME;
    }

}