package xyz.luisnglbrv.sigma.objects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MateriaHorario {
    private int id;
    private String grupo;
    private String nombre;
    private String profesor;
    private String edificio;
    private String salon;
    private String lunes;
    private String martes;
    private String miercoles;
    private String jueves;
    private String viernes;
    private String sabado;
    private static int uniqueId = 0;

    public MateriaHorario(String grupo, String nombre, String profesor, String edificio,
                          String salon, String lunes, String martes, String miercoles,
                          String jueves, String viernes, String sabado) {
        this.grupo = grupo;
        this.nombre = nombre;
        this.profesor = profesor;
        this.edificio = edificio;
        this.salon = salon;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.id = ++uniqueId;
    }

    public String getGrupo() {
        return this.grupo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String arg){
        this.nombre = arg;
    }

    public String getProfesor() {
        return this.profesor;
    }

    public String getEdificio() {
        return this.edificio;
    }

    public String getSalon() {
        return this.salon;
    }

    public String getLunes() {
        return this.lunes;
    }

    public String getMartes() {
        return this.martes;
    }

    public String getMiercoles() {
        return this.miercoles;
    }

    public String getJueves() {
        return this.jueves;
    }

    public String getViernes() {
        return this.viernes;
    }

    public String getSabado() {
        return this.sabado;
    }
}