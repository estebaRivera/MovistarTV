package com.smartboxtv.movistartv.programation.preview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import com.smartboxtv.movistartv.data.preference.UserPreference;

/**
 * Created by Esteban- on 06-05-14.
 */
public class AnimationCustom {

    //private ViewObject trivia,accion,encuesta,header,tw,check,check_;

    private float unidadAncho;

    private float unidadAlto;

    private final float HEADER_ALTO = 270;

    private final float HEADER_ANCHO = 700;

    private final float ACCION_ALTO = 240;

    private final float ACCION_ANCHO = 200;

    private final float TW_ALTO = 240;

    private final float TW_ANCHO = 496;

    private final float TRIVIA_ALTO = 90;

    private final float TRIVIA_ANCHO = 348;

    private final float ENCUESTA_ALTO = 90;

    private final float ENCUESTA_ANCHO = 348;

    private Animation headerInicio;
    private Animation accionInicio;


    public AnimationCustom(Context c) {

        unidadAlto = (UserPreference.getHEIGTH_SCREEN(c)/10);

        unidadAncho = (UserPreference.getWIDTH_SCREEN(c)/10);

        /*Animation headerFin = AnimationUtils.loadAnimation(c, R.anim.preview_header);
        Animation accionFin = AnimationUtils.loadAnimation(c, R.anim.preview_accion);*/

    }

