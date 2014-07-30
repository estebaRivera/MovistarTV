package com.smartboxtv.movistartv.data.image;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by Esteban- on 26-05-14.
 */
public class ScreenShot {

    public static Bitmap takeScreenshot(View v) {
        try {
            //View v = ((Activity) context).findViewById(R.id.container);

            v.setDrawingCacheEnabled(true);
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            v.buildDrawingCache(true);

            Bitmap bm = Bitmap.createBitmap(v.getDrawingCache());

            v.setDrawingCacheEnabled(false);

            v.destroyDrawingCache();

            return bm;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
