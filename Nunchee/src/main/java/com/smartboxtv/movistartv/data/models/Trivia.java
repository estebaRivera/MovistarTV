package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 18-04-14.
 */
public class Trivia implements Serializable {

    private int category;
    private List<Image> listaImagenes = new ArrayList<Image>();
    private List<String> phrases;
    private List<TriviaQuestion> preguntas = new ArrayList<TriviaQuestion>();

    public Trivia() {
    }

    @DataMember( member = "Category" )
    public int getCategory() {
        return category;
    }
    @DataMember( member = "Category" )
    public void setCategory(int category) {
        this.category = category;
    }

    public List<Image> getListaImagenes() {
        return listaImagenes;
    }
    public void setListaImagenes(List<Image> listaImagenes) {
        this.listaImagenes = listaImagenes;
    }

    public List<String> getPhrases() {
        return phrases;
    }
    public void setPhrases(List<String> phrases) {
        this.phrases = phrases;
    }

    public List<TriviaQuestion> getPreguntas() {
        return preguntas;
    }
    public void setPreguntas(List<TriviaQuestion> preguntas) {
        this.preguntas = preguntas;
    }

}
