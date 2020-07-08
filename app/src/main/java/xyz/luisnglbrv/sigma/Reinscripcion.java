package xyz.luisnglbrv.sigma;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import xyz.luisnglbrv.sigma.dataProvider.DataSAES;
import xyz.luisnglbrv.sigma.reinscripcion.TabAdapter;
import xyz.luisnglbrv.sigma.reinscripcion.TabFragment;


/**
 * Fragmento principal
 */
public class Reinscripcion extends Fragment {

    public static final String ARG_SECTION_TITLE = "section_number";

    private static final String TAG = Reinscripcion.class.getSimpleName();

    private AppBarLayout mAppBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Preferencias Datos;
    private List<List<String>> listaAux;
    private List<List<String>> listaCita;
    private List<List<String>> listaInfo;
    private static int desfasadas = 0;
    private static int reprobadas = 0;
    private static int nocursadas = 0;
    private static String promediogen;
    private Locale loc = new Locale("es", "MX");

    public static Reinscripcion newInstance(String sectionTitle) {
        Reinscripcion fragment = new Reinscripcion();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public Reinscripcion() {
    }

    public static void setReprobadas(int e) {
        reprobadas = e;
    }

    public static void setDesfasadas(int e) {
        desfasadas = e;
    }

    public static void setNocursadas(int e) {
        nocursadas = e;
    }

    public static void setPromedioGen(String s) {
        promediogen = s;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        View view = inflater.inflate(R.layout.fragment_reinscripcion, container, false);
        Datos = new Preferencias(getContext());
        View padre = (View) container.getParent();

        mAppBar = (AppBarLayout) padre.findViewById(R.id.AppBar);
        mTabLayout = new TabLayout(getActivity());
        mTabLayout.setTabTextColors( //Color de los textos del tablayout
                Color.parseColor(Datos.getColorTab()),
                Color.parseColor(getResources().getString(R.string.tab_SelectedText)));
        mTabLayout.setSelectedTabIndicatorColor( //Color del indicador
                Color.parseColor(getResources().getString(R.string.tab_Indicator)));
        mAppBar.addView(mTabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.cita_viewPager);

        listaInfo = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            listaInfo.add(new ArrayList<String>());
        listaInfo.get(0).add(this.getContext().getResources().getString(R.string.reinscripcion_1));
        listaInfo.get(0).add(this.getContext().getResources().getString(R.string.reinscripcion_2));
        listaInfo.get(0).add(this.getContext().getResources().getString(R.string.reinscripcion_3));
        listaInfo.get(0).add(this.getContext().getResources().getString(R.string.reinscripcion_4));

        listaInfo.get(1).add(this.getContext().getResources().getString(R.string.reinscripcion_5));
        listaInfo.get(1).add(this.getContext().getResources().getString(R.string.reinscripcion_6));
        listaInfo.get(1).add(this.getContext().getResources().getString(R.string.reinscripcion_7));
        listaInfo.get(1).add(this.getContext().getResources().getString(R.string.reinscripcion_8));
        listaInfo.get(1).add(this.getContext().getResources().getString(R.string.reinscripcion_9));
        listaInfo.get(1).add(this.getContext().getResources().getString(R.string.reinscripcion_10));

        listaInfo.get(2).add(this.getContext().getResources().getString(R.string.reinscripcion_11));
        listaInfo.get(2).add(this.getContext().getResources().getString(R.string.reinscripcion_12));
        listaInfo.get(2).add(this.getContext().getResources().getString(R.string.reinscripcion_13));
        listaInfo.get(2).add(this.getContext().getResources().getString(R.string.reinscripcion_14));
        listaInfo.get(2).add(this.getContext().getResources().getString(R.string.reinscripcion_15));
        listaInfo.get(2).add(this.getContext().getResources().getString(R.string.reinscripcion_16));

        // Lista de dato
        this.listaAux = new ArrayList<>();
        this.listaCita = new ArrayList<>();
        DataSAES data = new DataSAES(this.getContext(), 1);
        if(data.getListaCita() != null) {
            listaAux.clear();
            listaCita.clear();
            listaAux = data.getListaCita();
            for (int i = 0; i < 3; i++)
                listaCita.add(new ArrayList<String>());
            try {
                int inicioReLength = listaAux.get(0).get(0).length();
                String inicioRe = listaAux.get(0).get(0).substring(0, inicioReLength - 5);
                inicioRe += listaAux.get(0).get(0).substring(inicioReLength - 5, inicioReLength).replace(" ", "");
                int finReLength = listaAux.get(0).get(1).length();
                String finRe = listaAux.get(0).get(1).substring(0, finReLength - 5);
                finRe += listaAux.get(0).get(1).substring(finReLength - 5, finReLength).replace(" ", "");

                Date inicio = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", loc).parse(inicioRe);
                Date fin = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", loc).parse(finRe);
                listaCita.get(0).add(getFecha(inicio));
                listaCita.get(0).add(getFecha(fin));
            } catch (Exception e) {
                listaCita.get(0).add(listaAux.get(0).get(0));
                listaCita.get(0).add(listaAux.get(0).get(1));
                Log.e("ReinscripciónFragment", e.getLocalizedMessage());
                e.printStackTrace();
            }
            listaCita.get(0).add(String.valueOf(promediogen));
            if (reprobadas != 1)
                listaCita.get(0).add(String.valueOf(reprobadas) + " materias.");
            else
                listaCita.get(0).add(String.valueOf(reprobadas) + " materia.");
            // # Carrera del alumno
            listaCita.get(1).add(listaAux.get(1).get(0) + " créditos.");
            listaCita.get(1).add(listaAux.get(1).get(1) + " créditos.");
            listaCita.get(1).add(listaAux.get(1).get(2) + " créditos.");
            listaCita.get(1).add(listaAux.get(1).get(3) + " créditos.");
            listaCita.get(1).add(listaAux.get(1).get(4) + " semestres.");
            listaCita.get(1).add(listaAux.get(1).get(5) + " semestres.");
            // # Trayectoria del alumno
            listaCita.get(2).add(listaAux.get(2).get(1) + " créditos.");
            listaCita.get(2).add(listaAux.get(2).get(2) + " créditos.");
            // Materias desfasadas
            if (desfasadas != 1)
                listaCita.get(2).add(String.valueOf(desfasadas) + " materias.");
            else
                listaCita.get(2).add(String.valueOf(desfasadas) + " materia.");
            // Materias no cursadas
            if (nocursadas != 1)
                listaCita.get(2).add(String.valueOf(nocursadas) + " materias.");
            else
                listaCita.get(2).add(String.valueOf(nocursadas) + " materia.");
            // Semestres cursados
            if (!listaAux.get(2).get(3).equals("1"))
                listaCita.get(2).add(listaAux.get(2).get(3) + " semestres.");
            else
                listaCita.get(2).add(listaAux.get(2).get(3) + " semestre.");
            // Semestres faltantes
            if (!listaAux.get(2).get(4).equals("1"))
                listaCita.get(2).add(listaAux.get(2).get(4) + " semestres.");
            else
                listaCita.get(2).add(listaAux.get(2).get(4) + " semestre.");
        }else{
            for (int i = 0; i < 3; i++)
                listaCita.add(new ArrayList<String>());
            // # Cita
            for (int i = 0; i < 5; i++)
                listaCita.get(0).add("No disponible");
            // # Carrera
            for (int i = 0; i < 6; i++)
                listaCita.get(1).add("N/D créditos.");
            // # Trayectoria
            for (int i = 0; i < 7; i++)
                listaCita.get(2).add("N/D créditos.");
        }
        /* FIN */

        TabFragment.clearAndInit();
        setupViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getChildFragmentManager());
        // Cita ====
        adapter.add("CITA", listaInfo.get(0), listaCita.get(0), 0);
        // Carrera ====
        if (listaCita.get(1).get(0).equals("N/D créditos.")) {
            listaCita.get(1).set(0, "No hay información para mostrar.\n\nRazones:\n\n" +
                    " - Aún vas en primer semestre.\n\n");
            for(int i = 1; i < listaCita.get(1).size(); i++){
                listaCita.get(1).set(i, "");
            }
        }
        adapter.add("CARRERA", listaInfo.get(1), listaCita.get(1), 1);
        // Trayectoria ====
        if(listaCita.get(2).get(0).equals("N/D créditos.")){
            listaCita.get(2).set(0, "No hay información para mostrar.\n\nRazones:\n\n" +
                    " - Aún vas en primer semestre.\n\n");
            for(int i = 1; i < listaCita.get(2).size(); i++){
                listaCita.get(2).set(0, "");
            }
        }
        adapter.add("TRAYECTORIA", listaInfo.get(2), listaCita.get(2), 2);
        // Setear
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAppBar.removeView(mTabLayout);
    }

    public String getFecha(Date date){
        String mes = "", dia = "", año = "", hora = "", semana = "";
        mes = new SimpleDateFormat("MMMM", loc).format(date);
        semana = new SimpleDateFormat("EEEE", loc).format(date);
        semana = semana.substring(0,1).toUpperCase()+semana.substring(1);
        dia = new SimpleDateFormat("dd", loc).format(date);
        año = new SimpleDateFormat("yyyy", loc).format(date);
        hora = new SimpleDateFormat("hh:mm a", loc).format(date);
        return semana+", "+dia+" de "+mes+" del "+año+"\nHora: "+hora;
    }
}


