package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 18-04-14.
 */
public class Polls implements Serializable{

    private int categoria;

    private List<PollsQuestions> preguntas = new ArrayList<PollsQuestions>();

    public Polls() {

    }
    @DataMember( member = "Category" )
    public int getCategoria() {
        return categoria;
    }
    @DataMember( member = "Category" )
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public List<PollsQuestions> getPreguntas() {
        return preguntas;
    }
    public void setPreguntas(List<PollsQuestions> preguntas) {
        this.preguntas = preguntas;
    }

}
