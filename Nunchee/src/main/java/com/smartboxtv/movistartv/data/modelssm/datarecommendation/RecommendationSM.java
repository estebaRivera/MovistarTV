package com.smartboxtv.movistartv.data.modelssm.datarecommendation;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 05-08-14.
 */
public class RecommendationSM {

    public String image;
    public String name;
    public int id;
    public  EpisodeRecommendationSM episode;

    public RecommendationSM() {
    }

    @DataMember( member = "image")
    public String getImage() {
        return image;
    }
    @DataMember( member = "image")
    public void setImage(String image) {
        this.image = image;
    }
    @DataMember( member = "name")
    public String getName() {
        return name;
    }
    @DataMember( member = "name")
    public void setName(String name) {
        this.name = name;
    }
    @DataMember( member = "id")
    public int getId() {
        return id;
    }
    @DataMember( member = "id")
    public void setId(int id) {
        this.id = id;
    }
}
