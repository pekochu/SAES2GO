package xyz.luisnglbrv.sigma.reinscripcion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import xyz.luisnglbrv.sigma.R;

/**
 * Created by LuisAngel on 31/12/16.
 */

public class TabFragment extends Fragment {
    ListView mLista;

    static List<List<String>> listaCita;
    static List<List<String>> listaInfo;

    public static int CAT = 0;

    public static final String TEXT = "text";

    public static void clearAndInit(){
        listaCita = null;
        listaCita = new ArrayList<>();
        listaCita.clear();
        listaInfo = null;
        listaInfo = new ArrayList<>();
        listaInfo.clear();
    }

    public static Fragment newInstance(String text, List<String> info, List<String> datos, int cat) {
        TabFragment f = new TabFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);

        if(cat == 0){
            listaCita.add(datos);
            listaInfo.add(info);
        }else if(cat == 1){
            listaCita.add(datos);
            listaInfo.add(info);
        }else if(cat == 2){
            listaCita.add(datos);
            listaInfo.add(info);
        }

        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saes_tab_fragment, container, false);
        mLista = (ListView) view.findViewById(R.id.listView);
        ItemList adapter;

        if (getArguments().getString(TEXT).equals("CITA")) {
            adapter = new ItemList(this.getActivity(), listaInfo.get(0), listaCita.get(0));
            mLista.setAdapter(adapter);
        } else if (getArguments().getString(TEXT).equals("CARRERA")) {
            adapter = new ItemList(this.getActivity(), listaInfo.get(1), listaCita.get(1));
            mLista.setAdapter(adapter);
        } else if (getArguments().getString(TEXT).equals("TRAYECTORIA")) {
            adapter = new ItemList(this.getActivity(), listaInfo.get(2), listaCita.get(2));
            mLista.setAdapter(adapter);
        }

        return view;
    }
}
