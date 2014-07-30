package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 18-04-14.
 */
public class CategorieChannel {

    public String channelCategoryName;
    public int channelCategoryRatio;
    public int idChannleCategory;
    public int index;

    public CategorieChannel() {

    }

    @DataMember( member = "ChannelCategoryName")
    public String getChannelCategoryName() {
        return channelCategoryName;
    }

    @DataMember( member = "ChannelCategoryName")
    public void setChannelCategoryName(String channelCategoryName) {
        this.channelCategoryName = channelCategoryName;
    }

    @DataMember( member = "IdChannelCategory")
    public int getIdChannleCategory() {
        return idChannleCategory;
    }

    @DataMember( member = "IdChannelCategory")
    public void setIdChannleCategory(int idChannleCategory) {
        this.idChannleCategory = idChannleCategory;
    }

    @DataMember( member = "IdChannelCategoryRatio")
    public int getChannelCategoryRatio() {
        return channelCategoryRatio;
    }

    @DataMember( member = "IdChannelCategoryRatio")
    public void setChannelCategoryRatio(int channelCategoryRatio) {
        this.channelCategoryRatio = channelCategoryRatio;
    }

    @DataMember( member = "Index")
    public int getIndex() {
        return index;
    }

    @DataMember( member = "Index")
    public void setIndex(int index) {
        this.index = index;
    }
}
