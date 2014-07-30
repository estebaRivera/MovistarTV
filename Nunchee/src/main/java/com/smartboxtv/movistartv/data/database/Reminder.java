package com.smartboxtv.movistartv.data.database;

/**
 * Created by Esteban- on 20-05-14.
 */
public class Reminder {

    public String id;
    public String strDate;
    public String endDate;
    public String idChannel;
    public String name;
    public String image;

    public Reminder() {

    }

    public Reminder(String id, String strDate, String endDate, String idChannel, String name,String image) {
        this.id = id;
        this.strDate = strDate;
        this.endDate = endDate;
        this.idChannel = idChannel;
        this.name = name;
        this.image = image;
    }

    public Reminder(String id, String strDate, String name) {
        this.id = id;
        this.strDate = strDate;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
