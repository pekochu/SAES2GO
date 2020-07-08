package xyz.luisnglbrv.sigma.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by LuisAngel on 06/01/17.
 */

public class Horario_OpenHelper extends SQLiteOpenHelper{
    private static final String TAG = "Horario_OpenHelper";
    private static final String NAME_DATABASE = "SAES_HORARIO.db";
    private static final int VERSION_DATABASE = 1;
    private static Horario_OpenHelper instancia;
    private SQLiteDatabase db_ = getWritableDatabase();

    private Horario_OpenHelper(Context context) {
        super(context, NAME_DATABASE, null, VERSION_DATABASE);
    }

    public static synchronized Horario_OpenHelper getInstance(Context context) {
        if (instancia == null) {
            instancia = new Horario_OpenHelper(context.getApplicationContext());
        }
        return instancia;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Horario_Script.CREAR_ENTRADA);
        Log.d(TAG, "Base de datos creada. #");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Horario_Script.ENTRADA_TABLE_NAME);
        onCreate(db);
        Log.d(TAG, "Base de datos tirada y creada nuevamente. #");
    }

    public Cursor obtenerHorario() {
        // Seleccionamos todas las filas de la tabla 't_dias'
        return getWritableDatabase().rawQuery(
                "select * from " + Horario_Script.ENTRADA_TABLE_NAME  + " ORDER BY lunes ASC, martes ASC, miercoles ASC, jueves ASC, viernes ASC, sabado ASC", null);
    }

    public void actualizar(){
        db_.execSQL("DROP TABLE IF EXISTS " + Horario_Script.ENTRADA_TABLE_NAME);
        db_.execSQL(Horario_Script.CREAR_ENTRADA);
        Log.d(TAG, "Tabla actualizada. #");
    }

    public void insertarHorario(String dia, String materia, String profesor, String edificio,
                                String salon, String lunes, String martes, String miercoles,
                                String jueves, String viernes, String sabado){
        ContentValues values = new ContentValues();
        values.put(Horario_Script.ColumnEntradas.GRUPO, dia);
        values.put(Horario_Script.ColumnEntradas.MATERIA, materia);
        values.put(Horario_Script.ColumnEntradas.PROFESOR, profesor);
        values.put(Horario_Script.ColumnEntradas.EDIFICIO, edificio);
        values.put(Horario_Script.ColumnEntradas.SALON, salon);
        values.put(Horario_Script.ColumnEntradas.LUNES, lunes);
        values.put(Horario_Script.ColumnEntradas.MARTES, martes);
        values.put(Horario_Script.ColumnEntradas.MIERCOLES, miercoles);
        values.put(Horario_Script.ColumnEntradas.JUEVES, jueves);
        values.put(Horario_Script.ColumnEntradas.VIERNES, viernes);
        values.put(Horario_Script.ColumnEntradas.SABADO, sabado);

        // Insertando el registro en la base de datos
        getWritableDatabase().insert(
                Horario_Script.ENTRADA_TABLE_NAME,
                null,
                values
        );
    }

    public void actualizarHorario(String edificio, String salon, String materia){
        ContentValues values = new ContentValues();
        values.put(Horario_Script.ColumnEntradas.EDIFICIO, edificio);
        values.put(Horario_Script.ColumnEntradas.SALON, salon);

        getWritableDatabase().update(Horario_Script.ENTRADA_TABLE_NAME, values,
                "MATERIA = \""+materia+"\"", null);
    }
}
