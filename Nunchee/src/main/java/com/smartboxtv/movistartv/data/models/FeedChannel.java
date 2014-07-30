package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 18-04-14.
 */
public class FeedChannel {

    private String cCategory;
    private String cType;
    private String callLetter;
    private String channelImage;
    private String channelImage2;
    private String channelName;
    private String channelNumber;
    private String channelRatio;
    private String idChannel;

    public FeedChannel() {

    }
    @DataMember( member = "CCategory")
    public String getcCategory() {
        return cCategory;
    }

    @DataMember( member = "CCategory")
    public void setcCategory(String cCategory) {
        this.cCategory = cCategory;
    }

    @DataMember( member = "CType")
    public String getcType() {
        return cType;
    }

    @DataMember( member = "CType")
    public void setcType(String cType) {
        this.cType = cType;
    }

    @DataMember( member = "CallLetter")
    public String getCallLetter() {
        return callLetter;
    }

    @DataMember( member = "CallLetter")
    public void setCallLetter(String callLetter) {
        this.callLetter = callLetter;
    }

    @DataMember( member = "ChannelImage")
    public String getChannelImage() {
        return channelImage;
    }

    @DataMember( member = "ChannelImage")
    public void setChannelImage(String channelImage) {
        this.channelImage = channelImage;
    }

    @DataMember( member = "ChannelImage2")
    public String getChannelImage2() {
        return channelImage2;
    }

    @DataMember( member = "ChannelImage2")
    public void setChannelImage2(String channelImage2) {
        this.channelImage2 = channelImage2;
    }

    @DataMember( member = "ChannelName")
    public String getChannelName() {
        return channelName;
    }

    @DataMember( member = "ChannelName")
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @DataMember( member = "ChannelNumber")
    public String getChannelNumber() {
        return channelNumber;
    }
    @DataMember( member = "ChannelNumber")
    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }

    @DataMember( member = "ChannelRatio")
    public String getChannelRatio() {
        return channelRatio;
    }

    @DataMember( member = "ChannelRatio")
    public void setChannelRatio(String channelRatio) {
        this.channelRatio = channelRatio;
    }

    @DataMember( member = "IdChannel")
    public String getIdChannel() {
        return idChannel;
    }

    @DataMember( member = "IdChannel")
    public void setIdChannel(String idChannel) {
        this.idChannel = idChannel;
    }
}
