package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

import java.io.Serializable;

/**
 * Created by Esteban- on 18-04-14.
 */
public class TriviaAnswers implements Serializable {

    private String respuesta;
    private boolean valor;
    private int votos;
    private int id;

    public TriviaAnswers() {

    }

    @DataMember( member = "Text" )
    public String getRespuesta() {
        return respuesta;
    }
    @DataMember( member = "Text" )
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    @DataMember( member = "State" )
    public boolean isValor() {
        return valor;
    }
    @DataMember( member = "State" )
    public void setValor(boolean valor) {
        this.valor = valor;
    }
}
