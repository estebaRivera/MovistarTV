package com.smartboxtv.movistartv.data.reminder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Esteban- on 19-05-14.
 */
public class Reminder {

    public static String ID_PROGRAMA;
    public static String DATE_REMINDER;
    public static String REMINDER;


    public static String  getReminder(Context context, String id, String date) {
        SharedPreferences prefs = context.getSharedPreferences("reminder", Context.MODE_PRIVATE);
        REMINDER = prefs.getString("reminder " + id, "sin reminder");
        return REMINDER;
    }

    public static void createReminder(Context context,String reminder, String id) {

        REMINDER = reminder;
        SharedPreferences prefs = context.getSharedPreferences("reminder", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("reminder " + id, REMINDER);
        editor.commit();
    }
}
