package xyz.luisnglbrv.sigma.kardex;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.luisnglbrv.sigma.R;

/**
 * Created by LuisAngel on 15/12/16.
 */

public class ItemList extends ArrayAdapter<String> {

    private final Activity context;
    private List<List<String>> listaKardex = new ArrayList<>();


    public ItemList(Activity context, List<List<String>> pack) {
        super(context, R.layout.item_list_kardex, pack.get(1));
        this.context = context;
        this.listaKardex = pack;
    }

    private class ViewHolder{
        private TextView txtClave;
        private TextView txtMateria;
        private TextView txtCalif;
        private TextView txtTipo;
        private TextView txtFecha;
        private TextView txtPeriodo;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder h;
        if(view == null) {
            view = inflater.inflate(R.layout.item_list_kardex, parent, false);
            h = new ViewHolder();

            h.txtClave = (TextView) view.findViewById(R.id.kardex_txtClave);
            h.txtMateria = (TextView) view.findViewById(R.id.kardex_txtMateria);
            h.txtCalif = (TextView) view.findViewById(R.id.kardex_txtCalificacion);
            h.txtTipo = (TextView) view.findViewById(R.id.kardex_txtTipoEval);
            h.txtFecha = (TextView) view.findViewById(R.id.kardex_txtFecha);
            h.txtPeriodo = (TextView) view.findViewById(R.id.kardex_txtPeriodo);

            view.setTag(h);
        }else{
            h = (ViewHolder) view.getTag();
        }
        h.txtClave.setText(listaKardex.get(0).get(position));
        h.txtMateria.setText(listaKardex.get(1).get(position));
        h.txtFecha.setText(listaKardex.get(2).get(position));
        h.txtPeriodo.setText(listaKardex.get(3).get(position));
        h.txtTipo.setText(listaKardex.get(4).get(position));
        h.txtCalif.setText(listaKardex.get(5).get(position));

        if(listaKardex.get(1).get(0).equals("No hay información para mostrar.\n\nRazones:\n\n" +
                " - Aún vas en primer semestre.\n\n" +
                " - Te diste de baja antes de completar un semestre.") || listaKardex.get(1).get(0).
                equals("No se pudo obtener ningún dato.")){
            h.txtTipo.setVisibility(View.GONE);
            h.txtCalif.setVisibility(View.GONE);
        }
        view.setBackgroundColor(position % 2 == 0 ? Color.parseColor("#FAFAFA") :
                Color.parseColor("#F5F5F5"));
        return view;
    }
}
