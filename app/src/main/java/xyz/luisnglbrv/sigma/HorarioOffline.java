package xyz.luisnglbrv.sigma;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import xyz.luisnglbrv.sigma.db.Horario_OpenHelper;
import xyz.luisnglbrv.sigma.horario.TabAdapter;

public class HorarioOffline extends AppCompatActivity {

    private static final String TAG = HorarioOffline.class.getSimpleName();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<List<String>> listaHorarioOffline;
    private int hoy = 0, day;
    private Boolean b1=false, b2=false, b3=false, b4=false, b5=false, b6=false;
    private ProgressDialog progressDialog;
    private Preferencias pref;
    // Static vars

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent().setAction("Already created");
        setContentView(R.layout.activity_horario_offline);
        pref = new Preferencias(this);

        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_WEEK);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.horario_offline_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.horario_offline_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.horario_offline_ViewPager);
        setSupportActionBar(mToolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Por favor, espera...");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "CANCELAR",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.dismiss();
                        HorarioOffline.this.finish();
                    }
                });
        progressDialog.show();
        new LoadData().execute();
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        if (!listaHorarioOffline.get(1).get(0).equals("No hay información para mostrar.\n\nRazones:\n\n" +
                " - No estás inscrito.\n\n" +
                " - El periodo ordinario ha finalizado.\n\n - Estás dado de baja.")) {
            for (int i = 0; i < listaHorarioOffline.get(5).size(); i++) {
                if (!listaHorarioOffline.get(5).get(i).equals("")) {
                    b1 = true;
                }
                if (!listaHorarioOffline.get(6).get(i).equals("")) {
                    b2 = true;
                }
                if (!listaHorarioOffline.get(7).get(i).equals("")) {
                    b3 = true;
                }
                if (!listaHorarioOffline.get(8).get(i).equals("")) {
                    b4 = true;
                }
                if (!listaHorarioOffline.get(9).get(i).equals("")) {
                    b5 = true;
                }
                if (!listaHorarioOffline.get(10).get(i).equals("")) {
                    b6 = true;
                }
            }
            if (b1)
                adapter.add("LUNES", listaHorarioOffline, 0);
            if (b2)
                adapter.add("MARTES", listaHorarioOffline, 1);
            if (b3)
                adapter.add("MIÉRCOLES", listaHorarioOffline, 2);
            if (b4)
                adapter.add("JUEVES", listaHorarioOffline, 3);
            if (b5)
                adapter.add("VIERNES", listaHorarioOffline, 4);
            if (b6)
                adapter.add("SÁBADO", listaHorarioOffline, 5);
            if (!b1 && !b2 && !b3 && !b4 && !b5 && !b6){
                adapter.add("PROFESORES", listaHorarioOffline, 6);
                mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                viewPager.setAdapter(adapter);
            }else{
                mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                viewPager.setAdapter(adapter);
            }
        } else {
            adapter.add("INFORMACIÓN", listaHorarioOffline, 7);
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
    }

    public class LoadData extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            return llenarHorario(Horario_OpenHelper.getInstance(HorarioOffline.this).obtenerHorario());
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            setupViewPager(mViewPager);
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            mTabLayout.setupWithViewPager(mViewPager);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            mViewPager.setCurrentItem(hoy);

            if(!pref.getHorOffTip())
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    View showcased1 = findViewById(R.id.horario_offline_action_1);
                    new ShowcaseView.Builder(HorarioOffline.this)
                            .setTarget(new ViewTarget(showcased1))
                            .setContentTitle("¡Nuevo!")
                            .setContentText("¡Edita tu horario tocando el ícono del lapiz!\n" +
                                    "También puedes activar las notificaciones de clase con el " +
                                    "ícono de la campana.")
                            .setStyle(R.style.CustomShowcaseTheme3)
                            .withNewStyleShowcase()
                            .blockAllTouches()
                            .setShowcaseEventListener(new OnShowcaseEventListener() {
                                @Override
                                public void onShowcaseViewHide(ShowcaseView showcaseView) {
                                    pref.setHorOffTip(true);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_horario_offline, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem notif = menu.findItem(R.id.horario_offline_action_2);
        if(!pref.getNotificationActivated()){
            notif.setIcon(R.drawable.ic_notifications_off_white_24dp);
        }else{
            notif.setIcon(R.drawable.ic_notifications_white_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.horario_offline_action_1) {
            startActivity(new Intent(HorarioOffline.this, EditarHorarioActivity.class));
        }else if(id == R.id.horario_offline_action_2){
            if(!pref.getNotificationActivated()) {
                pref.setNotificationsActivated(true);
                Snackbar.make(findViewById(R.id.offline_Coordinator), "Notificaciones activadas",
                        Snackbar.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_notifications_white_24dp);
            }else{
                pref.setNotificationsActivated(false);
                Snackbar.make(findViewById(R.id.offline_Coordinator), "Notificaciones desactivadas",
                        Snackbar.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_notifications_off_white_24dp);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        String action = getIntent().getAction();
        // Prevent endless loop by adding a unique action, don't restart if action is present
        if(action == null || !action.equals("Already created")) {
            Intent intent = new Intent(HorarioOffline.this, SplashScreenActivity.class);
            startActivity(intent);
            HorarioOffline.this.finish();
        }else getIntent().setAction(null);
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public Cursor llenarHorario(Cursor arg){
        listaHorarioOffline = new ArrayList<>();
        listaHorarioOffline.add(new ArrayList<String>());
        listaHorarioOffline.add(new ArrayList<String>());
        listaHorarioOffline.add(new ArrayList<String>());
        listaHorarioOffline.add(new ArrayList<String>());
        listaHorarioOffline.add(new ArrayList<String>());
        listaHorarioOffline.add(new ArrayList<String>());
        listaHorarioOffline.add(new ArrayList<String>());
        listaHorarioOffline.add(new ArrayList<String>());
        listaHorarioOffline.add(new ArrayList<String>());
        listaHorarioOffline.add(new ArrayList<String>());
        listaHorarioOffline.add(new ArrayList<String>());

        int datos = arg.getCount();

        if(arg.moveToFirst()){
            for(int e = 0; e < datos; e++){
                listaHorarioOffline.get(0).add(arg.getString(1));
                listaHorarioOffline.get(1).add(arg.getString(2));
                listaHorarioOffline.get(2).add(arg.getString(3));
                listaHorarioOffline.get(3).add(arg.getString(4));
                listaHorarioOffline.get(4).add(arg.getString(5));
                listaHorarioOffline.get(5).add(arg.getString(6));
                listaHorarioOffline.get(6).add(arg.getString(7));
                listaHorarioOffline.get(7).add(arg.getString(8));
                listaHorarioOffline.get(8).add(arg.getString(9));
                listaHorarioOffline.get(9).add(arg.getString(10));
                listaHorarioOffline.get(10).add(arg.getString(11));
                arg.moveToNext();
            }
        }

        return arg;
    }
}
