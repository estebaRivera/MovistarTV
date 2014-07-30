package com.smartboxtv.movistartv.data.trivia;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Esteban- on 18-04-14.
 */
public class GameTrivia {

    public static int VIDA_TRIVIA;
    public static int NIVEL_TRIVIA;
    public static int PUNTAJE;
    public static int NIVEL_1_BLOQUEADO;
    public static int NIVEL_2_BLOQUEADO;
    public static int NIVEL_3_BLOQUEADO;
    public static int MAX_PUNTAJE_NIVEL_1;
    public static int MAX_PUNTAJE_NIVEL_2;
    public static int MAX_PUNTAJE_NIVEL_3;
    public static int JUEGO_ACTUAL;

    // VIDA TRIVIA
    public static int getVIDA_TRIVIA(Context context, String id) {
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        VIDA_TRIVIA = prefs.getInt("vida_"+id,3);
        return VIDA_TRIVIA;
    }

    public static void setVIDA_TRIVIA(Context context,int VIDA_TRIVIA, String id) {
        GameTrivia.VIDA_TRIVIA = VIDA_TRIVIA;
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("vida_"+id, VIDA_TRIVIA);
        editor.commit();
    }

    // NIVEL TRIVIA
    public static int getNIVEL_TRIVIA(Context context,String id) {
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        NIVEL_TRIVIA = prefs.getInt("nivel_"+id,1);
        return NIVEL_TRIVIA;
    }

    public static void setNIVEL_TRIVIA(Context context,int NIVEL_TRIVIA,String id) {
        GameTrivia.NIVEL_TRIVIA = NIVEL_TRIVIA;
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("nivel_"+id, VIDA_TRIVIA);
        editor.commit();
    }

    // JUEGO ACTUAL
    public static int getJUEGO_ACTUAL(Context context, String id) {
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        JUEGO_ACTUAL = prefs.getInt("actual_"+id, 0);
        return JUEGO_ACTUAL;
    }

    public static void setJUEGO_ACTUAL(Context context,int JUEGO_ACTUAL,String id) {
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("actual_"+id,JUEGO_ACTUAL);
        editor.commit();
    }

    // PUNTAJE MAXIMOS POR NIVELES
    // NIVEL FACIL
    public static int getPUNTAJE_MAX_NIVEL_FACIL(Context context,String id) {
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        MAX_PUNTAJE_NIVEL_1 = prefs.getInt("max_facil_"+id,0);
        return MAX_PUNTAJE_NIVEL_1;
    }

    public static void setPUNTAJE_MAX_NIVEL_FACIL(Context context,int PUNTAJE_MAX_NIVEL, String id) {
        GameTrivia.MAX_PUNTAJE_NIVEL_1 = PUNTAJE_MAX_NIVEL;
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("max_facil_"+id, PUNTAJE_MAX_NIVEL);
        editor.commit();
    }

    // NIVEL MEDIO
    public static int getPUNTAJE_MAX_NIVEL_MEDIO(Context context, String id) {
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        MAX_PUNTAJE_NIVEL_2 = prefs.getInt("max_medio_"+id,0);
        return MAX_PUNTAJE_NIVEL_2;
    }

    public static void setPUNTAJE_MAX_NIVEL_MEDIO(Context context,int PUNTAJE_MAX_NIVEL, String id) {
        GameTrivia.MAX_PUNTAJE_NIVEL_2 = PUNTAJE_MAX_NIVEL;
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("max_medio_"+id, PUNTAJE_MAX_NIVEL);
        editor.commit();
    }
    // NIVEL DIFICIL
    public static int getPUNTAJE_MAX_NIVEL_DIFICIL(Context context ,String id) {
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        MAX_PUNTAJE_NIVEL_3 = prefs.getInt("max_dificil_"+id,0);
        return MAX_PUNTAJE_NIVEL_3;
    }

    public static void setPUNTAJE_MAX_NIVEL_DIFICIL(Context context,int PUNTAJE_MAX_NIVEL, String id) {
        GameTrivia.MAX_PUNTAJE_NIVEL_3 = PUNTAJE_MAX_NIVEL;
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("max_dificil_"+id, PUNTAJE_MAX_NIVEL);
        editor.commit();
    }

    // PUNTAJE
    public static int getPUNTAJE(Context context, String id) {
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        PUNTAJE = prefs.getInt("puntaje_"+id,0);
        return PUNTAJE;
    }

    public static void setPUNTAJE(Context context,int PUNTAJE,String id) {
        GameTrivia.PUNTAJE = PUNTAJE;
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("puntaje_"+id, PUNTAJE);
        editor.commit();
    }

    // NIVEL DESBLOQUEADO

    // NIVEL FACIL
    public static int getNIVEL_DESBLOQUEO_FACIL(Context context, String id) {
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        NIVEL_1_BLOQUEADO = prefs.getInt("nivel_facil_"+id,1);
        return NIVEL_1_BLOQUEADO;
    }

    public static void setNIVEL_DESBLOQUEO_FACIL(Context context,int DESBLOQUEADO, String id) {
        GameTrivia.NIVEL_1_BLOQUEADO = DESBLOQUEADO;
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("nivel_facil_"+id, DESBLOQUEADO);
        editor.commit();
    }

    // NIVEL MEDIO
    public static int getNIVEL_DESBLOQUEO_MEDIO(Context context, String id) {
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        NIVEL_2_BLOQUEADO= prefs.getInt("nivel_medio_"+id,0);
        return NIVEL_2_BLOQUEADO;
    }

    public static void setNIVEL_DESBLOQUEO_MEDIO(Context context,int DESBLOQUEADO, String id) {
        GameTrivia.NIVEL_2_BLOQUEADO = DESBLOQUEADO;
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("nivel_medio_"+id, DESBLOQUEADO);
        editor.commit();
    }

    // NIVEL DIFICIL
    public static int getNIVEL_DESBLOQUEO_DIFICIL(Context context, String id) {
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        NIVEL_3_BLOQUEADO = prefs.getInt("nivel_dificil_"+id,0);
        return NIVEL_3_BLOQUEADO;
    }

    public static void setNIVEL_DESBLOQUEO_DIFICIL(Context context,int DESBLOQUEADO, String id) {
        GameTrivia.NIVEL_3_BLOQUEADO = DESBLOQUEADO;
        SharedPreferences prefs = context.getSharedPreferences("trivia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("nivel_dificil_"+id, DESBLOQUEADO);
        editor.commit();
    }
}
