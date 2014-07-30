package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

import java.util.Date;

/**
 * Created by Esteban- on 18-04-14.
 */
public class TrendingChannel {

    private Date fechaInicio;
    private Date fechaFin;

    private String idChannel;
    private String idPrograma;
    private String fotoCanal;
    private String nombreCanal;
    private String nombrePrograma;
    private String numeroCanal;
    private String posicion;

    public TrendingChannel() {

    }
    @DataMember( member = "StartProgram" )
    public Date getFechaInicio() {
        return fechaInicio;
    }
    @DataMember( member = "StartProgram" )
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    @DataMember( member = "EndProgram" )
    public Date getFechaFin() {
        return fechaFin;
    }
    @DataMember( member = "EndProgram" )
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @DataMember( member = "IdChannel" )
    public String getIdChannel() {
        return idChannel;
    }
    @DataMember( member = "IdChannel" )
    public void setIdChannel(String idChannel) {
        this.idChannel = idChannel;
    }

    @DataMember( member = "IdProgram" )
    public String getIdPrograma() {
        return idPrograma;
    }
    @DataMember( member = "IdProgram" )
    public void setIdPrograma(String idPrograma) {
        this.idPrograma = idPrograma;
    }

    @DataMember( member = "LogoUrl" )
    public String getFotoCanal() {
        return fotoCanal;
    }
    @DataMember( member = "LogoUrl" )
    public void setFotoCanal(String fotoCanal) {
        this.fotoCanal = fotoCanal;
    }

    @DataMember( member = "NameChannel" )
    public String getNombreCanal() {
        return nombreCanal;
    }
    @DataMember( member = "NameChannel" )
    public void setNombreCanal(String nombreCanal) {
        this.nombreCanal = nombreCanal;
    }

    @DataMember( member = "NameProgram" )
    public String getNombrePrograma() {
        return nombrePrograma;
    }
    @DataMember( member = "NameProgram" )
    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    @DataMember( member = "NumberChannel" )
    public String getNumeroCanal() {
        return numeroCanal;
    }
    @DataMember( member = "NumberChannel" )
    public void setNumeroCanal(String numeroCanal) {
        this.numeroCanal = numeroCanal;
    }

    @DataMember( member = "Position" )
    public String getPosicion() {
        return posicion;
    }
    @DataMember( member = "Position" )
    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

}
