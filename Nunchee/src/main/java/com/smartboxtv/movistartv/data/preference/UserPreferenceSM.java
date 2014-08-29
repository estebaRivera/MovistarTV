package com.smartboxtv.movistartv.data.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Esteban- on 07-08-14.
 */
public class UserPreferenceSM {

    public static String idFacebook;
    public static String NombreUsuario;
    public static String idNunchee;
    public static String idPrograma;
    public static String idCanal;
    public static String tokenMovistar;
    public static String tokenFacebook;
    public static int WIDTH_SCREEN;
    public static int HEIGTH_SCREEN;
    public static int isFinish;
    public static boolean isFacebookActive;
    public static boolean showTutorial;

    public UserPreferenceSM(){

    }

    public static boolean isFacebookActive(Context actividad) {
        SharedPreferences prefs = actividad.getSharedPreferences("is_facebook_sm", Context.MODE_PRIVATE);
        isFacebookActive = prefs.getBoolean("active", true);
        return isFacebookActive;
    }

    public static void setFacebookActive(boolean finish, Context actividad) {
        UserPreference.isFacebookActive = finish;
        SharedPreferences prefs = actividad.getSharedPreferences("is_facebook_sm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("active", finish);
        editor.commit();
    }

    public static String getTokenFacebook(Context actividad) {
        SharedPreferences prefs = actividad.getSharedPreferences("token_facebook_sm", Context.MODE_PRIVATE);
        tokenFacebook = prefs.getString("token_fb","");
        return tokenFacebook;
    }

    public static void setTokenFacebook(String token, Context actividad) {
        UserPreferenceSM.tokenFacebook = token;
        SharedPreferences prefs = actividad.getSharedPreferences("token_facebook_sm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token_fb", token);
        editor.commit();
    }
    public static boolean isShowTutorial(Context actividad) { // responde true sólo la primera vez
        SharedPreferences prefs = actividad.getSharedPreferences("show_tutorial_sm", Context.MODE_PRIVATE);
        showTutorial = prefs.getBoolean("show", true);
        return showTutorial;
    }

    public static void setShowTutorial(boolean show, Context actividad) {
        UserPreference.showTutorial = show;
        SharedPreferences prefs = actividad.getSharedPreferences("show_tutorial_sm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("show", show);
        editor.commit();
    }
    public static int isFinish(Context actividad) {
        SharedPreferences prefs = actividad.getSharedPreferences("finish_sm", Context.MODE_PRIVATE);
        HEIGTH_SCREEN = prefs.getInt("finish",0);
        return isFinish;
    }

    public static void setFinish(int finish, Context actividad) {
        UserPreference.isFinish = finish;
        SharedPreferences prefs = actividad.getSharedPreferences("finish_sm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("finish", HEIGTH_SCREEN);
        editor.commit();
    }

    public static int getHEIGTH_SCREEN(Context actividad) {
        SharedPreferences prefs = actividad.getSharedPreferences("pantalla_sm", Context.MODE_PRIVATE);
        HEIGTH_SCREEN = prefs.getInt("pantalla_alto",0);
        return HEIGTH_SCREEN;
    }

    public static void setHEIGTH_SCREEN(int HEIGTH_SCREEN, Context actividad) {
        UserPreference.HEIGTH_SCREEN = HEIGTH_SCREEN;
        SharedPreferences prefs = actividad.getSharedPreferences("pantalla_sm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("pantalla_alto", HEIGTH_SCREEN);
        editor.commit();
    }

    public static int getWIDTH_SCREEN(Context actividad) {
        SharedPreferences prefs = actividad.getSharedPreferences("pantalla_sm", Context.MODE_PRIVATE);
        WIDTH_SCREEN = prefs.getInt("pantalla_ancho",0);
        return WIDTH_SCREEN;
    }

    public static void setWIDTH_SCREEN(int WIDTH_SCREEN, Context actividad) {
        UserPreference.WIDTH_SCREEN = WIDTH_SCREEN;
        SharedPreferences prefs = actividad.getSharedPreferences("pantalla_sm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("pantalla_ancho", WIDTH_SCREEN);
        editor.commit();
    }

    public static void setNombreFacebook(String nombre, Context actividad){
        SharedPreferences prefs = actividad.getSharedPreferences("facebook_sm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("facebook_nombre",nombre);
        editor.commit();
    }
    public static String getNombreFacebook(Context actividad){

        SharedPreferences prefs = actividad.getSharedPreferences("facebook_sm", Context.MODE_PRIVATE);
        NombreUsuario = prefs.getString("facebook_nombre", "Nombre Usuario");
        return NombreUsuario;
    }

    public static void setIdFacebook(String id, Context actividad){
        SharedPreferences prefs = actividad.getSharedPreferences("facebook_sm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("facebook_id", id);
        editor.commit();
    }


    public static String getTokenMovistar(Context actividad){

        SharedPreferences prefs = actividad.getSharedPreferences("token_sm", Context.MODE_PRIVATE);
        tokenMovistar = prefs.getString("token_movistar", "");
        return tokenMovistar;
    }

    public static void setTokenMovistar(String token, Context actividad){
        SharedPreferences prefs = actividad.getSharedPreferences("token_sm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token_movistar", token);
        editor.commit();
    }

    public static String getIdFacebook(Context actividad){

        SharedPreferences prefs = actividad.getSharedPreferences("facebook_sm", Context.MODE_PRIVATE);
        idFacebook= prefs.getString("facebook_id", " ");
        return idFacebook;
    }

    public static void setIdNunchee(String id, Context actividad){
        SharedPreferences prefs = actividad.getSharedPreferences("nunchee_sm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nunchee_id", id);
        editor.commit();
    }
    public static String getIdNunchee(Context actividad){

        SharedPreferences prefs = actividad.getSharedPreferences("nunchee_sm", Context.MODE_PRIVATE);
        idNunchee= prefs.getString("nunchee_id", " ");
        return idNunchee;
    }

    public static String getIdPrograma(Activity actividad) {
        SharedPreferences prefs = actividad.getSharedPreferences("programa_sm", Context.MODE_PRIVATE);
        idPrograma = prefs.getString("nunchee_idPrograma", " ");
        return idPrograma;
    }

    public static void setIdPrograma(String idPrograma, Activity actividad) {
        UserPreference.idPrograma = idPrograma;
        SharedPreferences prefs = actividad.getSharedPreferences("programa_sm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nunchee_idPrograma", idPrograma);
        editor.commit();
    }

    public static String getIdCanal(Activity actividad) {
        SharedPreferences prefs = actividad.getSharedPreferences("canal_sm", Context.MODE_PRIVATE);
        idCanal = prefs.getString("nunchee_idCanal", " ");
        return idCanal;
    }

    public static void setIdCanal(String idCanal, Activity actividad) {
        UserPreference.idCanal = idCanal;
        SharedPreferences prefs = actividad.getSharedPreferences("canal_sm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nunchee_idCanal", idCanal);
        editor.commit();
    }
}
