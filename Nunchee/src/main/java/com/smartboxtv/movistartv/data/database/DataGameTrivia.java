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

    public boolean isShare;                     // Valor booleano que permite determinar si compartir el puntaje o no
    public boolean nivel_1_activo = false;      // Valores para determinar si hay un juego activo o pausadp
    public boolean nivel_2_activo = false;
    public boolean nivel_3_activo = false;


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

    public DataGameTrivia(int vidas, int puntaje, int nivel, int puntaje_max_1, int puntaje_max_2, int puntaje_max_3, boolean bloqueo_nivel_1, boolean bloqueo_nivel_2, boolean bloqueo_nivel_3,
                          boolean game_over, boolean next_level, boolean nivel_1_activo, boolean nivel_2_activo, boolean nivel_3_activo, boolean isShare) {
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

        this.nivel_1_activo = nivel_1_activo;
        this.nivel_2_activo = nivel_2_activo;
        this.nivel_3_activo = nivel_3_activo;

        this.isShare = isShare;
    }
}
