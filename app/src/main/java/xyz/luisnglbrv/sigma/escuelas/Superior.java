package xyz.luisnglbrv.sigma.escuelas;

import xyz.luisnglbrv.sigma.R;

/**
 * Created by LuisAngel on 21/12/16.
 */

public class Superior {
    private String nombre;
    private int idDrawable;
    private int alphaDrawable;

    public Superior(String nombre, int idDrawable, int alphaDrawable) {
        this.nombre = nombre;
        this.idDrawable = idDrawable;
        this.alphaDrawable = alphaDrawable;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public int getAlphaDrawable() {
        return alphaDrawable;
    }

    public int getId() {
        return nombre.hashCode();
    }

    public static Superior[] ITEMS = {
            new Superior("CICS UMA", R.drawable.cicsuma, R.drawable.cicsuma_a),
            new Superior("CICS UST", R.drawable.cicsust, R.drawable.cicsust_a),
            new Superior("ENCB", R.drawable.encb, R.drawable.encb_a),
            new Superior("ENMH", R.drawable.enmh, R.drawable.enmh_a),
            new Superior("ESCA UST", R.drawable.escaust, R.drawable.escaust_a),
            new Superior("ESCA TEP", R.drawable.escautep, R.drawable.escautep_a),
            new Superior("ESCOM", R.drawable.escom, R.drawable.escom_a),
            new Superior("ESE", R.drawable.ese, R.drawable.ese_a),
            new Superior("ESEO", R.drawable.eseo, R.drawable.eseo_a),
            new Superior("ESFM", R.drawable.esfm, R.drawable.esfm_a),
            new Superior("ESIA TEC", R.drawable.esiatec, R.drawable.esiatec_a),
            new Superior("ESIA TIC", R.drawable.esiatic, R.drawable.esiatic_a),
            new Superior("ESIA ZAC", R.drawable.esiaz, R.drawable.esiaz_a),
            new Superior("ESIME AZC", R.drawable.esimeazc, R.drawable.esimeazc_a),
            new Superior("ESIME CUL", R.drawable.esimecul, R.drawable.esimecul_a),
            new Superior("ESIME TIC", R.drawable.esimetic, R.drawable.esimetic_a),
            new Superior("ESIMEZ", R.drawable.esimez, R.drawable.esimez_a),
            new Superior("ESIQIE", R.drawable.esiqie, R.drawable.esiqie_a),
            new Superior("ESIT", R.drawable.esit, R.drawable.esit_a),
            new Superior("ESM", R.drawable.esm, R.drawable.esm_a),
            new Superior("EST", R.drawable.est, R.drawable.est_a),
            new Superior("UPIBI", R.drawable.upibi, R.drawable.upibi_a),
            new Superior("UPIICSA", R.drawable.upiicsa, R.drawable.upiicsa_a),
            new Superior("UPIIG", R.drawable.upiig, R.drawable.upiig_a),
            new Superior("UPIIH", R.drawable.upiih, R.drawable.upiih_a),
            new Superior("UPIITA", R.drawable.upiita, R.drawable.upiita_a),
            new Superior("UPIIZ", R.drawable.upiiz, R.drawable.upiiz_a)
    };

    /**
     * Obtiene item basado en su identificador
     *
     * @param id identificador
     * @return Coche
     */
    public static Superior getItem(int id) {
        for (Superior item : ITEMS) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

}
