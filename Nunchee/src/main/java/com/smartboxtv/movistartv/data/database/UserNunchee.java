package com.smartboxtv.movistartv.data.database;

/**
 * Created by Esteban- on 04-06-14.
 */
public class UserNunchee {

    public String name;
    public String id;
    public String image;
    public boolean isFacebookActive;

    public UserNunchee() {
    }

    public UserNunchee(String name, String id, String image, boolean isFacebookActive) {
        this.name = name;
        this.id = id;
        this.image = image;
        this.isFacebookActive = isFacebookActive;
    }
}
