package xyz.luisnglbrv.sigma.horario;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import xyz.luisnglbrv.sigma.R;

/**
 * Created by LuisAngel on 15/12/16.
 */

public class TabFragment extends Fragment {

    ListView mLista;
    static List<List<String>> listaHorario;

    public static final String TEXT = "text";

    public static void clearAndInit(){
        listaHorario = null;
        listaHorario = new ArrayList<>();
        listaHorario.clear();
    }

    public static Fragment newInstance(String text, List<List<String>> pack, int cat) {
        TabFragment f = new TabFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        listaHorario = pack;
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saes_tab_fragment, container, false);
        mLista = (ListView) view.findViewById(R.id.listView);

        List<List<String>> listaProvisional = new ArrayList<>();
        List<List<String>> listaProvisional1 = new ArrayList<>();
        List<List<String>> listaProvisional2 = new ArrayList<>();
        List<List<String>> listaProvisional3 = new ArrayList<>();
        List<List<String>> listaProvisional4 = new ArrayList<>();
        List<List<String>> listaProvisional5 = new ArrayList<>();
        List<List<String>> listaProvisional6 = new ArrayList<>();
        for(int i = 0; i < listaHorario.size(); i++) {
            listaProvisional.add(new ArrayList<String>());
            listaProvisional1.add(new ArrayList<String>());
            listaProvisional2.add(new ArrayList<String>());
            listaProvisional3.add(new ArrayList<String>());
            listaProvisional4.add(new ArrayList<String>());
            listaProvisional5.add(new ArrayList<String>());
            listaProvisional6.add(new ArrayList<String>());
        }
        for(int i = 0; i < listaHorario.size(); i++) {
            for(int j = 0; j < listaHorario.get(i).size(); j++){
                listaProvisional.get(i).add(listaHorario.get(i).get(j));
                listaProvisional1.get(i).add(listaHorario.get(i).get(j));
                listaProvisional2.get(i).add(listaHorario.get(i).get(j));
                listaProvisional3.get(i).add(listaHorario.get(i).get(j));
                listaProvisional4.get(i).add(listaHorario.get(i).get(j));
                listaProvisional5.get(i).add(listaHorario.get(i).get(j));
                listaProvisional6.get(i).add(listaHorario.get(i).get(j));
            }
        }

        if(getArguments().getString(TEXT).equals("LUNES")){
            for(int j = 0; j < 2; j++)
                for(int i = 0; i < listaProvisional.get(5).size(); i++)
                    if(listaProvisional.get(5).get(i).length() == 0){
                        listaProvisional.get(5).remove(i);
                        listaProvisional1.get(0).remove(i);
                        listaProvisional1.get(1).remove(i);
                        listaProvisional1.get(2).remove(i);
                        listaProvisional1.get(3).remove(i);
                        listaProvisional1.get(4).remove(i);
                    }

            ItemList adapter = new ItemList(this.getActivity(), getArguments().getString(TEXT),
                    listaProvisional1, listaProvisional.get(5));
            mLista.setAdapter(adapter);
        }
        if(getArguments().getString(TEXT).equals("MARTES")){
            for(int j = 0; j < 2; j++)
                for(int i = 0; i < listaProvisional.get(6).size(); i++)
                    if(listaProvisional.get(6).get(i).length() == 0){
                        listaProvisional.get(6).remove(i);
                        listaProvisional2.get(0).remove(i);
                        listaProvisional2.get(1).remove(i);
                        listaProvisional2.get(2).remove(i);
                        listaProvisional2.get(3).remove(i);
                        listaProvisional2.get(4).remove(i);
                    }
            ItemList adapter = new ItemList(this.getActivity(), getArguments().getString(TEXT),
                    listaProvisional2, listaProvisional.get(6));
            mLista.setAdapter(adapter);
        }

        if(getArguments().getString(TEXT).equals("MIÉRCOLES")){
            for(int j = 0; j < 2; j++)
                for(int i = 0; i < listaProvisional.get(7).size(); i++)
                    if(listaProvisional.get(7).get(i).length() == 0){
                        listaProvisional.get(7).remove(i);
                        listaProvisional3.get(0).remove(i);
                        listaProvisional3.get(1).remove(i);
                        listaProvisional3.get(2).remove(i);
                        listaProvisional3.get(3).remove(i);
                        listaProvisional3.get(4).remove(i);
                    }

            ItemList adapter = new ItemList(this.getActivity(), getArguments().getString(TEXT),
                    listaProvisional3, listaProvisional.get(7));
            mLista.setAdapter(adapter);
        }
        if(getArguments().getString(TEXT).equals("JUEVES")){
            for(int j = 0; j < 2; j++)
                for(int i = 0; i < listaProvisional.get(8).size(); i++)
                    if(listaProvisional.get(8).get(i).length() == 0){
                        listaProvisional.get(8).remove(i);
                        listaProvisional4.get(0).remove(i);
                        listaProvisional4.get(1).remove(i);
                        listaProvisional4.get(2).remove(i);
                        listaProvisional4.get(3).remove(i);
                        listaProvisional4.get(4).remove(i);
                    }

            ItemList adapter = new ItemList(this.getActivity(), getArguments().getString(TEXT),
                    listaProvisional4, listaProvisional.get(8));
            mLista.setAdapter(adapter);
        }
        if(getArguments().getString(TEXT).equals("VIERNES")){
            for(int j = 0; j < 2; j++)
                for(int i = 0; i < listaProvisional.get(9).size(); i++)
                    if(listaProvisional.get(9).get(i).length() == 0){
                        listaProvisional.get(9).remove(i);
                        listaProvisional5.get(0).remove(i);
                        listaProvisional5.get(1).remove(i);
                        listaProvisional5.get(2).remove(i);
                        listaProvisional5.get(3).remove(i);
                        listaProvisional5.get(4).remove(i);
                    }
            ItemList adapter = new ItemList(this.getActivity(), getArguments().getString(TEXT),
                    listaProvisional5, listaProvisional.get(9));
            mLista.setAdapter(adapter);
        }
        if(getArguments().getString(TEXT).equals("SÁBADO")){
            for(int j = 0; j < 2; j++)
                for(int i = 0; i < listaProvisional.get(10).size(); i++)
                    if(listaProvisional.get(10).get(i).length() == 0){
                        listaProvisional.get(10).remove(i);
                        listaProvisional6.get(0).remove(i);
                        listaProvisional6.get(1).remove(i);
                        listaProvisional6.get(2).remove(i);
                        listaProvisional6.get(3).remove(i);
                        listaProvisional6.get(4).remove(i);
                    }
            ItemList adapter = new ItemList(this.getActivity(), getArguments().getString(TEXT),
                    listaProvisional6, listaProvisional.get(10));
            mLista.setAdapter(adapter);
        }
        if(getArguments().getString(TEXT).equals("INFORMACIÓN")){
            ItemList adapter = new ItemList(this.getActivity(), getArguments().getString(TEXT),
                    listaHorario, listaHorario.get(10));
            mLista.setAdapter(adapter);
        }
        if(getArguments().getString(TEXT).equals("PROFESORES")) {
            ItemList adapter = new ItemList(this.getActivity(), getArguments().getString(TEXT),
                    listaHorario, listaHorario.get(10));
            mLista.setAdapter(adapter);
        }

        return view;
    }

}
