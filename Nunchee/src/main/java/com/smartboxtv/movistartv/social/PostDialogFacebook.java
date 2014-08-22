package com.smartboxtv.movistartv.social;

import android.os.Bundle;
import android.util.Log;

import com.facebook.android.*;
import com.facebook.android.Facebook;

/**
 * Created by Esteban- on 14-08-14.
 */
public class PostDialogFacebook  implements Facebook.DialogListener{
    @Override
    public void onComplete(Bundle values) {

    }

    @Override
    public void onFacebookError(FacebookError e) {
        Log.e("Facebook", e.getMessage());
        e.printStackTrace();

    }

    @Override
    public void onError(DialogError e) {

    }

    @Override
    public void onCancel() {

    }
}
