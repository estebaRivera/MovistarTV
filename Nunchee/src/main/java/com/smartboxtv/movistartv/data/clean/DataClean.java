package com.smartboxtv.movistartv.data.clean;

import android.util.Log;

/**
 * Created by Esteban- on 17-05-14.
 */
public class DataClean {

    public static void garbageCollector(String TAG){

        Runtime garbage = Runtime.getRuntime();
        Log.e(TAG+" Garbage Collector", "Memoria Total: " + garbage.totalMemory());
        Log.e(TAG+" Garbage Collector", "Memoria Max: " + garbage.maxMemory());
        Log.e(TAG+" Garbage Collector", "Memoria libre antes de limpieza: " + garbage.freeMemory());
        garbage.gc();
        Log.e(TAG+" Garbage Collector", "Memoria libre después s la limpieza:" + garbage.freeMemory());
    }
}
