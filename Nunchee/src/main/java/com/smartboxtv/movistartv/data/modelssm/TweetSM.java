package com.smartboxtv.movistartv.data.modelssm;

import com.smartboxtv.movistartv.services.DataMember;

import java.util.Date;

/**
 * Created by Esteban- on 25-06-14.
 */
public class TweetSM {

    //public int id;
    public Date date;
    public String name;
    public String text;
    public String screenName;
    public String image;

    public TweetSM() {

    }
    /*@DataMember( member = "id" )
    public int getId() {
        return id;
    }
    @DataMember( member = "id" )
    public void setId(int id) {
        this.id = id;
    }

    @DataMember( member = "id" )
    public void setId(long id) {
        this.id = (int)id;
    }*/
    @DataMember( member = "created_at" )
    public Date getDate() {
        return date;
    }
    @DataMember( member = "created_at" )
    public void setDate(Date date) {
        this.date = date;
    }
    @DataMember( member = "name" )
    public String getName() {
        return name;
    }
    @DataMember( member = "name" )
    public void setName(String name) {
        this.name = name;
    }
    @DataMember( member = "text" )
    public String getText() {
        return text;
    }
    @DataMember( member = "text" )
    public void setText(String text) {
        this.text = text;
    }
    @DataMember( member = "screen_name" )
    public String getScreenName() {
        return screenName;
    }
    @DataMember( member = "screen_name" )
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
    @DataMember( member = "image" )
    public String getImage() {
        return image;
    }
    @DataMember( member = "image" )
    public void setImage(String image) {
        this.image = image;
    }
}
