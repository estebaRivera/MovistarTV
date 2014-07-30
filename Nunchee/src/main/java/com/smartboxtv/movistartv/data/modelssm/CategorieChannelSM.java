package com.smartboxtv.movistartv.data.modelssm;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 16-06-14.
 */
public class CategorieChannelSM {

    public int id;
    public String name;
    public Language language;

    public CategorieChannelSM() {
    }

    @DataMember( member = "id_program_category")
    public int getId() {
        return id;
    }
    @DataMember( member = "id_program_category")
    public void setId(int id) {
        this.id = id;
    }
    @DataMember( member = "description")
    public String getName() {
        return name;
    }
    @DataMember( member = "description")
    public void setName(String name) {
        this.name = name;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
