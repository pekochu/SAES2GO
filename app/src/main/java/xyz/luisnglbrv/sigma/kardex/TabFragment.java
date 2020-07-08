package xyz.luisnglbrv.sigma.kardex;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import xyz.luisnglbrv.sigma.R;

/**
 * Created by LuisAngel on 15/12/16.
 */

public class TabFragment extends Fragment {

    ListView mLista;

    static List<List<List<String>>> listaKardex;
    List<List<String>> listaDef = new ArrayList<>();

    private static final String TEXT = "text";
    private static int SEMESTRE = 0;
    private int currentTab = 0, contador;

    public static void clearAndInit(){
        listaKardex = null;
        listaKardex = new ArrayList<>();
        listaKardex.clear();
    }

    public static Fragment newInstance(String text, List<List<List<String>>> pack, int semestre) {
        TabFragment f = new TabFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        SEMESTRE = semestre;
        listaKardex = pack;
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saes_tab_fragment, container, false);
        mLista = (ListView) view.findViewById(R.id.listView);

        // # Niveles
        if(getArguments().getString(TEXT).equals("PRIMER SEMESTRE")){
            currentTab = 0;
        }else if(getArguments().getString(TEXT).equals("SEGUNDO SEMESTRE")){
            currentTab = 1;
        }else if(getArguments().getString(TEXT).equals("TERCER SEMESTRE")){
            currentTab = 2;
        }else if(getArguments().getString(TEXT).equals("CUARTO SEMESTRE")){
            currentTab = 3;
        }else if(getArguments().getString(TEXT).equals("QUINTO SEMESTRE")){
            currentTab = 4;
        }else if(getArguments().getString(TEXT).equals("SEXTO SEMESTRE")){
            currentTab = 5;
        }else if(getArguments().getString(TEXT).equals("SEPTIMO SEMESTRE")){
            currentTab = 6;
        }else if(getArguments().getString(TEXT).equals("OCTAVO SEMESTRE")){
            currentTab = 7;
        }else if(getArguments().getString(TEXT).equals("NOVENO SEMESTRE")){
            currentTab = 8;
        }
        // # Default
        if(getArguments().getString(TEXT).equals("INFORMACIÓN")) {
            listaDef.add(listaKardex.get(0).get(0)); // # Clave
            listaDef.add(listaKardex.get(1).get(0)); // # Materia
            listaDef.add(listaKardex.get(2).get(0)); // # Fecha
            listaDef.add(listaKardex.get(3).get(0)); // # Periodo
            listaDef.add(listaKardex.get(4).get(0)); // # Evaluación
            listaDef.add(listaKardex.get(5).get(0)); // # Calificación

            ItemList adapter = new ItemList(this.getActivity(), listaDef);
            mLista.setAdapter(adapter);
        }else{
            contador = 0;
            for(int i = 0; i < listaKardex.get(0).get(currentTab).size(); i++){
                if(listaKardex.get(0).get(currentTab).get(0) != null){
                    listaDef.add(listaKardex.get(0).get(currentTab));
                    listaDef.add(listaKardex.get(1).get(currentTab));
                    listaDef.add(listaKardex.get(2).get(currentTab));
                    listaDef.add(listaKardex.get(3).get(currentTab));
                    listaDef.add(listaKardex.get(4).get(currentTab));
                    listaDef.add(listaKardex.get(5).get(currentTab));
                }
            }
            ItemList adapter = new ItemList(this.getActivity(), listaDef);
            mLista.setAdapter(adapter);
        }
        return view;
    }
}
