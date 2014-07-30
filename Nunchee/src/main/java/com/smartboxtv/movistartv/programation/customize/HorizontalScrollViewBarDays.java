package com.smartboxtv.movistartv.programation.customize;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import java.util.Date;

/**
 * Created by Esteban- on 02-05-14.
 */
public class HorizontalScrollViewBarDays extends HorizontalScrollView implements View.OnTouchListener{

    private OnScrollChangedListener mOnScrollChangedListener;
    private OnScrollChangedListener.ScrollState STATE = OnScrollChangedListener.ScrollState.NONE;
    private Date date;
    private int xScroll = -1;
    private int BLOCK = 6;
    private float y;
    private float x;
    private float old_x;
    private float old_y;
    private boolean isInitialPosition = false;

    public HorizontalScrollViewBarDays(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public HorizontalScrollViewBarDays(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public HorizontalScrollViewBarDays(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnTouchListener(this);
    }

    public void setmOnScrollChangedListener(OnScrollChangedListener mOnScrollChangedListener) {
        this.mOnScrollChangedListener = mOnScrollChangedListener;
    }
    public void setxScroll(int xScroll) {
        this.xScroll = xScroll;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(xScroll > 0){
            this.setScrollX(xScroll);
            xScroll = -1;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(mOnScrollChangedListener !=null){
            mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt, STATE);
            x =l;
            y = t;
            old_x = oldl;
            old_y = oldt;
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()){

            case MotionEvent.ACTION_DOWN :  STATE = OnScrollChangedListener.ScrollState.STARTED;
                                            if (mOnScrollChangedListener != null){
                                                mOnScrollChangedListener.onScrollStateChanged(this, OnScrollChangedListener.ScrollState.STARTED);
                                            }
                                            break;
            case MotionEvent.ACTION_UP :    STATE = OnScrollChangedListener.ScrollState.STOPPED;
                                            if (mOnScrollChangedListener != null){
                                                mOnScrollChangedListener.onScrollStateChanged(this, OnScrollChangedListener.ScrollState.STOPPED);
                                            }
                                            break;
            case MotionEvent.ACTION_MOVE :  STATE = OnScrollChangedListener.ScrollState.MOVING;
                                            if (mOnScrollChangedListener != null){
                                                old_x = x;
                                                old_y = y;
                                                x = motionEvent.getX();
                                                y = motionEvent.getY();
                                                mOnScrollChangedListener.onScrollStateChanged(this, OnScrollChangedListener.ScrollState.MOVING);
                                            }

                                            break;
        }
        return false;
    }
    @Override
    public void scrollTo(int x, int y) {
        if(x != 0 ){
            super.scrollTo(x, y);
        }
        else{
            super.scrollTo(x, y);
        }
    }
    public int getxScroll() {
        return xScroll;
    }

    public interface OnScrollChangedListener {

        enum ScrollState {
            NONE, STARTED, MOVING, STOPPED
        }
        /**
         * @param who
         * @param currHorizontal
         * @param currVertical
         * @param prevHorizontal
         * @param prevVertical
         */
        void onScrollChanged(HorizontalScrollViewBarDays who, int currHorizontal,
                             int currVertical, int prevHorizontal, int prevVertical, ScrollState newState);

        void onScrollStateChanged(HorizontalScrollViewBarDays who, ScrollState newState);
    }


}
