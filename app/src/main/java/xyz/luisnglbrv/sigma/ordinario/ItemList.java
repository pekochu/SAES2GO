package xyz.luisnglbrv.sigma.ordinario;

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
    private final List<List<String>> listaOrdinario = new ArrayList<List<String>>();

    public ItemList(Activity context, List<String> materia, List<String> calificacion) {
        super(context, R.layout.item_list_ordinario, materia);
        this.context = context;
        this.listaOrdinario.add(materia);
        this.listaOrdinario.add(calificacion);
    }

    private class ViewHolder{
        private TextView txtMateria;
        private TextView txtCalificacion;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder h;

        if(view == null){
            view = inflater.inflate(R.layout.item_list_ordinario, parent, false);
            h = new ViewHolder();

            h.txtMateria = (TextView) view.findViewById(R.id.calif_txtMateria);
            h.txtCalificacion = (TextView) view.findViewById(R.id.calif_txtCalificacion);

            view.setTag(h);
        }else{
            h = (ViewHolder) view.getTag();
        }

        h.txtMateria.setText(listaOrdinario.get(0).get(position));
        h.txtCalificacion.setText(listaOrdinario.get(1).get(position));

        view.setBackgroundColor(position % 2 == 0 ? Color.parseColor("#FAFAFA") :
                Color.parseColor("#F5F5F5"));
        return view;
    }
}