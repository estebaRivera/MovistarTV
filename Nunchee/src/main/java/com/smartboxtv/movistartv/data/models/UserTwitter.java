package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 18-04-14.
 */
public class UserTwitter {

    private String urlImagen;
    private String usuario;
    private String nombreUsuario;


    public UserTwitter() {

    }

    @DataMember(member = "profile_image_url")
    public String getUrlImagen() {
        return urlImagen;
    }
    @DataMember(member = "profile_image_url")
    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    @DataMember( member = "name" )
    public String getUsuario() {
        return usuario;
    }
    @DataMember( member = "name" )
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @DataMember( member = "screen_name" )
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    @DataMember( member = "screen_name" )
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
