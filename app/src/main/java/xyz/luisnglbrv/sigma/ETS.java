package xyz.luisnglbrv.sigma;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ETS extends Fragment {
    /**
     * Este argumento del fragmento representa el título de cada
     * sección
     */
    public static final String ARG_SECTION_TITLE = "section_number";

    private ListView mLista;
    private ItemList adapter;

    static String[] periodo;
    static String[] tipo;
    static String[] clave;
    static String[] materia = { "No hay información para mostrar.\n\nRazones:\n\n" +
            " - No es periodo de ETS.\n\n" +
            " - No tienes ETS inscritos." };
    static String[] turno;
    static String[] calif;

    public static ETS newInstance(String sectionTitle) {
        ETS fragment = new ETS();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public static void setETS(String[] txt, String[] txt1, String[] txt2, String[] txt3,
                                    String[] txt4, String[] txt5){
        periodo = txt;
        tipo = txt1;
        clave = txt2;
        materia = txt3;
        turno = txt4;
        calif = txt5;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ets, container, false);
        mLista = (ListView) view.findViewById(R.id.listView_ets);
        adapter = new ItemList(getActivity(), periodo, tipo, materia, turno, calif);
        mLista.setAdapter(adapter);
        return view;
    }

    //----- CLASE ItemList ------
    public class ItemList extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] periodo;
        private final String[] tipo;
        private final String[] materia;
        private final String[] turno;
        private final String[] calif;

        public ItemList(Activity context, String[] periodo, String[] tipo, String[] materia,
                        String[] turno, String[] calif) {
            super(context, R.layout.item_list_ordinario, materia);
            this.context = context;
            this.periodo = periodo;
            this.tipo = tipo;
            this.materia = materia;
            this.turno = turno;
            this.calif = calif;
        }

        private class ViewHolder{
            private TextView txtPeriodo;
            private TextView txtTipo;
            private TextView txtMateria;
            private TextView txtTurno;
            private TextView txtCalificacion;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater = context.getLayoutInflater();
            ViewHolder h;

            if(view == null){
                view = inflater.inflate(R.layout.item_list_ets, parent, false);
                h = new ViewHolder();

                h.txtPeriodo = (TextView) view.findViewById(R.id.ets_txtPeriodo);
                h.txtTipo = (TextView) view.findViewById(R.id.ets_txtTipo);
                h.txtMateria = (TextView) view.findViewById(R.id.ets_txtMateria);
                h.txtTurno = (TextView) view.findViewById(R.id.ets_txtTurno);
                h.txtCalificacion = (TextView) view.findViewById(R.id.ets_txtCalificacion);

                view.setTag(h);
            }else{
                h = (ViewHolder) view.getTag();
            }

            h.txtPeriodo.setText(periodo[position]);
            h.txtTipo.setText(tipo[position]);
            h.txtMateria.setText(materia[position]);
            h.txtTurno.setText(turno[position]);
            h.txtCalificacion.setText(calif[position]);

            view.setBackgroundColor(position % 2 == 0 ? Color.parseColor("#FAFAFA") :
                    Color.parseColor("#F5F5F5"));
            return view;
        }
    }

}
