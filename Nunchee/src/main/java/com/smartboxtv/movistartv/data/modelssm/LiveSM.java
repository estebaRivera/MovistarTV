package com.smartboxtv.movistartv.data.modelssm;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Esteban- on 07-07-14.
 */
public class LiveSM implements Serializable{

    public String image;
    public String nombre;
    public String fecha;
    public Date date;
    public String url;
    public LiveStreamSchedule liveStreamSchedule;

    public LiveSM(String image, String nombre, String fecha, Date date, String url, LiveStreamSchedule live) {
        this.image = image;
        this.nombre = nombre;
        this.fecha = fecha;
        this.date = date;
        this.url = url;
        this.liveStreamSchedule = live;
    }

    public LiveSM() {

    }
}
