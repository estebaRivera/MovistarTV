package com.smartboxtv.movistartv.data.modelssm.datafavorites;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 24-06-14.
 */
public class ChannelFavoriteSM {

    public int id;

    public String image;
    public String callLetter;

    public ChannelFavoriteSM() {
    }
    @DataMember( member = "id" )
    public int getId() {
        return id;
    }
    @DataMember( member = "id" )
    public void setId(int id) {
        this.id = id;
    }
    @DataMember( member = "image" )
    public String getImage() {
        return image;
    }
    @DataMember( member = "image" )
    public void setImage(String image) {
        this.image = image;
    }
    @DataMember( member = "callLetter" )
    public String getCallLetter() {
        return callLetter;
    }
    @DataMember( member = "callLetter" )
    public void setCallLetter(String callLetter) {
        this.callLetter = callLetter;
    }
}
