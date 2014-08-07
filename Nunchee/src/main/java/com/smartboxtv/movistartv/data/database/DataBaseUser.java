package com.smartboxtv.movistartv.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Esteban- on 04-06-14.
 */
public class DataBaseUser extends SQLiteOpenHelper {

    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "nuncheeuser.db";
    private String sqlCreate = "CREATE TABLE user (      id TEXT" +
                                                        ",  name TEXT" +
                                                        ",  image TEXT" +
                                                        ",  facebook_active TEXT)";

    public DataBaseUser(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, NOMBRE_BASEDATOS, factory, VERSION_BASEDATOS);
    }

    public DataBaseUser(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, NOMBRE_BASEDATOS, factory, VERSION_BASEDATOS, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //Se elimina la versión anterior de la tabla
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user");

        //Se crea la nueva versión de la tabla
        sqLiteDatabase.execSQL(sqlCreate);

    }

    public UserNunchee select (String id){

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = { "image", "facebook_active","name"};

        Cursor c = db.query("user", valores_recuperar,"id = '"+ id+"'",
                null, null, null, null, null);
        DataGameTrivia dataGameTrivia;

        //Log.e("Select", "valor " + id);

        c.moveToNext();
        if(c != null && c.getCount()>0 ){

            String name = c.getString(2);
            String image = c.getString(0);
            boolean facebook_active = Boolean.parseBoolean(c.getString(1));

            UserNunchee user = new UserNunchee(name,id,image,facebook_active);
            Log.e("get", c.getString(2));

            db.close();
            c.close();
            return user;
        }
        else{
            db.close();
            c.close();
            return null;
        }
    }

    public void insertUser(String id, String image, String name){

        Log.e("Insert", "insertUser");
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put("id", id);
            valores.put("image", image);
            valores.put("name",  name);
            valores.put("facebook_active", "true");
            db.insert("user", null, valores);
            Log.e("insert","valor "+id);
            db.close();
        }
    }

    public boolean isFacebookActive(String id){

        boolean isActive = true;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"facebook_active"};

        Cursor c = db.query("user", valores_recuperar, "id = '"+ id+"'",
                null, null, null, null, null);

        c.moveToNext();
        if(c != null ){
            isActive = Boolean.parseBoolean( c.getString(0));
            return isActive;
        }

       return  isActive;
    }

    public void updateFacebookActive(String id, UserNunchee user){

            SQLiteDatabase db = getWritableDatabase();
            ContentValues valores = new ContentValues();

            valores.put("name", user.name);
            valores.put("image", user.image);

            String valor;
            if(user.isFacebookActive)
                valor = "true";
            else
                valor = "false";

            Log.e("facebook is ",valor);
            valores.put("facebook_active",valor );

            db.update("user", valores, "id = '" + id+"'", null);
            db.close();


    }

}
