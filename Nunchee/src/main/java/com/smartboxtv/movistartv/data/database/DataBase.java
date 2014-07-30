package com.smartboxtv.movistartv.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 20-05-14.
 */
public class DataBase extends SQLiteOpenHelper {

    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "nunchee.db";
    private String sqlCreate = "CREATE TABLE reminder (id TEXT, begin_date TEXT, name TEXT, end_date TEXT, channel TEXT, image TEXT)";

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, NOMBRE_BASEDATOS, factory, VERSION_BASEDATOS);
    }

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, NOMBRE_BASEDATOS, factory, VERSION_BASEDATOS, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        //Se elimina la versión anterior de la tabla
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS reminder");

        //Se crea la nueva versión de la tabla
        sqLiteDatabase.execSQL(sqlCreate);
    }

    public void insertReminder(String id, String date, String programa){

        Log.e("Insert", "insertReminder");
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put("id", id);
            valores.put("fecha", date);
            valores.put("nombre",programa) ;
            db.insert("reminder", null, valores);
            db.close();
        }
    }

    public void updateReminder(String id, String date, String programa){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("id", id);
        valores.put("fecha", date);
        valores.put("nombre",programa) ;
        db.update("reminder", valores, "id =" + id, null);
        db.close();
    }
    public void deleteReminder(String  id) {
        SQLiteDatabase db = getWritableDatabase();
        if(!id.equals(""))
            db.delete("reminder", "id="+id, null);
        else
            db.delete("reminder", "", null);
        db.close();
    }

    public List<Reminder> getReminder() {

        SQLiteDatabase db = getReadableDatabase();
        Log.e("get", "getReminder");
        String[] valores_recuperar = {"id", "begin_date", "end_date","name","channel","image"};
        List<Reminder> reminderList = new ArrayList<Reminder>();
        Cursor c = db.query("reminder", valores_recuperar, "",
                null, null, null, null, null);
       for(int i = 0; i < c.getCount(); i ++){
        //while(c != null){
            //if(c != null) {
            c.moveToNext();
            if(c != null ){
                Reminder reminder = new Reminder(c.getString(0), c.getString(1), c.getString(2),c.getString(4),c.getString(3),c.getString(5));
                reminderList.add(reminder);
                Log.e("get", c.getString(0));
            }
              //  c.moveToNext();
            //}
       }

        db.close();
        c.close();
        return reminderList;
    }

    // Base de datos
       /* DataBase dataBase = new DataBase(this,"",null,1);
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Log.e("Inicio DB","Sin novedad");
        if(db != null){
            for(int i=1; i<=5; i++)
            {
                //Generamos los datos
                int codigo = i;
                String nombre = "Usuario" + i;
                String fecha = ""+new Date().getTime();
                //Insertamos los datos en la tabla Usuarios
                String query = "INSERT INTO reminder (id,fecha,nombre) " + "VALUES ('" + codigo + "',' "+fecha+"', '" + nombre +"')";
                Log.e("Query","zdf   "+query);
                db.execSQL(query);
            }

        }

        List<Reminder> reminderList=  dataBase.getReminder();
        for(int i = 0 ;i < reminderList.size();i++){
            Log.e("Reminder name",reminderList.get(i).getName());
            Log.e("Reminder id",reminderList.get(i).getId());
            Log.e("Reminder fecha",reminderList.get(i).getStrDate());
        }
        Log.e("Fin DB","Sin novedad");
        db.close();*/
    ////
}
