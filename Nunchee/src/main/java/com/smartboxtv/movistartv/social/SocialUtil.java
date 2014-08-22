package com.smartboxtv.movistartv.social;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.auth.FacebookHandle;
import com.androidquery.auth.TwitterHandle;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;

import com.facebook.android.Facebook;
import com.facebook.widget.FacebookDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Franklin Cruz on 20-03-14.
 */
public final class SocialUtil {

    private AQuery aq;
    private Context context;
    private final String APP_ID = "487156824723876";
    private Facebook facebook = null;

    public SocialUtil(Context context) {
        this.context = context;
        this.aq = new AQuery(context);
    }

    public SocialUtil(Context context, Facebook facebook) {
        this.context = context;
        this.aq = new AQuery(context);
        this.facebook = facebook;

    }
    public void fbshare (){

    }

    public void fbshare(Activity activity, String text, String imageUrl,String url, String title, String description, final SocialUtilHandler handler) {

        Map<String, String> fbparams = new HashMap<String, String>();
        fbparams.put("message",text);
        fbparams.put("picture", imageUrl);
        fbparams.put("link", url);
        fbparams.put("description", description);
        fbparams.put("name", title);
        fbparams.put("caption", "Movistar TV");

        FacebookHandle handle = new FacebookHandle(activity, "487156824723876", "publish_stream,email");

        /*aq.auth(handle).ajax("https://graph.facebook.com/me/feed", fbparams, JSONObject.class, new AjaxCallback<JSONObject>(){

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if(status.getCode() == 200) {
                    handler.done(null);
                }
                else {
                    handler.done(new Exception(status.getMessage()));
                    Log.e("Mensaje ","No publicado");
                }

            }
        });*/
    }

    public void tweet(Activity activity, String tweet, final SocialUtilHandler handler) {

        if(tweet.length() > 140) {
            tweet = tweet.substring(0,140);
        }

        //TwitterHandle handle = new TwitterHandle(activity, "ORUGhRg8O2ieZdWUnY98A", "XLuLuGHHVdYz0LkXYRiXybBVPTAUcDvnCxCH1cATE");
        TwitterHandle handle = new TwitterHandle(activity, "I81MsOAJdP9fHRdVLk1C6VyNN", "yUV7d6Pu94fGx2SljSvxy3FoQHAfDLCH3CvhJM1QOx9FjqPCII");
        String url = "https://api.twitter.com/1.1/statuses/update.json";

        Map<String,String> params = new HashMap<String, String>();
        params.put("status", tweet);

        aq.auth(handle).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {
                    Log.d("Twitter", object.toString());

                    if(status.getCode() == 200) {
                        handler.done(null);
                    }
                    else {
                        handler.done(new Exception(status.getMessage()));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Twitter", e.toString());
                }

            }
        });
    }

    public abstract static class SocialUtilHandler {

        public void done(Exception e) {

        }

    }

}
