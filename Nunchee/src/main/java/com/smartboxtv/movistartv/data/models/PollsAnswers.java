package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

import java.io.Serializable;

/**
 * Created by Esteban- on 18-04-14.
 */
public class PollsAnswers implements Serializable {

    private String text;
    private int idAnswer;
    private int votos;

    public PollsAnswers() {

    }

    @DataMember( member = "IdAnswer" )
    public int getIdAnswer() {
        return idAnswer;
    }
    @DataMember( member = "IdAnswer" )
    public void setIdAnswer(int idAnswer) {
        this.idAnswer = idAnswer;
    }

    @DataMember( member = "Text" )
    public String getText() {
        return text;
    }
    @DataMember( member = "Text" )
    public void setText(String text) {
        this.text = text;
    }

    @DataMember( member = "Votes" )
    public int getVotos() {
        return votos;
    }
    @DataMember( member = "Votes" )
    public void setVotos(int votos) {
        this.votos = votos;
    }
}
