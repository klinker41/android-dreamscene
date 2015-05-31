package com.klinker.android.dream;

import android.service.dreams.DreamService;

public class DreamSceneService extends DreamService {

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        // add initial options
        setInteractive(false);
        setFullscreen(true);
        setScreenBright(true);
    }

}
