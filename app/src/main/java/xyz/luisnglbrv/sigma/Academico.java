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

import xyz.luisnglbrv.sigma.academico.TabAdapter;
import xyz.luisnglbrv.sigma.academico.TabFragment;
import xyz.luisnglbrv.sigma.dataProvider.DataSAES;

/**
 * Fragmento para el horario
 */
public class Academico extends Fragment {
    /**
     * Este argumento del fragmento representa el título de cada
     * sección
     */
    public static final String ARG_SECTION_TITLE = "section_number";
    private static final String TAG = Academico.class.getSimpleName();

    private AppBarLayout mAppBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Preferencias datos;
    private List<List<String>> listaAcademico = null;

    public static Academico newInstance(String sectionTitle) {
        Academico fragment = new Academico();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public Academico() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_academico, container, false);
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
        mViewPager = (ViewPager) view.findViewById(R.id.academico_ViewPager);

        // Lista de datos
        listaAcademico = new ArrayList<>();
        if(new DataSAES(this.getContext(), 1).getListAcademico() != null) {
            listaAcademico.clear();
            listaAcademico = new DataSAES(this.getContext(), 1).getListAcademico();
        }else{
            listaAcademico.add(new ArrayList<String>());
            listaAcademico.add(new ArrayList<String>());
            listaAcademico.get(0).add("");
            listaAcademico.get(1).add("No se pudo obtener ningún dato.");
            /* Desfasadas */
            listaAcademico.add(new ArrayList<String>());
            listaAcademico.add(new ArrayList<String>());
            listaAcademico.get(2).add("");
            listaAcademico.get(3).add("No se pudo obtener ningún dato.");
            /* No cursadas */
            listaAcademico.add(new ArrayList<String>());
            listaAcademico.add(new ArrayList<String>());
            listaAcademico.get(4).add("");
            listaAcademico.get(5).add("No se pudo obtener ningún dato.");
        }
        /* FIN */

        TabFragment.clearAndInit();
        setupViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(this.getFragmentManager());
        if(!listaAcademico.get(1).get(0).equals("No se pudo obtener ningún dato.")){
            adapter.add("REPROBADAS", listaAcademico.get(0), listaAcademico.get(1), 0);
            adapter.add("NO CURSADAS", listaAcademico.get(4), listaAcademico.get(5), 1);
            adapter.add("DESFASADAS", listaAcademico.get(2), listaAcademico.get(3), 2);
        }else{
            adapter.add("INFORMACIÓN", listaAcademico.get(0), listaAcademico.get(1), 3);
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAppBar.removeView(mTabLayout);
    }



}


