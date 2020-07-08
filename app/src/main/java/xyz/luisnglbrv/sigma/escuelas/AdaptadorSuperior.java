package xyz.luisnglbrv.sigma.escuelas;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import xyz.luisnglbrv.sigma.Preferencias;
import xyz.luisnglbrv.sigma.R;

/**
 * Created by LuisAngel on 21/12/16.
 */

public class AdaptadorSuperior extends BaseAdapter {
    private Context context;
    private Preferencias datos;
    private int entero;

    public AdaptadorSuperior(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Superior.ITEMS.length;
    }

    @Override
    public Superior getItem(int position) {
        return Superior.ITEMS[position];
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        datos = new Preferencias(context);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list_grid_escuela, viewGroup, false);
        }

        ImageView fondoEscuela =(ImageView) view.findViewById(R.id.imagen_escuela);
        TextView nombreEscuela = (TextView) view.findViewById(R.id.nombre_escuela);
        nombreEscuela.setBackgroundColor(Color.parseColor(datos.getColorFooter()));

        final Superior item = getItem(position);

        Glide.with(fondoEscuela.getContext()).load(item.getIdDrawable()).into(fondoEscuela);

        nombreEscuela.setText(item.getNombre());

        return view;
    }
}
