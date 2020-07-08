package xyz.luisnglbrv.sigma;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xyz.luisnglbrv.sigma.kardex.TabAdapter;
import xyz.luisnglbrv.sigma.kardex.TabFragment;
import xyz.luisnglbrv.sigma.dataProvider.DataSAES;

/**
 * Fragmento para el horario
 */
public class Kardex extends Fragment {
    /**
     * Este argumento del fragmento representa el título de cada
     * sección
     */
    public static final String ARG_SECTION_TITLE = "section_number";
    private List<String> listaSemestre;
    private List<List<List<String>>> listaKardex;

    private AppBarLayout mAppBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Preferencias datos;
    private Context parentActivity;

    private int semestres;

    public static Kardex newInstance(String sectionTitle) {
        Kardex fragment = new Kardex();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public Kardex() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kardex, container, false);
        datos = new Preferencias(getContext());
        View padre = (View) container.getParent();

        mAppBar = (AppBarLayout) padre.findViewById(R.id.AppBar);
        mTabLayout = new TabLayout(getActivity());
        mTabLayout.setTabTextColors( //Color de los textos del tablayout
                Color.parseColor(datos.getColorTab()),
                Color.parseColor(getResources().getString(R.string.tab_SelectedText)));
        mTabLayout.setSelectedTabIndicatorColor( //Color del indicador
                Color.parseColor(getResources().getString(R.string.tab_Indicator)));
        mAppBar.addView(mTabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.kardex_ViewPager);

        // Lista de datos
        listaKardex = new ArrayList<>();
        listaSemestre = new ArrayList<>();
        DataSAES data = new DataSAES(this.getContext(), 1);
        if(data.getListKardex() != null) {
            listaKardex.clear();
            listaKardex = data.getListKardex();
        }else {
            listaKardex.add(new ArrayList<List<String>>());
            listaKardex.add(new ArrayList<List<String>>());
            listaKardex.add(new ArrayList<List<String>>());
            listaKardex.add(new ArrayList<List<String>>());
            listaKardex.add(new ArrayList<List<String>>());
            listaKardex.add(new ArrayList<List<String>>());

            listaKardex.get(0).add(new ArrayList<String>()); // # Clave
            listaKardex.get(1).add(new ArrayList<String>()); // # Materia
            listaKardex.get(2).add(new ArrayList<String>()); // # Fecha
            listaKardex.get(3).add(new ArrayList<String>()); // # Periodo
            listaKardex.get(4).add(new ArrayList<String>()); // # Tipo de evaluación
            listaKardex.get(5).add(new ArrayList<String>()); // # Calificación

            listaKardex.get(0).get(0).add("");
            listaKardex.get(1).get(0).add("No se pudo obtener ningún dato.");
            listaKardex.get(2).get(0).add("");
            listaKardex.get(3).get(0).add("");
            listaKardex.get(4).get(0).add("");
            listaKardex.get(5).get(0).add("");
        }
        if(data.getListSemestre() != null){
            listaSemestre.clear();
            listaSemestre = data.getListSemestre();
            semestres = listaSemestre.size();
        }else{
            semestres = 1;
        }
        /* FIN */

        TabFragment.clearAndInit();
        setupViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(this.getFragmentManager());
        switch (listaKardex.get(1).get(0).get(0)) {
            case "No hay información para mostrar.\n\nRazones:\n\n" +
                    " - Aún vas en primer semestre.\n\n" +
                    " - Te diste de baja antes de completar un semestre.":
                adapter.add("INFORMACIÓN", listaKardex, semestres);
                viewPager.setAdapter(adapter);
                mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                break;
            case "No se pudo obtener ningún dato.":
                adapter.add("INFORMACIÓN", listaKardex, semestres);
                viewPager.setAdapter(adapter);
                mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                break;
            default:
                for (int i = 0; i < semestres; i++) {
                    adapter.add(listaSemestre.get(i), listaKardex, semestres);
                }
                viewPager.setAdapter(adapter);
                mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                if (semestres > 2) {
                    mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                }
                break;
        }

    }

    @Override
    public void onAttach(Context cnt){
        super.onAttach(cnt);
        parentActivity = cnt;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAppBar.removeView(mTabLayout);
    }

}