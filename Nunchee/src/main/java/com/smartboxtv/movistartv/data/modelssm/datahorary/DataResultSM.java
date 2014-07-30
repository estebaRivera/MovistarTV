package com.smartboxtv.movistartv.data.modelssm.datahorary;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 27-06-14.
 */
public class DataResultSM {

    public int idProgram;
    public String name;
    public String image;


    public DataResultSM() {
    }
    @DataMember( member = "idProgram")
    public int getIdProgram() {
        return idProgram;
    }
    @DataMember( member = "idProgram")
    public void setIdProgram(int idProgram) {
        this.idProgram = idProgram;
    }

    @DataMember( member = "originalTitle")
    public String getName() {
        return name;
    }
    @DataMember( member = "originalTitle")
    public void setName(String name) {
        this.name = name;
    }
    @DataMember( member = "image")
    public String getImage() {
        return image;
    }
    @DataMember( member = "image")
    public void setImage(String image) {
        this.image = image;
    }
}
