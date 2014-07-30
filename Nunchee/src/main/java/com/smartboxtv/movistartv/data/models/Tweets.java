package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

import java.io.Serializable;

/**
 * Created by Esteban- on 18-04-14.
 */
public class Tweets implements Serializable {

    private String nombre;
    private String nombreUsuario;
    private String urlImagen;
    private String tw;
    private String fecha;

    public Tweets() {

    }

    public Tweets(String fecha, String nombre, String nombreUsuario, String urlImagen, String tw) {
        this.fecha = fecha;
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.urlImagen = urlImagen;
        this.tw = tw;
    }
    @DataMember( member = "FromUser" )
    public String getNombre() {
        return nombre;
    }
    @DataMember( member = "FromUser" )
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @DataMember( member = "FromUserName" )
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    @DataMember( member = "FromUserName" )
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    @DataMember( member = "ProfileImageUrl" )
    public String getUrlImagen() {
        return urlImagen;
    }
    @DataMember( member = "ProfileImageUrl" )
    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    @DataMember( member = "Text" )
    public String getTw() {
        return tw;
    }
    @DataMember( member = "Text" )
    public void setTw(String tw) {
        this.tw = tw;
    }

    @DataMember( member = "CreatedAt" )
    public String getFecha() {
        return fecha;
    }
    @DataMember( member = "CreatedAt" )
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
