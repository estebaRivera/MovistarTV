package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 18-04-14.
 */
public class LoginData {

    private String idNunchee;

    public LoginData(){

    }

    @DataMember( member = "_id" )
    public String getIdNunchee() {
        return idNunchee;
    }

    @DataMember( member = "_id" )
    public void setIdNunchee(String idNunchee) {
        this.idNunchee = idNunchee;
    }
}
