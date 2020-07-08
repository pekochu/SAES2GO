package xyz.luisnglbrv.sigma.academico;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.luisnglbrv.sigma.R;

/**
 * Created by LuisAngel on 19/12/16.
 */

public class ItemList extends ArrayAdapter<String> {

    private final Activity context;
    private final List<List<String>> testReprobada = new ArrayList<List<String>>();

    public ItemList(Activity context, List<String> clave, List<String> materia) {
        super(context, R.layout.item_list_academico, materia);
        this.context = context;
        this.testReprobada.add(clave);
        this.testReprobada.add(materia);
    }

    private class ViewHolder{
        private TextView txtClave;
        private TextView txtMateria;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder h;

        if(view == null) {
            view = inflater.inflate(R.layout.item_list_academico, parent, false);
            h = new ViewHolder();

            h.txtClave = (TextView) view.findViewById(R.id.academico_txtClave);
            h.txtMateria = (TextView) view.findViewById(R.id.academico_txtMateria);

            view.setTag(h);
        }else{
            h = (ViewHolder) view.getTag();
        }

        h.txtClave.setText(testReprobada.get(0).get(position));
        h.txtMateria.setText(testReprobada.get(1).get(position));

        view.setBackgroundColor(position % 2 == 0 ? Color.parseColor("#FAFAFA") :
                Color.parseColor("#F5F5F5"));
        return view;
    }
}
