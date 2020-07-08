package xyz.luisnglbrv.sigma.academico;

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
 * Created by LuisAngel on 19/12/16.
 */

public class TabFragment extends Fragment {

    private static final String TAG = "Academico"+TabFragment.class.getSimpleName();
    static List<List<String>> testReprobada;
    public static int CAT = 0;

    public static final String TEXT = "";

    public static void clearAndInit(){
        testReprobada = null;
        testReprobada = new ArrayList<>();
        testReprobada.clear();
    }

    public static Fragment newInstance(String text, List<String> clave, List<String> materia, int cat) {
        TabFragment f = new TabFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        CAT = cat;
        if(CAT == 0){
            testReprobada.add(clave);
            testReprobada.add(materia);
        }else if(CAT == 1){
            testReprobada.add(clave);
            testReprobada.add(materia);
        }else if(cat == 2){
            testReprobada.add(clave);
            testReprobada.add(materia);
        }else if(cat == 3){
            testReprobada.add(clave);
            testReprobada.add(materia);
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saes_tab_fragment, container, false);
        ListView mLista = (ListView) view.findViewById(R.id.listView);

        if(getArguments().getString(TEXT).equals("REPROBADAS")){
            ItemList adapter = new ItemList(this.getActivity(), testReprobada.get(0), testReprobada.get(1));
            mLista.setAdapter(adapter);
        }else if(getArguments().getString(TEXT).equals("NO CURSADAS")){
            ItemList adapter = new ItemList(this.getActivity(), testReprobada.get(2), testReprobada.get(3));
            mLista.setAdapter(adapter);
        }else if(getArguments().getString(TEXT).equals("DESFASADAS")){
            ItemList adapter = new ItemList(this.getActivity(), testReprobada.get(4), testReprobada.get(5));
            mLista.setAdapter(adapter);
        }else if(getArguments().getString(TEXT).equals("INFORMACIÓN")){
            ItemList adapter = new ItemList(this.getActivity(), testReprobada.get(0), testReprobada.get(1));
            mLista.setAdapter(adapter);
        }
        return view;
    }
}
