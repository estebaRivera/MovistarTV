package com.smartboxtv.movistartv.data.modelssm;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 17-06-14.
 */
public class ProgramSM {

    public int favorite;
    public int like;
    public int id;
    public String name;
    public String urlImage;
    public String description;

    public ProgramSM() {

    }
    @DataMember( member = "favorite" )
    public int getFavorite() {
        return favorite;
    }
    @DataMember( member = "favorite" )
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
    @DataMember( member = "like" )
    public int getLike() {
        return like;
    }
    @DataMember( member = "like" )
    public void setLike(int like) {
        this.like = like;
    }
    @DataMember( member = "image" )
    public String getUrlImage() {
        return urlImage;
    }
    @DataMember( member = "image" )
    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
    @DataMember( member = "id_program")
    public int getId() {
        return id;
    }
    @DataMember( member = "id_program")
    public void setId(int id) {
        this.id = id;
    }
    @DataMember( member = "name")
    public String getName() {
        return name;
    }
    @DataMember( member = "name")
    public void setName(String name) {
        this.name = name;
    }
    @DataMember( member = "description")
    public String getDescription() {
        return description;
    }
    @DataMember( member = "description")
    public void setDescription(String description) {
        this.description = description;
    }
}
