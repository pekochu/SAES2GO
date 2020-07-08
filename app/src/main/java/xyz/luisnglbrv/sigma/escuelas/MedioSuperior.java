package xyz.luisnglbrv.sigma.escuelas;

import xyz.luisnglbrv.sigma.R;

/**
 * Created by LuisAngel on 21/12/16.
 */

public class MedioSuperior {
    private String nombre;
    private int idDrawable;
    private int alphaDrawable;

    public MedioSuperior(String nombre, int idDrawable, int alphaDrawable) {
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

    public static MedioSuperior[] ITEMS = {
            new MedioSuperior("CECyT 1", R.drawable.cecyt1, R.drawable.cecyt1_a),
            new MedioSuperior("CECyT 2", R.drawable.cecyt2, R.drawable.cecyt2_a),
            new MedioSuperior("CECyT 3", R.drawable.cecyt3, R.drawable.cecyt3_a),
            new MedioSuperior("CECyT 4", R.drawable.cecyt4, R.drawable.cecyt4_a),
            new MedioSuperior("CECyT 5", R.drawable.cecyt5, R.drawable.cecyt5_a),
            new MedioSuperior("CECyT 6", R.drawable.cecyt6, R.drawable.cecyt6_a),
            new MedioSuperior("CECyT 7", R.drawable.cecyt7, R.drawable.cecyt7_a),
            new MedioSuperior("CECyT 8", R.drawable.cecyt8, R.drawable.cecyt8_a),
            new MedioSuperior("CECyT 9", R.drawable.cecyt9, R.drawable.cecyt9_a),
            new MedioSuperior("CECyT 10", R.drawable.cecyt10, R.drawable.cecyt10_a),
            new MedioSuperior("CECyT 11", R.drawable.cecyt11, R.drawable.cecyt11_a),
            new MedioSuperior("CECyT 12", R.drawable.cecyt12, R.drawable.cecyt12_a),
            new MedioSuperior("CECyT 13", R.drawable.cecyt13, R.drawable.cecyt13_a),
            new MedioSuperior("CECyT 14", R.drawable.cecyt14, R.drawable.cecyt14_a),
            new MedioSuperior("CECyT 15", R.drawable.cecyt15, R.drawable.cecyt15_a),
            new MedioSuperior("CECyT 16", R.drawable.cecyt16, R.drawable.cecyt16_a),
            new MedioSuperior("CECyT 17", R.drawable.cecyt17, R.drawable.cecyt17_a),
            new MedioSuperior("CECyT 18", R.drawable.cecyt18, R.drawable.cecyt17_a),
            new MedioSuperior("CET 1", R.drawable.cet1, R.drawable.cet1_a),
    };

    /**
     * Obtiene item basado en su identificador
     *
     * @param id identificador
     * @return Coche
     */
    public static MedioSuperior getItem(int id) {
        for (MedioSuperior item : ITEMS) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
