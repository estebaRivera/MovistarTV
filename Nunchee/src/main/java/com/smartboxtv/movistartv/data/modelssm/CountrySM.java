package com.smartboxtv.movistartv.data.modelssm;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 16-06-14.
 */
public class CountrySM {

    private String name;
    private String initial;
    private int id;

    public CountrySM() {

    }

    @DataMember( member = "name")
    public String getName() {
        return name;
    }
    @DataMember( member = "name")
    public void setName(String name) {
        this.name = name;
    }
    @DataMember( member = "initials")
    public String getInitial() {
        return initial;
    }
    @DataMember( member = "initials")
    public void setInitial(String initial) {
        this.initial = initial;
    }
    @DataMember( member = "id_country")
    public int getId() {
        return id;
    }
    @DataMember( member = "id_country")
    public void setId(int id) {
        this.id = id;
    }
}
