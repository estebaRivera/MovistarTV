package com.smartboxtv.movistartv.animation;

import android.graphics.drawable.Drawable;

/**
 * Created by Esteban- on 18-04-14.
 */
public class AnimationClass {
    private Drawable imageBackground;
    private String initialPhrase =null;

    public AnimationClass(Drawable imageBackground, String initialPhrase){
        this.imageBackground = imageBackground;
        this.initialPhrase = initialPhrase;
    }

    public Drawable getImageBackground() {
        return imageBackground;
    }

    public String getInitialPhrase() {
        return initialPhrase;
    }
}