    public void mueveTrivia(View viewTrivia, View viewEncuesta, View viewTw , View viewHeader, View viewSugeridos, View viewAccion, View buttonCheck, View buttonCheck_){

        // x,y,ancho ,alto

        Log.e("unidad ancho", "" + unidadAncho);

        Log.e("unidad alto",""+unidadAlto);

        /*header = new ViewObject(0,0,1,1);//0.6628f,0.7241f);
        trivia = new ViewObject(700,0,1,1);
        accion = new ViewObject(0,212,1,1);
        tw = new ViewObject(0,494,1,1);
        encuesta = new ViewObject(0,586,1,1);
        check = new ViewObject(0,0,1,1); // 110 x 110*/

        //check_ = new ViewObject(0,0,1,1); // 88x 60


        viewSugeridos.setVisibility(View.GONE);
        viewAccion.setVisibility(View.GONE);
        viewEncuesta.setVisibility(View.GONE);
        viewHeader.setVisibility(View.GONE);
        viewTw.setVisibility(View.GONE);
        viewTrivia.setVisibility(View.GONE);
        buttonCheck.setVisibility(View.GONE);
        buttonCheck_.setVisibility(View.GONE);

 /*     ObjectAnimator animaTriviaX = ObjectAnimator.ofFloat(viewTrivia,View.X,trivia.getX());
        ObjectAnimator animaTriviaY = ObjectAnimator.ofFloat(viewTrivia,View.Y,trivia.getY());
        ObjectAnimator animaTriviaAncho = ObjectAnimator.ofFloat(viewTrivia,View.SCALE_X,trivia.getAncho());
        ObjectAnimator animaTriviaAlto = ObjectAnimator.ofFloat(viewTrivia,View.SCALE_Y,trivia.getAlto());


        ObjectAnimator animaEncuestaX = ObjectAnimator.ofFloat(viewEncuesta, View.X, encuesta.getX());
        ObjectAnimator animaEncuestaY = ObjectAnimator.ofFloat(viewEncuesta, View.Y, encuesta.getY());
        ObjectAnimator animaEncuestaAncho = ObjectAnimator.ofFloat(viewEncuesta,View.SCALE_X,encuesta.getAncho());
        ObjectAnimator animaEncuestaAlto = ObjectAnimator.ofFloat(viewEncuesta,View.SCALE_Y,encuesta.getAlto());

        ObjectAnimator animaTwX = ObjectAnimator.ofFloat(viewTw,View.X,tw.getX());
        ObjectAnimator animaTwY = ObjectAnimator.ofFloat(viewTw,View.Y,tw.getY());
        ObjectAnimator animaTwAncho = ObjectAnimator.ofFloat(viewTw,View.SCALE_X,tw.getAncho());
        ObjectAnimator animaTwAlto = ObjectAnimator.ofFloat(viewTw,View.SCALE_Y,tw.getAlto());

        ObjectAnimator animaHeaderX = ObjectAnimator.ofFloat(viewHeader,View.X,header.getX());
        ObjectAnimator animaHeaderY = ObjectAnimator.ofFloat(viewHeader,View.Y,header.getY());

        ObjectAnimator animaAccionX = ObjectAnimator.ofFloat(viewAccion,View.X, accion.getX());
        ObjectAnimator animaAccionY = ObjectAnimator.ofFloat(viewAccion, View.Y, accion.getY());
        //ObjectAnimator animaAccionAncho = ObjectAnimator.ofFloat(viewAccion, View.SCALE_X, accion.getAncho());
        //ObjectAnimator animaAccionAlto = ObjectAnimator.ofFloat(viewAccion, View.SCALE_Y, accion.getAlto());

        ObjectAnimator animaCheckX = ObjectAnimator.ofFloat(buttonCheck, View.X, check.getX());
        ObjectAnimator animaCheckY = ObjectAnimator.ofFloat(buttonCheck, View.Y, check.getY());
        ObjectAnimator animaCheckAncho = ObjectAnimator.ofFloat(buttonCheck, View.SCALE_Y, check.getAncho());
        ObjectAnimator animaCheckAlto = ObjectAnimator.ofFloat(buttonCheck, View.SCALE_Y, check.getAlto());

        ObjectAnimator animaCheck_X = ObjectAnimator.ofFloat(buttonCheck_, View.X, check_.getX());
        ObjectAnimator animaCheck_Y = ObjectAnimator.ofFloat(buttonCheck_, View.Y, check_.getY());
        ObjectAnimator animaCheck_Ancho = ObjectAnimator.ofFloat(buttonCheck_, View.SCALE_X, check_.getAncho());
        ObjectAnimator animaCheck_Alto = ObjectAnimator.ofFloat(buttonCheck_, View.SCALE_Y, check_.getAlto());

        /*AnimatorSet animTrivia = new AnimatorSet();
        animTrivia.playTogether(animaTriviaX,animaTriviaY,animaTriviaAlto,animaTriviaAncho);
        animTrivia.setDuration(800);
        animTrivia.start();

        AnimatorSet animEncuesta = new AnimatorSet();
        animEncuesta.playTogether(animaEncuestaX,animaEncuestaY,animaEncuestaAlto,animaEncuestaAncho);
        animEncuesta.setDuration(800);
        animEncuesta.start();

        AnimatorSet animTw = new AnimatorSet();
        animTw.playTogether(animaTwX,animaTwY,animaTwAlto,animaTwAncho);
        animTw.setDuration(800);
        animTw.start();*/

       /* AnimatorSet animHeader = new AnimatorSet();
        animHeader.playTogether(animaHeaderX,animaHeaderY);
        animHeader.setDuration(800);
        animHeader.start();
        viewHeader.startAnimation(headerFin);

        AnimatorSet animAccion= new AnimatorSet();
        animAccion.playTogether(animaAccionX,animaAccionY);
        animAccion.setDuration(800);
        animAccion.start();
        viewAccion.startAnimation(accionFin);

        /*AnimatorSet animCheck= new AnimatorSet();
        animCheck.playTogether(animaCheckX,animaCheckY,animaCheckAlto,animaCheckAncho);
        animCheck.setDuration(800);
        animCheck.start();

        AnimatorSet animCheck_= new AnimatorSet();
        animCheck_.playTogether(animaCheck_X,animaCheck_Y,animaCheck_Alto,animaCheck_Ancho);
        animCheck_.setDuration(800);
        animCheck_.start();*/

    }

    public void rotacion (View view){

        ObjectAnimator animacionRotacion = ObjectAnimator.ofFloat(view,View.ROTATION_Y, (360*4));

        //ObjectAnimator animacionRotacion2 = ObjectAnimator.ofFloat(view2,View.ROTATION_Y, (360*4));

        AnimatorSet animacion = new AnimatorSet();

        //animacion.playTogether(animacionRotacion,animacionRotacion2);

        animacion.play(animacionRotacion);

        animacion.setDuration(6200);

        animacion.start();

    }

