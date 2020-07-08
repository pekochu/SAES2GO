package xyz.luisnglbrv.sigma.db;

import android.provider.BaseColumns;

/**
 * Created by LuisAngel on 06/01/17.
 */

public class Horario_Script {
    /*
    Etiqueta para Depuración
     */
    private static final String TAG = Horario_Script.class.getSimpleName();

    // Metainformación de la base de datos
    public static final String ENTRADA_TABLE_NAME = "t_dias";
    public static final String STRING_TYPE = "TEXT";
    public static final String INT_TYPE = "INTEGER";

    // Campos de la tabla entrada
    public static class ColumnEntradas {
        public static final String ID = BaseColumns._ID;
        public static final String GRUPO = "grupo";
        public static final String MATERIA = "materia";
        public static final String PROFESOR = "profesor";
        public static final String EDIFICIO = "edificio";
        public static final String SALON = "salon";
        public static final String LUNES = "lunes";
        public static final String MARTES = "martes";
        public static final String MIERCOLES = "miercoles";
        public static final String JUEVES = "jueves";
        public static final String VIERNES = "viernes";
        public static final String SABADO = "sabado";
    }

    // Comando CREATE para la tabla ENTRADA
    public static final String CREAR_ENTRADA =
            "CREATE TABLE " + ENTRADA_TABLE_NAME + "(" +
                    ColumnEntradas.ID + " " + INT_TYPE + " primary key autoincrement," +
                    ColumnEntradas.GRUPO + " " + STRING_TYPE + " not null," +
                    ColumnEntradas.MATERIA + " " + STRING_TYPE + "," +
                    ColumnEntradas.PROFESOR + " " + STRING_TYPE + "," +
                    ColumnEntradas.EDIFICIO + " " + STRING_TYPE + "," +
                    ColumnEntradas.SALON + " " + STRING_TYPE + "," +
                    ColumnEntradas.LUNES + " " + STRING_TYPE + "," +
                    ColumnEntradas.MARTES + " " + STRING_TYPE + "," +
                    ColumnEntradas.MIERCOLES + " " + STRING_TYPE + "," +
                    ColumnEntradas.JUEVES + " " + STRING_TYPE + "," +
                    ColumnEntradas.VIERNES + " " + STRING_TYPE + "," +
                    ColumnEntradas.SABADO + " " + STRING_TYPE +")";
}
