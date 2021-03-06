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
    public boolean CONNECT_AWS = false;
    public boolean CONNECT_SERVICES_PYTHON = true;
    public boolean RELOGIN;
    private Thread.UncaughtExceptionHandler defaultUEH;
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler;

    public enum TrackerName {
        APP_TRACKER,            // Tracker used only in this app.
        GLOBAL_TRACKER,         // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER,      // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public void catchGlobal(){

        Log.e("Catch","Global");
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);

        _unCaughtExceptionHandler =
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread thread, Throwable ex) {

                        // here I do logging of exception to a db
                        // re-throw critical exception further to the os (important)
                        defaultUEH.uncaughtException(thread, ex);
                    }
                };
    }

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
        Log.e("Evento", evento);
        t.send(new HitBuilders.EventBuilder("evento", evento).build());
    }

    public void sendAnaliticsScreen(String titulo){
        Tracker t = getTracker(TrackerName.APP_TRACKER);
        t.setScreenName(titulo);
        Log.e("Screen", titulo);
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        BitmapAjaxCallback.clearCache();
    }

}
