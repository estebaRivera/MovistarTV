package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

import java.util.Date;
import java.util.List;

/**
 * Created by Esteban- on 18-04-14.
 */
public class FeedJSON {

    private FeedAction action;
    private FeedChannel channel;
    private FeedFriends friends;
    private String feedText;
    private String idPrograma;
    private boolean isFavorite;
    private List<Image> imagenes;
    private Date date;

    public FeedJSON() {

    }

    public FeedAction getAction() {
        return action;
    }

    public void setAction(FeedAction action) {
        this.action = action;
    }

    public FeedChannel getChannel() {
        return channel;
    }

    public void setChannel(FeedChannel channel) {
        this.channel = channel;
    }
    @DataMember( member = "Date")
    public Date getDate() {
        return date;
    }
    @DataMember( member = "Date")
    public void setDate(Date date) {
        this.date = date;
    }

    public String getFeedText() {
        return feedText;
    }
    @DataMember( member = "FeedText")
    public void setFeedText(String feedText) {
        this.feedText = feedText;
    }
    @DataMember( member = "FeedText")
    public FeedFriends getFriends() {
        return friends;
    }

    public void setFriends(FeedFriends friends) {
        this.friends = friends;
    }
    @DataMember( member = "IdPrograma")
    public String getIdPrograma() {
        return idPrograma;
    }
    @DataMember( member = "IdPrograma")
    public void setIdPrograma(String idPrograma) {
        this.idPrograma = idPrograma;
    }
    @DataMember( member = "IsFavorite")
    public boolean isFavorite() {
        return isFavorite;
    }
    @DataMember( member = "IsFavorite")
    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public List<Image> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Image> imagenes) {
        this.imagenes = imagenes;
    }

    public Image getImageWidthType(String width, String type){

        if(imagenes!= null){
            if(!imagenes.isEmpty()){
                for (Image imagene : imagenes) {
                    if (imagene.getImageType().equals(type) && imagene.getImageSize().equals(width)) {

                        return imagene;
                    }
                }
            }
            else{
                return null;
            }
        }
        else{
            return  null;
        }
        return null;
    }
}
