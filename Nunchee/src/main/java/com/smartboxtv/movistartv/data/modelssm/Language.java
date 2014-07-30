package com.smartboxtv.movistartv.data.modelssm;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 16-06-14.
 */
public class Language {

    private int id;
    private String initial;
    private String description;

    public Language() {

    }

    @DataMember( member = "id_language")
    public int getId() {
        return id;
    }
    @DataMember( member = "id_language")
    public void setId(int id) {
        this.id = id;
    }
    @DataMember( member = "initial")
    public String getInitial() {
        return initial;
    }
    @DataMember( member = "initial")
    public void setInitial(String initial) {
        this.initial = initial;
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
