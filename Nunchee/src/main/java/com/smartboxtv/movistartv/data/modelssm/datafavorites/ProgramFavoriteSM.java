package com.smartboxtv.movistartv.data.modelssm.datafavorites;

import com.smartboxtv.movistartv.services.DataMember;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 24-06-14.
 */
public class ProgramFavoriteSM {

    public String image;
    public String name;
    public String description;
    public int id;
    public int like;
    public int favorite;

    public List<ScheduleFavoriteSM> schedule = new ArrayList<ScheduleFavoriteSM>();

    public ProgramFavoriteSM() {
    }
    @DataMember( member = "image" )
    public String getImage() {
        return image;
    }
    @DataMember( member = "image" )
    public void setImage(String image) {
        this.image = image;
    }
    @DataMember( member = "name" )
    public String getName() {
        return name;
    }
    @DataMember( member = "name" )
    public void setName(String name) {
        this.name = name;
    }

    public List<ScheduleFavoriteSM> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleFavoriteSM> schedule) {
        this.schedule = schedule;
    }


    @DataMember( member = "description" )
    public String getDescription() {
        return description;
    }
    @DataMember( member = "description" )
    public void setDescription(String description) {
        this.description = description;
    }
    @DataMember( member = "id" )
    public int getId() {
        return id;
    }
    @DataMember( member = "id" )
    public void setId(int id) {
        this.id = id;
    }
    @DataMember( member = "like" )
    public int getLike() {
        return like;
    }
    @DataMember( member = "like" )
    public void setLike(int like) {
        this.like = like;
    }
    @DataMember( member = "favorite" )
    public int getFavorite() {
        return favorite;
    }
    @DataMember( member = "favorite" )
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
