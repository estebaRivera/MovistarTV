package com.smartboxtv.movistartv.programation.customize;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Created by Esteban- on 24-05-14.
 */
public class ImageCustom extends ImageView {

    public ImageCustom(Context context) {
        super(context);
    }

    public ImageCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void startAnimation(Animation animation) {
        super.startAnimation(animation);
    }
}
