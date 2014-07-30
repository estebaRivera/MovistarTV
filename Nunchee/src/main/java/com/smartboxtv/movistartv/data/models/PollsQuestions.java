package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 18-04-14.
 */
public class PollsQuestions implements Serializable {

    private int id;
    private String text;
    private List<PollsAnswers> respuestas = new ArrayList<PollsAnswers>();

    public PollsQuestions() {

    }

    public List<PollsAnswers> getRespuestas() {
        return respuestas;
    }
    public void setRespuestas(List<PollsAnswers> respuestas) {
        this.respuestas = respuestas;
    }

    @DataMember( member = "Id" )
    public int getId() {
        return id;
    }
    @DataMember( member = "Id" )
    public void setId(int id) {
        this.id = id;
    }

    @DataMember( member = "Text" )
    public String getText() {
        return text;
    }
    @DataMember( member = "Text" )
    public void setText(String text) {
        this.text = text;
    }
}