    public void rotacionSugerencias (View v1, View v2){

        ObjectAnimator animacionRotacion = ObjectAnimator.ofFloat(v1,View.ROTATION_Y, (360));

        ObjectAnimator animacionRotacion2 = ObjectAnimator.ofFloat(v2,View.ROTATION_Y, (360));

        AnimatorSet animacion = new AnimatorSet();

        animacion.playTogether(animacionRotacion,animacionRotacion2);

        animacion.setDuration(1500);

        animacion.start();

    }
    public void desapareceVistas(View viewTrivia, View viewEncuesta, View viewTw , View viewHeader, View viewSugeridos, View viewAccion, View buttonCheck, View buttonCheck_){

        viewSugeridos.setVisibility(View.GONE);
        viewAccion.setVisibility(View.GONE);
        viewEncuesta.setVisibility(View.GONE);
        viewHeader.setVisibility(View.GONE);
        viewTw.setVisibility(View.GONE);
        viewTrivia.setVisibility(View.GONE);
        buttonCheck.setVisibility(View.GONE);
        buttonCheck_.setVisibility(View.GONE);
    }
    public void mueveEncuesta(View viewTrivia, View viewEncuesta, View viewTw , View viewHeader, View viewSugeridos, View viewAccion, View buttonCheck, View buttonCheck_){

        // x,y,ancho ,alto

        Log.e("unidad ancho",""+unidadAncho);

        Log.e("unidad alto",""+unidadAlto);

        /*header = new ViewObject(0,0,1,1);//0.6628f,0.7241f);
        trivia = new ViewObject(700,0,1,1);
        accion = new ViewObject(0,212,1,1);
        tw = new ViewObject(0,494,1,1);
        encuesta = new ViewObject(0,586,1,1);
        check = new ViewObject(0,0,1,1); // 110 x 110
        check_ = new ViewObject(0,0,1,1); // 88x 60*/


        viewSugeridos.setVisibility(View.GONE);
        viewAccion.setVisibility(View.GONE);
        viewEncuesta.setVisibility(View.GONE);
        viewHeader.setVisibility(View.GONE);
        viewTw.setVisibility(View.GONE);
        viewTrivia.setVisibility(View.GONE);
        buttonCheck.setVisibility(View.GONE);
        buttonCheck_.setVisibility(View.GONE);

 /*       ObjectAnimator animaTriviaX = ObjectAnimator.ofFloat(viewTrivia,View.X,trivia.getX());
        ObjectAnimator animaTriviaY = ObjectAnimator.ofFloat(viewTrivia,View.Y,trivia.getY());
        ObjectAnimator animaTriviaAncho = ObjectAnimator.ofFloat(viewTrivia,View.SCALE_X,trivia.getAncho());
        ObjectAnimator animaTriviaAlto = ObjectAnimator.ofFloat(viewTrivia,View.SCALE_Y,trivia.getAlto());


        ObjectAnimator animaEncuestaX = ObjectAnimator.ofFloat(viewEncuesta, View.X, encuesta.getX());
        ObjectAnimator animaEncuestaY = ObjectAnimator.ofFloat(viewEncuesta, View.Y, encuesta.getY());
        ObjectAnimator animaEncuestaAncho = ObjectAnimator.ofFloat(viewEncuesta,View.SCALE_X,encuesta.getAncho());
        ObjectAnimator animaEncuestaAlto = ObjectAnimator.ofFloat(viewEncuesta,View.SCALE_Y,encuesta.getAlto());

        ObjectAnimator animaTwX = ObjectAnimator.ofFloat(viewTw,View.X,tw.getX());
        ObjectAnimator animaTwY = ObjectAnimator.ofFloat(viewTw,View.Y,tw.getY());
        ObjectAnimator animaTwAncho = ObjectAnimator.ofFloat(viewTw,View.SCALE_X,tw.getAncho());
        ObjectAnimator animaTwAlto = ObjectAnimator.ofFloat(viewTw,View.SCALE_Y,tw.getAlto());

        ObjectAnimator animaHeaderX = ObjectAnimator.ofFloat(viewHeader,View.X,header.getX());
        ObjectAnimator animaHeaderY = ObjectAnimator.ofFloat(viewHeader,View.Y,header.getY());

        ObjectAnimator animaAccionX = ObjectAnimator.ofFloat(viewAccion,View.X, accion.getX());
        ObjectAnimator animaAccionY = ObjectAnimator.ofFloat(viewAccion, View.Y, accion.getY());
        //ObjectAnimator animaAccionAncho = ObjectAnimator.ofFloat(viewAccion, View.SCALE_X, accion.getAncho());
        //ObjectAnimator animaAccionAlto = ObjectAnimator.ofFloat(viewAccion, View.SCALE_Y, accion.getAlto());

        ObjectAnimator animaCheckX = ObjectAnimator.ofFloat(buttonCheck, View.X, check.getX());
        ObjectAnimator animaCheckY = ObjectAnimator.ofFloat(buttonCheck, View.Y, check.getY());
        ObjectAnimator animaCheckAncho = ObjectAnimator.ofFloat(buttonCheck, View.SCALE_Y, check.getAncho());
        ObjectAnimator animaCheckAlto = ObjectAnimator.ofFloat(buttonCheck, View.SCALE_Y, check.getAlto());

        ObjectAnimator animaCheck_X = ObjectAnimator.ofFloat(buttonCheck_, View.X, check_.getX());
        ObjectAnimator animaCheck_Y = ObjectAnimator.ofFloat(buttonCheck_, View.Y, check_.getY());
        ObjectAnimator animaCheck_Ancho = ObjectAnimator.ofFloat(buttonCheck_, View.SCALE_X, check_.getAncho());
        ObjectAnimator animaCheck_Alto = ObjectAnimator.ofFloat(buttonCheck_, View.SCALE_Y, check_.getAlto());

        /*AnimatorSet animTrivia = new AnimatorSet();
        animTrivia.playTogether(animaTriviaX,animaTriviaY,animaTriviaAlto,animaTriviaAncho);
        animTrivia.setDuration(800);
        animTrivia.start();

        AnimatorSet animEncuesta = new AnimatorSet();
        animEncuesta.playTogether(animaEncuestaX,animaEncuestaY,animaEncuestaAlto,animaEncuestaAncho);
        animEncuesta.setDuration(800);
        animEncuesta.start();

        AnimatorSet animTw = new AnimatorSet();
        animTw.playTogether(animaTwX,animaTwY,animaTwAlto,animaTwAncho);
        animTw.setDuration(800);
        animTw.start();*/

       /* AnimatorSet animHeader = new AnimatorSet();
        animHeader.playTogether(animaHeaderX,animaHeaderY);
        animHeader.setDuration(800);
        animHeader.start();
        viewHeader.startAnimation(headerFin);

        AnimatorSet animAccion= new AnimatorSet();
        animAccion.playTogether(animaAccionX,animaAccionY);
        animAccion.setDuration(800);
        animAccion.start();
        viewAccion.startAnimation(accionFin);

        /*AnimatorSet animCheck= new AnimatorSet();
        animCheck.playTogether(animaCheckX,animaCheckY,animaCheckAlto,animaCheckAncho);
        animCheck.setDuration(800);
        animCheck.start();

        AnimatorSet animCheck_= new AnimatorSet();
        animCheck_.playTogether(animaCheck_X,animaCheck_Y,animaCheck_Alto,animaCheck_Ancho);
        animCheck_.setDuration(800);
        animCheck_.start();*/

    }

    public void animaEncusta(View contenedor, View segundoContenedor){

        //segundoContenedor.bringToFront();

        ObjectAnimator animacion = ObjectAnimator.ofFloat(contenedor,View.X,660);

        ObjectAnimator animacion2 = ObjectAnimator.ofFloat(segundoContenedor,View.X,0);

        AnimatorSet animHeader = new AnimatorSet();

        animHeader.playSequentially(animacion2,animacion);

        animHeader.setDuration(1100);

        animHeader.start();

    }

    public float tresSimple(float a,float x){

        float resultado = (x)/a;

        Log.e("Valor de x",""+x);
        Log.e("valor de a ",""+a);
        Log.e("resltado",""+resultado);

        return resultado;
    }
    /*
    * ObjectAnimator animation = ObjectAnimator.ofFloat(v, "rotationY", 0.0f, 360f);
		animation.setDuration(3600);
		animation.setRepeatCount(ObjectAnimator.INFINITE);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.start();*/
}
