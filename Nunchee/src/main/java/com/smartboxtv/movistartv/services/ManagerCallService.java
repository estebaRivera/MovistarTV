package com.smartboxtv.movistartv.services;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Esteban- on 06-05-14.
 */
public class ManagerCallService {

    private final int INTENT_MAX = 4;
    private final long TIME_OUT = 10000;
    private int intent = 0;
    private TimerTask timerTask;
    private Timer timer;
    public OnStateChangedListener onStateChangedListener;
    private final Context context;
    /*public static int STATE_STOP = 1;
    public static int STATE_RESTART = 2;
    public static int STATE_START = 3;
    public static int STATE_TIME_OUT = 4;*/
    private OnStateChangedListener.State STATE = OnStateChangedListener.State.NONE;

    public ManagerCallService(Context context) {
        this.context = context;
    }
    public void stop(){
        if(onStateChangedListener != null){
            STATE =  OnStateChangedListener.State.STATE_STOP;
            onStateChangedListener.onStateChanged(STATE);
        }
        timerTask.cancel();
        timer.cancel();
    }
    public void start(){
        if(onStateChangedListener != null){
            STATE =  OnStateChangedListener.State.STATE_START;
            onStateChangedListener.onStateChanged(STATE);
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(intent < INTENT_MAX){
                    intent++;
                    if(onStateChangedListener != null){
                        STATE =  OnStateChangedListener.State.STATE_RESTART;
                        onStateChangedListener.onStateChanged(STATE);
                        //Toast.makeText(context,""+intent,Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if(onStateChangedListener != null){
                        STATE =  OnStateChangedListener.State.STATE_TIME_OUT;
                        onStateChangedListener.onStateChanged(STATE);

                    }
                }
            }
        };
        timer = new Timer();
        // dentro de 0 segundos avisame cada 23000 milisegundos
        timer.scheduleAtFixedRate(timerTask, 0, TIME_OUT);

    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }

    public interface OnStateChangedListener {

        /*public static int STATE_STOP = 1;
        public static int STATE_RESTART = 2;
        public static int STATE_START = 3;
        public static int STATE_TIME_OUT = 4;*/

        enum State {
            NONE, STATE_STOP, STATE_RESTART , STATE_START, STATE_TIME_OUT
        }

        /**
         * @param state
         */
        void onStateChanged(State state);

    }
}
