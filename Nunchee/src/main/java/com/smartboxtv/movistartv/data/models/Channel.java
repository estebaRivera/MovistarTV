package com.smartboxtv.movistartv.data.models;

import android.graphics.drawable.Drawable;

import com.smartboxtv.movistartv.services.DataMember;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Esteban- on 18-04-14.
 */
public class Channel implements Serializable {

    public List<Program> channelPrograms;
    public String channelCallLetter;
    public String channelImageURL;
    public String channelImageURLLight;
    public String channelName;
    public String channelNumber;
    public String channelID;
    public Drawable channelImage;
    public Date startDate;
    public Date endDate;
    public boolean par;
    public int position;

    public Channel() {

    }
    @DataMember( member = "CallLetter" )
    public String getChannelCallLetter() {
        return channelCallLetter;
    }

    @DataMember( member = "CallLetter" )
    public void setChannelCallLetter(String channelCallLetter) {
        this.channelCallLetter = channelCallLetter;
    }

    @DataMember( member = "ChannelImage2" )
    public String getChannelImageURL() {
        return channelImageURL;
    }

    @DataMember( member = "ChannelImage2" )
    public void setChannelImageURL(String channelImageURL) {

        if (channelImageURL != null) {
            this.channelImageURL = channelImageURL.replace( ".png","_nunchee.png");
        }
        else {
            this.channelImageURL = null;
        }
    }

    @DataMember( member = "ChannelImage" )
    public String getChannelImageURLLight() {
        return channelImageURLLight;
    }

    @DataMember( member = "ChannelImage" )
    public void setChannelImageURLLight(String channelImageURLLight) {

        if (channelImageURL != null) {
            this.channelImageURLLight = channelImageURLLight.replace( ".png","_nunchee.png");
        }
        else {

            this.channelImageURLLight = channelImageURLLight;
        }
    }

    public Drawable getChannelImage() {
        return channelImage;
    }


    public void setChannelImage(Drawable channelImage) {
        this.channelImage = channelImage;
    }

    @DataMember( member = "ChannelName" )
    public String getChannelName() {
        return channelName;
    }

    @DataMember( member = "ChannelName" )
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @DataMember( member = "ChannelNumber" )
    public String getChannelNumber() {
        return channelNumber;
    }

    @DataMember( member = "ChannelNumber" )
    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }

    @DataMember( member = "IdChannel" )
    public String getChannelID() {
        return channelID;
    }

    @DataMember( member = "IdChannel" )
    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }


    public List<Program> getChannelPrograms() {
        return channelPrograms;
    }


    public void setChannelPrograms(List<Program> channelPrograms) {
        this.channelPrograms = channelPrograms;
    }


    public Date getStartDate() {
        return startDate;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public Date getEndDate() {
        return endDate;
    }


    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
