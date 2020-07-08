package xyz.luisnglbrv.sigma;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import xyz.luisnglbrv.sigma.db.Horario_OpenHelper;
import xyz.luisnglbrv.sigma.horario.TabAdapter;
import xyz.luisnglbrv.sigma.horario.TabFragment;
import xyz.luisnglbrv.sigma.dataProvider.DataSAES;
import xyz.luisnglbrv.sigma.objects.MateriaHorario;

public class Horario extends Fragment {

    public static final String ARG_SECTION_TITLE = "section_number";
    private AppBarLayout mAppBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Preferencias pref;
    private List<MateriaHorario> listaHorario = new ArrayList<>();
    private List<List<String>> horario = new ArrayList<>();
    private int hoy = 0, day;
    private View padre;
    private Boolean b1=false, b2=false, b3=false, b4=false, b5=false, b6=false;

    public static Horario newInstance(String sectionTitle) {
        Horario fragment = new Horario();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public Horario() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horario, container, false);
        pref = new Preferencias(getContext());
        padre = (View) container.getParent();

        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_WEEK);

        mAppBar = padre.findViewById(R.id.AppBar);
        mTabLayout = new TabLayout(getActivity());
        mTabLayout.setTabTextColors( //Color de los textos del tablayout
                Color.parseColor(pref.getColorTab()),
                Color.parseColor(getResources().getString(R.string.tab_SelectedText)));
        mTabLayout.setSelectedTabIndicatorColor( //Color del indicador
                Color.parseColor(getResources().getString(R.string.tab_Indicator)));
        mAppBar.addView(mTabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.horario_viewPager);
        setHasOptionsMenu(true);

        // Lista de datos
        listaHorario = new ArrayList<>();
        if(new DataSAES(this.getContext(), 1).getListHorario() != null) {
            listaHorario.clear();
            listaHorario = new DataSAES(this.getContext(), 1).getListHorario();
        }else{
            listaHorario = new ArrayList<>();
            listaHorario.add(new MateriaHorario("", "No se pudo obtener ningún dato.", "",
                    "","","","","","","",""));
        }

        for(int i = 0; i < 11; i++){
            horario.add(new ArrayList<String>());
        }
        //
        for(MateriaHorario m : listaHorario){
            horario.get(0).add(m.getGrupo());
            horario.get(1).add(m.getNombre());
            horario.get(2).add(m.getProfesor());
            horario.get(3).add(m.getEdificio());
            horario.get(4).add(m.getSalon());
            horario.get(5).add(m.getLunes());
            horario.get(6).add(m.getMartes());
            horario.get(7).add(m.getMiercoles());
            horario.get(8).add(m.getJueves());
            horario.get(9).add(m.getViernes());
            horario.get(10).add(m.getSabado());
        }

        TabFragment.clearAndInit();
        setupViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(this.getFragmentManager());
        if (!(listaHorario.get(0).getNombre().equals("No hay información para mostrar." +
                "\n\nRazones:\n\n - No estás inscrito.\n\n" +
                " - El periodo ordinario ha finalizado.\n\n - Estás dado de baja.") ||
                listaHorario.get(0).getNombre().equals("No se pudo obtener ningún dato."))) {
            for (int i = 0; i < listaHorario.size(); i++) {
                if (!listaHorario.get(i).getLunes().equals("")) {
                    b1 = true;
                }
                if (!listaHorario.get(i).getMartes().equals("")) {
                    b2 = true;
                }
                if (!listaHorario.get(i).getMiercoles().equals("")) {
                    b3 = true;
                }
                if (!listaHorario.get(i).getJueves().equals("")) {
                    b4 = true;
                }
                if (!listaHorario.get(i).getViernes().equals("")) {
                    b5 = true;
                }
                if (!listaHorario.get(i).getSabado().equals("")) {
                    b6 = true;
                }
            }
            if (b1)
                adapter.add("LUNES", horario, 0);
            if (b2)
                adapter.add("MARTES", horario, 1);
            if (b3)
                adapter.add("MIÉRCOLES", horario, 2);
            if (b4)
                adapter.add("JUEVES", horario, 3);
            if (b5)
                adapter.add("VIERNES", horario, 4);
            if (b6)
                adapter.add("SÁBADO", horario, 5);
            if (!b1 && !b2 && !b3 && !b4 && !b5 && !b6){
                adapter.add("PROFESORES", horario, 6);
                mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                viewPager.setAdapter(adapter);
            }else{
                mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                viewPager.setAdapter(adapter);
            }
        } else {
            adapter.add("INFORMACIÓN", horario, 7);
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            viewPager.setAdapter(adapter);
        }
        if(day == 2 && b1) { hoy = 0; }
        else if(day == 3 && b2) { hoy = 1; }
        else if(day == 4 && b3) { hoy = 2; }
        else if(day == 5 && b4) { hoy = 3; }
        else if(day == 6 && b5) { hoy = 4; }
        else if(day == 7 && b6) { hoy = 5; }
        else{ hoy = 0; }
        mViewPager.setCurrentItem(hoy);

        if(!pref.getHorarioTip())
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    View showcased1 = padre.findViewById(R.id.horario_action_1);
                    new ShowcaseView.Builder(getActivity())
                            .setTarget(new ViewTarget(showcased1))
                            .setContentTitle("Recomendación")
                            .setContentText("Puedes guardar tu horario tocando el símbolo del disco.")
                            .setStyle(R.style.CustomShowcaseTheme2)
                            .withNewStyleShowcase()
                            .blockAllTouches()
                            .setShowcaseEventListener(new OnShowcaseEventListener() {
                                @Override
                                public void onShowcaseViewHide(ShowcaseView showcaseView) {
                                    pref.setHorarioTip(true);
                                }
                                @Override
                                public void onShowcaseViewDidHide(ShowcaseView showcaseView) { }
                                @Override
                                public void onShowcaseViewShow(ShowcaseView showcaseView){ }
                                @Override
                                public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) { }
                            })
                            .hideOnTouchOutside()
                            .build();
                }
            }, 1000);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_horario, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.horario_action_1) {
            if(!listaHorario.get(0).getNombre().equals("No hay información para mostrar.\n\nRazones:\n\n" +
                    " - No estás inscrito.\n\n" +
                    " - El periodo ordinario ha finalizado.\n\n - Estás dado de baja.")){
                guardarHorario();
                Toast.makeText(getActivity(), "¡Horario guardado para ver sin conexión!",
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Nada que guardar", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAppBar.removeView(mTabLayout);
    }

    private void guardarHorario(){
        pref.setHorarioGuardado(true);
        Horario_OpenHelper.getInstance(getActivity()).actualizar();
        for (MateriaHorario m : listaHorario) {
            Horario_OpenHelper.getInstance(getActivity()).insertarHorario(m.getGrupo(),
                    m.getNombre(),m.getProfesor(),m.getEdificio(),m.getSalon(),m.getLunes(),
                    m.getMartes(),m.getMiercoles(),m.getJueves(), m.getViernes(),m.getSabado());
        }
    }
}


