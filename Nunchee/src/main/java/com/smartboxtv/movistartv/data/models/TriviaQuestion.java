package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 18-04-14.
 */
public class TriviaQuestion implements Serializable {

    private List<TriviaAnswers> respuestas = new ArrayList<TriviaAnswers>();
    private int id;
    private int level;
    private String text;
    private String textAlt;
    private int type;

    public TriviaQuestion() {
    }

    public List<TriviaAnswers> getRespuestas() {
        return respuestas;
    }
    public void setRespuestas(List<TriviaAnswers> respuestas) {
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

    @DataMember( member = "Level" )
    public int getLevel() {
        return level;
    }
    @DataMember( member = "Level" )
    public void setLevel(int level) {
        this.level = level;
    }

    @DataMember( member = "Text" )
    public String getText() {
        return text;
    }
    @DataMember( member = "Text" )
    public void setText(String text) {
        this.text = text;
    }

    @DataMember( member = "TextAlt" )
    public String getTextAlt() {
        return textAlt;
    }
    @DataMember( member = "TextAlt" )
    public void setTextAlt(String textAlt) {
        this.textAlt = textAlt;
    }

    @DataMember( member = "Type" )
    public int getType() {
        return type;
    }
    @DataMember( member = "Type" )
    public void setType(int type) {
        this.type = type;
    }
}
