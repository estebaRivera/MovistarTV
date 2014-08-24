package com.smartboxtv.movistartv.fragments;

import android.app.Application;
import android.util.Log;

import com.androidquery.callback.BitmapAjaxCallback;
import com.facebook.Session;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.smartboxtv.movistartv.R;

import java.util.HashMap;

/**
 * Created by Esteban- on 19-04-14.
 */
public class NUNCHEE extends Application {

    public Session session = null;
    public boolean CONNECT_AWS = true;
    public boolean CONNECT_SERVICES_PYTHON = false;

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();


    synchronized Tracker getTracker(TrackerName trackerId) {
        Log.e("Tracker ID", trackerId.toString());
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t =  analytics.newTracker(R.xml.analytics);
            mTrackers.put(trackerId, t);
        }

        return mTrackers.get(trackerId);
    }

    public void sendAnalitics( String evento){
        Tracker t = getTracker(TrackerName.APP_TRACKER);
        Log.e("evento", evento);
        t.send(new HitBuilders.EventBuilder("evento", evento).build());
    }

    public void sendAnaliticsScreen(String titulo){
        Tracker t = getTracker(TrackerName.APP_TRACKER);
        t.setScreenName(titulo);
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        BitmapAjaxCallback.clearCache();
    }

}
