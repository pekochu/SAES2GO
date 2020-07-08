package xyz.luisnglbrv.sigma.objects;

public class MateriaCalificaciones {
    private int id;
    private String grupo;
    private String nombre;
    private String primerParcial;
    private String segundoParcial;
    private String tercerParcial;
    private String calExtra;
    private String calFinal;
    private static int uniqueId = 0;

    public MateriaCalificaciones(String grupo, String nombre, String prim, String seg,
                                 String ter, String extra, String calFinal){
            this.grupo = grupo;
            this.nombre = nombre;
            this.primerParcial = prim;
            this.segundoParcial = seg;
            this.tercerParcial = ter;
            this.calExtra = extra;
            this.calFinal = calFinal;
            this.id = ++uniqueId;
    }

    public String getGrupo(){
        return this.grupo;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getPrimerParcial(){
        return this.primerParcial;
    }

    public String getSegundoParcial(){
        return this.segundoParcial;
    }

    public String getTercerParcial(){
        return this.tercerParcial;
    }

    public String getExtra(){
        return this.calExtra;
    }

    public String getCalFinal(){
        return this.calFinal;
    }
}
