package xyz.luisnglbrv.sigma;

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

import xyz.luisnglbrv.sigma.dataProvider.DataSAES;
import xyz.luisnglbrv.sigma.objects.MateriaCalificaciones;
import xyz.luisnglbrv.sigma.ordinario.TabAdapter;
import xyz.luisnglbrv.sigma.ordinario.TabFragment;

/**
 * Fragmento para el horario
 */
public class Ordinario extends Fragment {
    /**
     * Este argumento del fragmento representa el título de cada
     * sección
     */
    public static final String ARG_SECTION_TITLE = "section_number";
    private static final String TAG = Ordinario.class.getSimpleName();

    private AppBarLayout mAppBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Preferencias datos;

    private List<MateriaCalificaciones> listaOrdinario;
    private List<String> materias, primero, segundo, tercero, extra, finales;

    public static Ordinario newInstance(String sectionTitle) {
        Ordinario fragment = new Ordinario();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public Ordinario() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ordinario, container, false);
        datos = new Preferencias(getContext());
        View padre = (View) container.getParent();
        // Diseño
        mAppBar = padre.findViewById(R.id.AppBar);
        mTabLayout = new TabLayout(getActivity());
        mTabLayout.setTabTextColors(
                Color.parseColor(datos.getColorTab()),
                Color.parseColor(getResources().getString(R.string.tab_SelectedText)));
        mTabLayout.setSelectedTabIndicatorColor(
                Color.parseColor(getResources().getString(R.string.tab_Indicator)));
        mAppBar.addView(mTabLayout);
        mViewPager = view.findViewById(R.id.ordinario_ViewPager);
        // Lista de datos
        listaOrdinario = new ArrayList<>();
        materias = new ArrayList<>();
        primero = new ArrayList<>();
        segundo = new ArrayList<>();
        tercero = new ArrayList<>();
        extra = new ArrayList<>();
        finales = new ArrayList<>();
        if(new DataSAES(this.getContext(), 1).getListOrdinario() != null) {
            listaOrdinario.clear();
            listaOrdinario = new DataSAES(this.getContext(), 1).getListOrdinario();
        }else{
            listaOrdinario.add(new MateriaCalificaciones("", "No se pudo obtener ningún dato.",
                    "", "", "", "", ""));
        }
        // Pasar a listas de Strings
        for(MateriaCalificaciones m : listaOrdinario){
            materias.add(m.getNombre());
            primero.add(m.getPrimerParcial());
            segundo.add(m.getSegundoParcial());
            tercero.add(m.getTercerParcial());
            extra.add(m.getExtra());
            finales.add(m.getCalFinal());
        }

        TabFragment.clearAndInit();
        setupViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(this.getFragmentManager());
        if(materias.get(0).equals("No hay información para mostrar.\n\nRazones:\n\n" +
                " - No estás inscrito.\n\n - No has evaluado a tus profesores.\n\n" +
                " - El periodo ordinario ha finalizado.\n\n - Estás dado de baja.") ||
                materias.get(0).equals("No se pudo obtener ningún dato.")){
            adapter.add("INFORMACIÓN", materias, primero, 6);
            viewPager.setAdapter(adapter);
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }else{
            adapter.add("1ER PARCIAL", materias, primero, 0);
            adapter.add("2DO PARCIAL", materias, segundo, 1);
            adapter.add("3ER PARCIAL", materias, tercero, 2);
            adapter.add("EXTRA", materias, extra, 3);
            adapter.add("FINAL", materias, finales, 4);
            viewPager.setAdapter(adapter);
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAppBar.removeView(mTabLayout);
    }

}


