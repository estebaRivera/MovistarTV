package com.smartboxtv.movistartv.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by Esteban- on 17-05-14.
 */
public  class ManagerAnimation {

    public static  void alpha(View v){

        ObjectAnimator animAlpha = ObjectAnimator.ofFloat(v, View.ALPHA, 0.8f);
        ObjectAnimator animAlpha2 = ObjectAnimator.ofFloat(v, View.ALPHA, 0.6f);
        ObjectAnimator animAlpha3 = ObjectAnimator.ofFloat(v, View.ALPHA, 0.4f);
        ObjectAnimator animAlpha4 = ObjectAnimator.ofFloat(v, View.ALPHA, 0.6f);
        ObjectAnimator animAlpha5 = ObjectAnimator.ofFloat(v, View.ALPHA, 0.8f);
        ObjectAnimator animAlpha6 = ObjectAnimator.ofFloat(v, View.ALPHA, 1f);

        AnimatorSet animEncuesta = new AnimatorSet();
        animEncuesta.playSequentially(animAlpha,animAlpha2,animAlpha3,animAlpha4,animAlpha5,animAlpha6);
        animEncuesta.setDuration(200);

        animEncuesta.start();
    }

    public static void anim(View v){

        float x = v.getX();
        ObjectAnimator moveToLef = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, x);
        ObjectAnimator moveToRight = ObjectAnimator.ofFloat(v, View.TRANSLATION_X,x+20);
        AnimatorSet animView = new AnimatorSet();

        animView.playSequentially(moveToLef,moveToRight);
        //animView.setDuration();

