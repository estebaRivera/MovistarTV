package com.smartboxtv.movistartv.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Esteban- on 03-06-14.
 */
public class DataBaseTrivia extends SQLiteOpenHelper{

    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "nuncheetrivia.db";
    private String sqlCreate = "CREATE TABLE game_trivia (      id_trivia TEXT" +
                                                            ",  bloqueo_nivel_1 TEXT" +
                                                            ",  bloqueo_nivel_2 TEXT" +
                                                            ",  bloqueo_nivel_3 TEXT" +
                                                            ",  puntaje_max_nivel_1 TEXT" +
                                                            ",  puntaje_max_nivel_2 TEXT" +
                                                            ",  puntaje_max_nivel_3 TEXT" +
                                                            ",  vidas TEXT" +
                                                            ",  puntaje_actual TEXT" +
                                                            ",  game_over TEXT" +
                                                            ",  next_level TEXT" +
                                                            ",  nivel_actual TEXT)";

    public DataBaseTrivia(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, NOMBRE_BASEDATOS, factory, VERSION_BASEDATOS);
    }

    public DataBaseTrivia(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, NOMBRE_BASEDATOS, factory, VERSION_BASEDATOS, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //Se elimina la versión anterior de la tabla
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS game_trivia");

        //Se crea la nueva versión de la tabla
        sqLiteDatabase.execSQL(sqlCreate);
    }

    public void updateGame(String id, DataGameTrivia dataGameTrivia){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();

        //valores.put("id", id);
        valores.put("nivel_actual", dataGameTrivia.nivel);
        valores.put("vidas", dataGameTrivia.vidas);
        valores.put("puntaje_actual", dataGameTrivia.puntaje);

        String valor;
        String valor2;
        String valor3;

        if(dataGameTrivia.bloqueo_nivel_1){
            valor = "true";
        }
        else{
            valor = "false";
        }
        if(dataGameTrivia.bloqueo_nivel_2){
            valor2 = "true";
        }
        else{
            valor2 = "false";
        }
        if(dataGameTrivia.bloqueo_nivel_3){
            valor3 = "true";
        }
        else{
            valor3 = "false";
        }

        valores.put("bloqueo_nivel_1", valor);
        valores.put("bloqueo_nivel_2", valor2);
        valores.put("bloqueo_nivel_3", valor3);

        valores.put("puntaje_max_nivel_1", dataGameTrivia.puntaje_max_1);
        valores.put("puntaje_max_nivel_2", dataGameTrivia.puntaje_max_2);
        valores.put("puntaje_max_nivel_3", dataGameTrivia.puntaje_max_3);

        String game_over, next_level;

        if(dataGameTrivia.game_over){
            game_over = "true";
        }
        else{
            game_over = "false";
        }

        if(dataGameTrivia.next_level){
            next_level = "true";
        }
        else{
            next_level = "false";
        }

        valores.put("game_over", game_over);
        valores.put("next_level", next_level);
        db.update("game_trivia", valores, "id_trivia = '" + id+"'", null);
        db.close();
    }

    public void insertGameTrivia(String id){

        Log.e("Insert", "insertGame");
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put("id_trivia", id);
            valores.put("bloqueo_nivel_1", "true");
            valores.put("bloqueo_nivel_2", "false");
            valores.put("bloqueo_nivel_3", "false");
            valores.put("puntaje_max_nivel_1", "0");
            valores.put("puntaje_max_nivel_2", "0");
            valores.put("puntaje_max_nivel_3", "0");
            valores.put("vidas", "3");
            valores.put("puntaje_actual", "0");
            valores.put("nivel_actual", "1");
            db.insert("game_trivia", null, valores);
            Log.e("insert","valor "+id);
            db.close();
        }
    }

    public DataGameTrivia selectGame(String id) {

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"vidas", "puntaje_actual", "nivel_actual","puntaje_max_nivel_1","puntaje_max_nivel_2","puntaje_max_nivel_3","bloqueo_nivel_1"
                ,"bloqueo_nivel_2","bloqueo_nivel_3","game_over","next_level"};

        Cursor c = db.query("game_trivia", valores_recuperar,"id_trivia = '"+ id+"'",
                null, null, null, null, null);
        DataGameTrivia dataGameTrivia;

        Log.e("Select","valor "+id);

            c.moveToNext();
            if(c != null && c.getCount()>0 ){

                int vidas = Integer.parseInt(c.getString(0));
                int puntaje = Integer.parseInt(c.getString(1));
                int nivel = Integer.parseInt(c.getString(2));
                int puntaje_max_1 =  Integer.parseInt(c.getString(3));
                int puntaje_max_2 =  Integer.parseInt(c.getString(4));
                int puntaje_max_3 =  Integer.parseInt(c.getString(5));
                boolean bloqueo_nivel_1 = Boolean.parseBoolean(c.getString(6));
                boolean bloqueo_nivel_2 = Boolean.parseBoolean(c.getString(7));
                boolean bloqueo_nivel_3 = Boolean.parseBoolean(c.getString(8));
                boolean game_over = Boolean.parseBoolean(c.getString(9));
                boolean next_level = Boolean.parseBoolean(c.getString(10));

                dataGameTrivia = new DataGameTrivia(vidas,puntaje,nivel,puntaje_max_1,puntaje_max_2,puntaje_max_3,bloqueo_nivel_1,bloqueo_nivel_2,bloqueo_nivel_3,game_over, next_level);
                Log.e("get", c.getString(0));

                db.close();
                c.close();
                return dataGameTrivia;
            }
            else{
                db.close();
                c.close();
                return null;
            }
    }

    // Puntaje Maximo nivel fácil
    public int selectPuntajeMaxNivel1(String id){

        int puntaje = 0;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"puntaje_max_nivel_1"};

        Cursor c = db.query("game_trivia", valores_recuperar, "id_trivia = '"+ id+"'",
                null, null, null, null, null);

        c.moveToNext();
        if(c != null ){
            puntaje = Integer.parseInt( c.getString(0));
            return puntaje;
        }

       return -1;
    }

    // Puntaje Maximo nivel medio
    public int selectPuntajeMaxNivel2(String id){

        int puntaje = 0;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"puntaje_max_nivel_2"};

        Cursor c = db.query("game_trivia", valores_recuperar, "id_trivia = '"+ id+"'",
                null, null, null, null, null);

        c.moveToNext();
        if(c != null ){
            puntaje = Integer.parseInt( c.getString(0));
            return puntaje;
        }

        return -1;
    }

    // Puntaje Maximo nivel dificil
    public int selectPuntajeMaxNivel3(String id){

        int puntaje = 0;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"puntaje_max_nivel_3"};

        Cursor c = db.query("game_trivia", valores_recuperar, "id_trivia = '"+ id+"'",
                null, null, null, null, null);

        c.moveToNext();
        if(c != null ){
            puntaje = Integer.parseInt( c.getString(0));
            return puntaje;
        }

        return -1;
    }

    // Select Vidas
    public int selectVidas(String id){

        int vidas = 0;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"vidas"};

        Cursor c = db.query("game_trivia", valores_recuperar, "id_trivia = '"+ id+"'",
                null, null, null, null, null);

        c.moveToNext();
        if(c != null ){
            vidas = Integer.parseInt( c.getString(0));
            return vidas;
        }

        return -1;
    }
    // Select puntaje actual
    public int selectPuntaje(String id){

        int puntaje = 0;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"puntaje_actual"};

        Cursor c = db.query("game_trivia", valores_recuperar, "id_trivia = '"+ id+"'",
                null, null, null, null, null);

        c.moveToNext();
        if(c != null ){
            puntaje = Integer.parseInt( c.getString(0));
            return puntaje;
        }

        return -1;
    }
    public int selectNivel(String id){

        int nivel = 0;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"nivel_actual"};

        Cursor c = db.query("game_trivia", valores_recuperar, "id_trivia = '"+ id+"'",
                null, null, null, null, null);

        c.moveToNext();
        if(c != null ){
            nivel = Integer.parseInt( c.getString(0));
            return nivel;
        }

        return -1;
    }

    // Select niveles bloqueados
    public boolean selectBloqueoNivel1(String id){

        boolean bloqueo = false;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"bloqueo_nivel_1"};

        Cursor c = db.query("game_trivia", valores_recuperar, "id_trivia = '"+ id+"'",
                null, null, null, null, null);

        c.moveToNext();
        if(c != null ){
            bloqueo = Boolean.parseBoolean(c.getString(0));
            //return bloqueo;
        }

        return bloqueo;
    }

    public boolean selectBloqueoNivel2(String id){

        boolean bloqueo = false;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"bloqueo_nivel_2"};

        Cursor c = db.query("game_trivia", valores_recuperar, "id_trivia = '"+ id+"'",
                null, null, null, null, null);

        c.moveToNext();
        if(c != null ){
            bloqueo = Boolean.parseBoolean(c.getString(0));
            //return bloqueo;
        }

        return bloqueo;
    }

    public boolean selectBloqueoNivel3(String id){

        boolean bloqueo = false;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"bloqueo_nivel_3"};

        Cursor c = db.query("game_trivia", valores_recuperar, "id_trivia = '"+ id+"'",
                null, null, null, null, null);

        c.moveToNext();
        if(c != null ){
            bloqueo = Boolean.parseBoolean(c.getString(0));
            //return bloqueo;
        }
        return bloqueo;
    }

    public boolean isGameOver(String id){

        boolean bloqueo = false;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"game_over"};

        Cursor c = db.query("game_trivia", valores_recuperar, "id_trivia = '"+ id+"'",
                null, null, null, null, null);

        c.moveToNext();
        if(c != null ){
            bloqueo = Boolean.parseBoolean(c.getString(0));
            //return bloqueo;
        }
        return bloqueo;
    }

    public boolean isNextLevel(String id){

        boolean bloqueo = false;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"next_level"};

        Cursor c = db.query("game_trivia", valores_recuperar, "id_trivia = '"+ id+"'",
                null, null, null, null, null);

        c.moveToNext();
        if(c != null ){
            bloqueo = Boolean.parseBoolean(c.getString(0));
            //return bloqueo;
        }
        return bloqueo;
    }
}
