package com.smartboxtv.movistartv.fragments;

import android.app.Application;

import com.androidquery.callback.BitmapAjaxCallback;
import com.facebook.Session;

/**
 * Created by Esteban- on 19-04-14.
 */
public class NUNCHEE extends Application {

    public Session session = null;

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        BitmapAjaxCallback.clearCache();
    }

    /*@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }*/

}
