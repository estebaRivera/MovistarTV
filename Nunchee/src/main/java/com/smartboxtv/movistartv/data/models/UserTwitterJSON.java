package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 18-04-14.
 */
public class UserTwitterJSON {

    private String texto;
    private UserTwitter Usuario;

    public UserTwitterJSON() {

    }

    @DataMember( member = "text" )
    public String getTexto() {
        return texto;
    }

    @DataMember( member = "text" )
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public UserTwitter getUsuario() {
        return Usuario;
    }
    public void setUsuario(UserTwitter usuario) {
        Usuario = usuario;
    }
}
