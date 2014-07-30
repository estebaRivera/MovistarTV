package com.smartboxtv.movistartv.data.modelssm.datacategory;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 20-06-14.
 */
public class ChannelCategorySM {

    public int idChannel;
    public String numberChannel;
    public String urlImage;
    public String callLetter;

    public ChannelCategorySM() {

    }
    @DataMember( member = "id_channel")
    public int getIdChannel() {
        return idChannel;
    }
    @DataMember( member = "id_channel")
    public void setIdChannel(int idChannel) {
        this.idChannel = idChannel;
    }
    @DataMember( member = "number")
    public String getNumberChannel() {
        return numberChannel;
    }
    @DataMember( member = "number")
    public void setNumberChannel(String numberChannel) {
        this.numberChannel = numberChannel;
    }
    @DataMember( member = "image")
    public String getUrlImage() {
        return urlImage;
    }
    @DataMember( member = "image")
    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
    @DataMember( member = "call_letter")
    public String getCallLetter() {
        return callLetter;
    }
    @DataMember( member = "call_letter")
    public void setCallLetter(String callLetter) {
        this.callLetter = callLetter;
    }
}
