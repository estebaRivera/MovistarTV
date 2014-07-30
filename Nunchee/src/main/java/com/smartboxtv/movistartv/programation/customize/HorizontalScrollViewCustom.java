package com.smartboxtv.movistartv.programation.customize;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.smartboxtv.movistartv.programation.delegates.HorizontalScrollViewDelegate;

import java.util.Date;

/**
 * Created by Esteban- on 20-04-14.
 */
public class HorizontalScrollViewCustom extends HorizontalScrollView implements View.OnTouchListener {

    private int l;
    private int t;
    private int xScroll;
    private int MINIMO_DIA;
    private final int BLOCK = 360;//330; //300; //270; // 240

    private float coordenadaProgramaX;
    private float coordenadaProgramaXOld;
    private float inicio;
    public boolean moveLeft = true;
    public boolean moveRight = true;

    private Date ahora = new Date();
    private HorizontalScrollViewDelegate delegate;

    public HorizontalScrollViewCustom(android.content.Context context) {  super(context); }
    public HorizontalScrollViewCustom(android.content.Context context, android.util.AttributeSet attrs) { super(context,attrs); }
    public HorizontalScrollViewCustom(android.content.Context context, android.util.AttributeSet attrs, int defStyle) { super(context,attrs,defStyle); }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);
        this.setScrollX(xScroll);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        // TODO Auto-generated method stub
        if(delegate != null){
            delegate.onScrollChanged(l, t);
        }

        this.l = l;
        this.t = t;

        int oldl1 = oldl;
        int oldt1 = oldt;

        if(l < MINIMO_DIA){
            l = MINIMO_DIA;
            this.smoothScrollTo(MINIMO_DIA, 0);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch(ev.getAction()){

            case MotionEvent.ACTION_DOWN:   break;

            case MotionEvent.ACTION_MOVE:   this.coordenadaProgramaXOld = this.coordenadaProgramaX;
                                            this.coordenadaProgramaX = ev.getX();
                                            float contador = ev.getX();
                                            if(moveToLeft())
                                                return moveLeft;
                                            else
                                                return moveRight;
                                            //break;

            case MotionEvent.ACTION_UP:     contador = ev.getX();
                                            float diferencia = inicio - contador;

                                            if(diferencia >10 || diferencia < -10){

                                                if(moveToLeft() && moveLeft){

                                                    this.xScroll = l + BLOCK;
                                                    if(delegate != null){
                                                        delegate.onScrollEnd(l,t);
                                                    }
                                                    this.smoothScrollTo(l + BLOCK,0);
                                                }
                                                else if(!moveToLeft() && moveRight){
                                                    int MINIMO = 1440;
                                                    if(l > MINIMO)
                                                    {
                                                        if(delegate != null){
                                                            delegate.onScrollEnd(l,t);
                                                        }
                                                        this.xScroll = l - BLOCK;
                                                        this.smoothScrollTo(l - BLOCK,0);
                                                    }
                                                }
                                                if(moveToLeft())
                                                    return moveLeft;
                                                else
                                                    return moveRight;
                                            }
                                            break;

        }
        return true;
    }

    public boolean moveToLeft(){

        if(0 > coordenadaProgramaX - coordenadaProgramaXOld){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    public void setxScroll(int xScroll) {
        this.xScroll = xScroll;
    }

    public void setDel( HorizontalScrollViewDelegate delegado){
        this.delegate = delegado;
    }
}
