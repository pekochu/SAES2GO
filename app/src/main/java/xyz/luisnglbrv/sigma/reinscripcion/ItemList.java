package xyz.luisnglbrv.sigma.reinscripcion;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.luisnglbrv.sigma.Preferencias;
import xyz.luisnglbrv.sigma.R;

/**
 * Created by LuisAngel on 31/12/16.
 */

public class ItemList extends ArrayAdapter<String> {

    private final Activity context;
    private List<String> listaCita;
    private List<String> listaInfo;
    private Preferencias pref;

    public ItemList(Activity context, List<String> info, List<String> datos) {
        super(context, R.layout.item_list_reinscripcion, datos);
        this.context = context;
        this.listaInfo = info;
        this.listaCita = datos;
    }

    private class ViewHolder{
        private TextView txtInfo;
        private TextView txtDatos;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        pref = new Preferencias(getContext());
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder h;

        if(view == null){
            view = inflater.inflate(R.layout.item_list_reinscripcion, parent, false);
            h = new ViewHolder();

            h.txtInfo = (TextView) view.findViewById(R.id.cita_txtInfo);
            h.txtDatos = (TextView) view.findViewById(R.id.cita_txtDatos);

            view.setTag(h);
        }else{
            h = (ViewHolder) view.getTag();
        }
        h.txtInfo.setTextColor(pref.getColorSelected());
        h.txtInfo.setText(listaInfo.get(position));
        h.txtDatos.setText(listaCita.get(position));

        if(listaCita.get(0).equals("No hay información para mostrar.\n\nRazones:\n\n" +
                " - Aún vas en primer semestre.\n\n")){
            h.txtDatos.setPadding(16, 16, 16, 16);
            h.txtInfo.setVisibility(View.GONE);
        }else if(listaCita.get(0).equals("No es periodo de reinscripciones. Vuelve más tarde.")){
            h.txtDatos.setPadding(16, 16, 16, 16);
            h.txtInfo.setVisibility(View.GONE);
        }
        return view;
    }
}