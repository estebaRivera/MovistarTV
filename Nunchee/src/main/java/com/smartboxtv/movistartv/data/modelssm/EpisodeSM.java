package com.smartboxtv.movistartv.data.modelssm;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 17-06-14.
 */
public class EpisodeSM {

    public int id;
    public String name;
    public String description;

    public EpisodeSM() {

    }
    @DataMember( member = "id_episode" )
    public int getId() {
        return id;
    }
    @DataMember( member = "id_episode" )
    public void setId(int id) {
        this.id = id;
    }
    @DataMember( member = "name" )
    public String getName() {
        return name;
    }
    @DataMember( member = "name" )
    public void setName(String name) {
        this.name = name;
    }
    @DataMember( member = "description" )
    public String getDescription() {
        return description;
    }
    @DataMember( member = "description" )
    public void setDescription(String description) {
        this.description = description;
    }
}
