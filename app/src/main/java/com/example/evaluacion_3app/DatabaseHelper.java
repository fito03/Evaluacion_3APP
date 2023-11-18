package com.example.evaluacion_3app;

import static android.provider.Settings.Secure.getString;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "INACAP";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "Incidentes";
    private static final String KEY_ID = "id";
    private static final String KEY_LABORATORIO = "laboratorio";
    private static final String KEY_RUT = "rut";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_DESCRIPCION = "descripcion";
    private static final String KEY_FECHA = "fecha";
    private static final String KEY_HORA = "hora";


    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_LABORATORIO + " TEXT, " + KEY_RUT + " TEXT, " + KEY_NOMBRE + " TEXT, " + KEY_DESCRIPCION + " TEXT, " + KEY_FECHA + " TEXT, " + KEY_HORA + " TEXT);";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        Log.d("table", CREATE_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" +TABLE_NAME + "'");
        onCreate(db);
    }

    public long addIncidenteDetail(String nombre, String rut, int laboratorio, String descripcion, String fecha, String hora){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, nombre);
        values.put(KEY_RUT, rut);
        values.put(KEY_LABORATORIO, laboratorio);
        values.put(KEY_DESCRIPCION, descripcion);
        values.put(KEY_FECHA, fecha);
        values.put(KEY_HORA, hora);

        long insert = db.insert(TABLE_NAME, null, values);

        return insert;
    }

    public ArrayList<Model> getAllIncidente(){
        ArrayList<Model> modelArrayList = new ArrayList<Model>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {
                Model model = new Model();
                model.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                model.setNombre(c.getString(c.getColumnIndex(KEY_NOMBRE)));
                model.setRut(c.getString(c.getColumnIndex(KEY_RUT)));
                model.setLaboratorio(c.getInt(c.getColumnIndex(KEY_LABORATORIO)));
                model.setDescripcion(c.getString(c.getColumnIndex(KEY_DESCRIPCION)));
                model.setFecha(c.getString(c.getColumnIndex(KEY_FECHA)));
                model.setHora(c.getString(c.getColumnIndex(KEY_HORA)));

                modelArrayList.add(model);
            } while (c.moveToNext());
        }
        return modelArrayList;
    }

    public int updateIncidente(int id, String nombre, String rut, int laboratorio, String descripcion){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, nombre);
        values.put(KEY_RUT, rut);
        values.put(KEY_LABORATORIO, laboratorio);
        values.put(KEY_DESCRIPCION, descripcion);

        return db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteIncidente(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
