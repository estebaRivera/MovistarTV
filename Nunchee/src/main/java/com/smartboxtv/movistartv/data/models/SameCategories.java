package com.smartboxtv.movistartv.data.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 18-04-14.
 */
public class SameCategories {

    private List<Program> recomendaciones = new ArrayList<Program>();


    public SameCategories() {

    }

    public List<Program> getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(List<Program> recomendaciones) {
        this.recomendaciones = recomendaciones;
    }
}