        animView.start();
    }

    public static  void visibilityRight(final View v){


        ObjectAnimator animScaleXmin = ObjectAnimator.ofFloat(v, View.SCALE_X, 0f);



        AnimatorSet animView = new AnimatorSet();
        animView.play(animScaleXmin);
        animView.setDuration(200);
        animView.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ObjectAnimator animScaleXmax = ObjectAnimator.ofFloat(v, View.SCALE_X, 1f);
                AnimatorSet anim = new AnimatorSet();
                anim.play(animScaleXmax);
                anim.setDuration(200);
                anim.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animView.start();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels, int border) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        final int borderSizePx = border;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) borderSizePx);
        canvas.drawRoundRect(rectF, rect.centerX(), rect.centerY(), paint);
        return output;
    }
    public static void moveToLeft(View v){


    }
    public static void moveToRight(View v){

        float x = v.getX();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(v, View.ALPHA, 0);
        ObjectAnimator moveToOrigen = ObjectAnimator.ofFloat(v, View.X, x - 129);
        ObjectAnimator moveToRight = ObjectAnimator.ofFloat(v, View.TRANSLATION_X,x);
        AnimatorSet animView = new AnimatorSet();

        //animView.playSequentially(alpha,moveToOrigen,moveToRight);
        animView.playSequentially(moveToOrigen,moveToRight);
        animView.setDuration(800);

        animView.start();
    }
    public static void transformerTranslation(View quieto, View mueve){

        quieto.setPivotX(0);
        quieto.setPivotY(0);

        float x1 = quieto.getX();
        float y1 = quieto.getY();

        Log.e("X ","--> "+x1);
        Log.e("Y ","--> "+y1);


        float anchoQuieto = quieto.getMeasuredWidth();
        float largoQuieto = quieto.getMeasuredHeight();

        float anchoMueve = mueve.getMeasuredWidth();
        float largoMueve = mueve.getMeasuredHeight();

        Log.e("anchoQuiero","--> "+anchoQuieto);
        Log.e("largoQuieto","--> "+largoQuieto);
        Log.e("anchoMueve","--> "+anchoMueve);
        Log.e("largoMueve","--> "+largoMueve);


        ObjectAnimator animTranslationX = ObjectAnimator.ofFloat(mueve, View.TRANSLATION_X, -(y1 +55+17)); // 657;
        ObjectAnimator animTranslationY = ObjectAnimator.ofFloat(mueve, View.TRANSLATION_Y, 164); // 164

        ObjectAnimator animTransformerWidth = ObjectAnimator.ofFloat(mueve, View.SCALE_X, porcentaje(anchoMueve,anchoQuieto));
        ObjectAnimator animTranslationHeight = ObjectAnimator.ofFloat(mueve, View.SCALE_Y, porcentaje(largoMueve, largoQuieto));

        Log.e("Porcentaje X","--> "+porcentaje(anchoMueve,anchoQuieto));
        Log.e("Porcentaje Y","--> "+porcentaje(largoMueve, largoQuieto));
        //ObjectAnimator animTransformerWidth = ObjectAnimator.ofFloat(mueve, View.SCALE_X, porcentaj() );
        //ObjectAnimator animTranslationHeight = ObjectAnimator.ofFloat(mueve, View.SCALE_Y, 0.169f);



        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1900);
        animatorSet.playTogether(animTranslationX, animTranslationY, animTransformerWidth, animTranslationHeight);
        //animatorSet.playTogether(animTranslationX, animTranslationY);
        //animatorSet.playTogether(animTransformerWidth, animTranslationHeight);
        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    public static void scaleY (View v){

        v.setPivotY(0);
        v.setVisibility(View.VISIBLE);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(v, View.SCALE_Y, 0,1);
        AnimatorSet animView = new AnimatorSet();

        animView.play(animatorY);
        animView.setDuration(600);

        animView.start();
    }

    public static void noScaleY (final View v){

        v.setPivotY(0);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(v, View.SCALE_Y, 1,0);
        AnimatorSet animView = new AnimatorSet();

        animView.play(animatorY);
        animView.setDuration(600);
        animView.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animView.start();
    }

    public static void scaleYList (List<View> list){

        for (View aList : list) {
            aList.setPivotY(0);
            aList.setVisibility(View.VISIBLE);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(aList, View.SCALE_Y, 0, 1);
            AnimatorSet animView = new AnimatorSet();

            animView.play(animatorY);
            animView.setDuration(200);
            animView.start();
        }
    }
    public static void noScaleYList (final List<View> list){

        for(int i = 0 ; i < list.size(); i++){
            list.get(i).setPivotY(0);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(list.get(i), View.SCALE_Y, 1,0);
            AnimatorSet animView = new AnimatorSet();

            animView.play(animatorY);
            animView.setDuration(200);
            final int finalI = i;
            animView.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    list.get(finalI).setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            animView.start();
        }
    }
    public static void fullScreen(View v, float x, float y){

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, View.SCALE_X, x);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(v, View.SCALE_Y, y);
        AnimatorSet animView = new AnimatorSet();

        animView.playTogether(animatorX,animatorY);
        animView.setDuration(500);

        animView.start();
    }

    public static void showControlBar(View v){

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, View.TRANSLATION_Y,0);
        AnimatorSet animView = new AnimatorSet();

        animView.play(animatorX);
        animView.setDuration(500);

        animView.start();
    }

    public static void hidenControlBar(View v){

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, View.TRANSLATION_Y,50);
        AnimatorSet animView = new AnimatorSet();

        animView.play(animatorX);
        animView.setDuration(500);

        animView.start();
    }

    public static void fade(View v, View v2, final View v3){

        ObjectAnimator animatorAlpha1 = ObjectAnimator.ofFloat(v, "alpha",0,1);
        ObjectAnimator animatorAlpha2 = ObjectAnimator.ofFloat(v2, View.ALPHA,0,1);
        ObjectAnimator animatorAlpha3 = ObjectAnimator.ofFloat(v3, View.ALPHA,0,0);

        AnimatorSet animView = new AnimatorSet();

        animView.playTogether(animatorAlpha1, animatorAlpha2, animatorAlpha3);
        animView.setDuration(500);
        animView.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) { 
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ObjectAnimator animatorAlpha3 = ObjectAnimator.ofFloat(v3, View.ALPHA,0,1);
                AnimatorSet animView2 = new AnimatorSet();
                animView2.play(animatorAlpha3);
                animView2.setDuration(800);
                animView2.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animView.start();
    }
    public void fadeOut(View v){

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, View.ALPHA,1,0);
        AnimatorSet animView = new AnimatorSet();

        animView.play(animatorX);
        animView.setDuration(300);
        animView.start();
    }

    public static void selection(final View v){
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(v, "alpha",  0.3f),
                ObjectAnimator.ofFloat(v, "scaleX", 1, 1.02f),
                ObjectAnimator.ofFloat(v, "scaleY", 1, 1.02f));
        set.setDuration(400);
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                AnimatorSet aux = new AnimatorSet();
                aux.playTogether(
                        ObjectAnimator.ofFloat(v, "alpha",  1f),
                        ObjectAnimator.ofFloat(v, "scaleX", 1.02f, 1f),
                        ObjectAnimator.ofFloat(v, "scaleY", 1.02f, 1f));
                aux.setDuration(400);
                aux.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    public static float porcentaje(float a, float b){

        return  (b / a);
    }
}
