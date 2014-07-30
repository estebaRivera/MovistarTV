package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 18-04-14.
 */
public class UserFacebook {

    private String idUsuario;
    private String fotoPerfil;
    private String nombre;


    public UserFacebook() {

    }
    @DataMember(member = "id")
    public String getIdUsuario() {
        return idUsuario;
    }
    @DataMember(member = "id")
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    @DataMember(member = "picture")
    public String getFotoPerfil() {
        return fotoPerfil;
    }
    @DataMember(member = "picture")
    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    @DataMember(member = "name")
    public String getNombre() {
        return nombre;
    }
    @DataMember(member = "name")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
