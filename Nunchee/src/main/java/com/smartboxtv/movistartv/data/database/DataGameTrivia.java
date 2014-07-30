package com.smartboxtv.movistartv.data.database;

/**
 * Created by Esteban- on 03-06-14.
 */
public class DataGameTrivia {

    public int vidas;
    public int puntaje;
    public int nivel;
    public int puntaje_max_1;
    public int puntaje_max_2;
    public int puntaje_max_3;
    public boolean bloqueo_nivel_1;
    public boolean bloqueo_nivel_2;
    public boolean bloqueo_nivel_3;

    public boolean game_over;
    public boolean next_level;


    public DataGameTrivia() {
    }

    public DataGameTrivia(int vidas, int puntaje, int nivel, int puntaje_max_1, int puntaje_max_2, int puntaje_max_3, boolean bloqueo_nivel_1, boolean bloqueo_nivel_2, boolean bloqueo_nivel_3) {
        this.vidas = vidas;
        this.puntaje = puntaje;
        this.nivel = nivel;
        this.puntaje_max_1 = puntaje_max_1;
        this.puntaje_max_2 = puntaje_max_2;
        this.puntaje_max_3 = puntaje_max_3;
        this.bloqueo_nivel_1 = bloqueo_nivel_1;
        this.bloqueo_nivel_2 = bloqueo_nivel_2;
        this.bloqueo_nivel_3 = bloqueo_nivel_3;
    }

    public DataGameTrivia(int vidas, int puntaje, int nivel, int puntaje_max_1, int puntaje_max_2, int puntaje_max_3, boolean bloqueo_nivel_1, boolean bloqueo_nivel_2, boolean bloqueo_nivel_3, boolean game_over, boolean next_level) {
        this.vidas = vidas;
        this.puntaje = puntaje;
        this.nivel = nivel;
        this.puntaje_max_1 = puntaje_max_1;
        this.puntaje_max_2 = puntaje_max_2;
        this.puntaje_max_3 = puntaje_max_3;
        this.bloqueo_nivel_1 = bloqueo_nivel_1;
        this.bloqueo_nivel_2 = bloqueo_nivel_2;
        this.bloqueo_nivel_3 = bloqueo_nivel_3;
        this.game_over = game_over;
        this.next_level = next_level;
    }
}
