package xyz.luisnglbrv.sigma.ordinario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import xyz.luisnglbrv.sigma.NavegadorActivity;
import xyz.luisnglbrv.sigma.R;

/**
 * Created by LuisAngel on 15/12/16.
 */

public class TabFragment extends Fragment {

    ListView mLista;

    static List<List<String>> listaOrdinario = null;
    static List<List<String>> listaMaterias = null;
    public static int CAT = 0;

    public static final String TEXT = "text";

    public static void clearAndInit(){
        listaMaterias = new ArrayList<>();
        listaOrdinario = new ArrayList<>();
        listaMaterias.clear();
        listaOrdinario.clear();
    }

    public static Fragment newInstance(String text, List<String> materia, List<String> calificacion,
                                       int cat) {
        TabFragment f = new TabFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        listaMaterias.add(materia);
        listaOrdinario.add(calificacion);
        CAT = cat;

        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saes_tab_fragment, container, false);
        mLista = (ListView) view.findViewById(R.id.listView);

        ItemList adapter;

        if (getArguments().getString(TEXT).equals("1ER PARCIAL")) {
            adapter = new ItemList(this.getActivity(), listaMaterias.get(0), listaOrdinario.get(0));
            mLista.setAdapter(adapter);
        } else if (getArguments().getString(TEXT).equals("2DO PARCIAL")) {
            adapter = new ItemList(this.getActivity(), listaMaterias.get(0), listaOrdinario.get(1));
            mLista.setAdapter(adapter);
        } else if (getArguments().getString(TEXT).equals("3ER PARCIAL")) {
            adapter = new ItemList(this.getActivity(), listaMaterias.get(0), listaOrdinario.get(2));
            mLista.setAdapter(adapter);
        } else if (getArguments().getString(TEXT).equals("EXTRA")) {
            adapter = new ItemList(this.getActivity(), listaMaterias.get(0), listaOrdinario.get(3));
            mLista.setAdapter(adapter);
        } else if (getArguments().getString(TEXT).equals("FINAL")) {
            adapter = new ItemList(this.getActivity(), listaMaterias.get(0), listaOrdinario.get(4));
            mLista.setAdapter(adapter);
        } else if (getArguments().getString(TEXT).equals("INFORMACIÓN")) {
            adapter = new ItemList(this.getActivity(), listaMaterias.get(0), listaOrdinario.get(0));
            mLista.setAdapter(adapter);
        }

        return view;
    }
}
