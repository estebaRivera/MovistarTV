package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 18-04-14.
 */
public class Recommendations implements Serializable{

    private int id;

    private List<Program> sameCategoria = new ArrayList<Program>();

    private List<String> sameActor = new ArrayList<String>();

    private List<String> sameDirector = new ArrayList<String>();

    private List<String> sameChannel = new ArrayList<String>();


    public Recommendations() {

    }

    @DataMember( member = "IdProgram" )
    public int getId() {
        return id;
    }

    @DataMember( member = "IdProgram" )
    public void setId(int id) {
        this.id = id;
    }

    public List<Program> getSameCategoria() {
        return sameCategoria;
    }

    public void setSameCategoria(List<Program> sameCategoria) {
        this.sameCategoria = sameCategoria;
    }

    public List<String> getSameActor() {
        return sameActor;
    }

    public void setSameActor(List<String> sameActor) {
        this.sameActor = sameActor;
    }

    public List<String> getSameDirector() {
        return sameDirector;
    }

    public void setSameDirector(List<String> sameDirector) {
        this.sameDirector = sameDirector;
    }

    public List<String> getSameChannel() {
        return sameChannel;
    }

    public void setSameChannel(List<String> sameChannel) {
        this.sameChannel = sameChannel;
    }
}
