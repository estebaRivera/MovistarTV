package com.smartboxtv.movistartv.data.modelssm;

import com.smartboxtv.movistartv.services.DataMember;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 17-06-14.
 */
public class ChannelSM {

    public int favorite;
    public int id;
    public int category;
    public String urlImage;
    public String name;
    public List<ScheduleSM> listSchedule = new ArrayList<ScheduleSM>();

    public ChannelSM() {

    }
    @DataMember( member = "call_letter")
    public String getName() {
        return name;
    }
    @DataMember( member = "call_letter")
    public void setName(String name) {
        this.name = name;
    }
    @DataMember( member = "favorite")
    public int getFavorite() {
        return favorite;
    }
    @DataMember( member = "favorite")
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
    @DataMember( member = "id_channel")
    public int getId() {
        return id;
    }
    @DataMember( member = "id_channel")
    public void setId(int id) {
        this.id = id;
    }
    @DataMember( member = "category")
    public int getCategory() {
        return category;
    }
    @DataMember( member = "category")
    public void setCategory(int category) {
        this.category = category;
    }
    @DataMember( member = "image")
    public String getUrlImage() {
        return urlImage;
    }
    @DataMember( member = "image")
    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public List<ScheduleSM> getListProgram() {
        return listSchedule;
    }

    public void setListProgram(List<ScheduleSM> listProgram) {
        this.listSchedule = listProgram;
    }
}
