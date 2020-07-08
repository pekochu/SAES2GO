package xyz.luisnglbrv.sigma;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.luisnglbrv.sigma.escuelas.MedioSuperior;
import xyz.luisnglbrv.sigma.escuelas.Superior;

public class MainActivity extends AppCompatActivity {

    private Preferencias datos;

    private TextView nombre;
    private TextView boleta;
    private CircleImageView escuela;
    private ImageView background, superbackground;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private CookieSyncManager cookieSyncMngr;
    private CookieManager cookieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent().setAction("Already created");
        datos = new Preferencias(this);
        cookieSyncMngr = CookieSyncManager.createInstance(this);
        cookieManager = CookieManager.getInstance();
        setTheme(datos.getStyle());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        nombre = hView.findViewById(R.id.txt_navNombre);
        boleta = hView.findViewById(R.id.txt_navBoleta);
        escuela = hView.findViewById(R.id.circle_image);
        background = hView.findViewById(R.id.background);
        superbackground = hView.findViewById(R.id.super_background);

        if (navigationView != null) {
            prepararDrawer(navigationView);
            int item_selected = datos.getInteger(Preferencias.KEY_ITEM_SELECTED);
            if (item_selected == -1)
                seleccionarItem(navigationView.getMenu().getItem(0).setChecked(true));
            else
                seleccionarItem(navigationView.getMenu().getItem(item_selected).setChecked(true));
        }
    }

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        seleccionarItem(menuItem);
                        drawer.closeDrawers();
                        return true;
                    }
                });

    }

    private void seleccionarItem(MenuItem item) {
        int id = item.getItemId();
        String title;
        Bundle args = new Bundle();
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_Inicio) {
            datos.setInteger(Preferencias.KEY_ITEM_SELECTED, 0);
            title = item.getTitle().toString();
            args.putString(Alumno.ARG_SECTION_TITLE, title);
            fragment = Alumno.newInstance(title);
            fragment.setArguments(args);
            setTitle(title); // Setear título actual
        } else if (id == R.id.nav_Horario) {
            datos.setInteger(Preferencias.KEY_ITEM_SELECTED, 1);
            title = item.getTitle().toString();
            args.putString(Horario.ARG_SECTION_TITLE, title);
            fragment = Horario.newInstance(title);
            fragment.setArguments(args);
            setTitle(title); // Setear título actual
        } else if (id == R.id.nav_Kardex) {
            datos.setInteger(Preferencias.KEY_ITEM_SELECTED, 2);
            title = item.getTitle().toString();
            args.putString(Kardex.ARG_SECTION_TITLE, title);
            fragment = Kardex.newInstance(title);
            fragment.setArguments(args);
            setTitle(title); // Setear título actual
        } else if (id == R.id.nav_Estadogen) {
            datos.setInteger(Preferencias.KEY_ITEM_SELECTED, 3);
            title = item.getTitle().toString();
            args.putString(Academico.ARG_SECTION_TITLE, title);
            fragment = Academico.newInstance(title);
            fragment.setArguments(args);
            setTitle(title); // Setear título actual
        } else if (id == R.id.nav_Cita) {
            datos.setInteger(Preferencias.KEY_ITEM_SELECTED, 4);
            title = item.getTitle().toString();
            args.putString(Reinscripcion.ARG_SECTION_TITLE, title);
            fragment = Reinscripcion.newInstance(title);
            fragment.setArguments(args);
            setTitle(title); // Setear título actual
        } else if (id == R.id.nav_Ordinario) {
            datos.setInteger(Preferencias.KEY_ITEM_SELECTED, 5);
            title = item.getTitle().toString();
            args.putString(Ordinario.ARG_SECTION_TITLE, title);
            fragment = Ordinario.newInstance(title);
            fragment.setArguments(args);
            setTitle(title); // Setear título actual
        } else if (id == R.id.nav_ETS) {
            datos.setInteger(Preferencias.KEY_ITEM_SELECTED, 5);
            title = item.getTitle().toString();
            args.putString(ETS.ARG_SECTION_TITLE, title);
            fragment = ETS.newInstance(title);
            fragment.setArguments(args);
            setTitle(title); // Setear título actual
        }else if (id == R.id.nav_Cerrar) {
            if (hayConexion()) {
                wipeCookie(datos.getURL());
                datos.setIsValid(false);
                startActivity(new Intent(this, LoginSaes.class));
                this.finish();
            }else{
                Toast.makeText(getApplicationContext(), "No hay conexión a Internet",
                        Toast.LENGTH_SHORT).show();
            }
        } else if(id == R.id.nav_Cambiar){
            if (hayConexion()) {
                wipeCookie(datos.getURL());
                datos.setIsValid(false);
                datos.setEscuela(null);
                datos.setURL(null);
                startActivity(new Intent(this, EscuelaActivity.class));
                this.finish();
            }else{
                Toast.makeText(getApplicationContext(), "No hay conexión a Internet",
                        Toast.LENGTH_SHORT).show();
            }
        } else if(id == R.id.nav_Recargar){
            if (hayConexion()) {
                startActivity(new Intent(this, SetearDatos.class));
                this.finish();
            }else{
                Toast.makeText(getApplicationContext(), "No hay conexión a Internet",
                        Toast.LENGTH_SHORT).show();
            }
        }else if(id == R.id.nav_Navegador){
            if (hayConexion()) {
                startActivity(new Intent(this, NavegadorActivity.class));
            }else{
                Toast.makeText(getApplicationContext(), "No hay conexión a Internet",
                        Toast.LENGTH_SHORT).show();
            }
        }else if(id == R.id.nav_ModSaes){
            if (hayConexion()) {
                startActivity(new Intent(this, ModSaesActivity.class));
            }else{
                Toast.makeText(getApplicationContext(), "No hay conexión a Internet",
                        Toast.LENGTH_SHORT).show();
            }
        } else if(id == R.id.nav_Configuracion){
            Intent i = new Intent(MainActivity.this, ChangeStyleActivity.class);
            i.putExtra("caller", MainActivity.class.getName());
            startActivity(i);
            MainActivity.this.finish();
        }

        // # Mostrar en pantalla
        if(fragment != null){
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
        }
    }

    public boolean hayConexion(){
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume(){
        String action = getIntent().getAction();
        // Prevent endless loop by adding a unique action, don't restart if action is present
        if(action == null || !action.equals("Already created")) {
            Intent intent = new Intent(MainActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }else
            getIntent().setAction(null);

        super.onResume();
        mFirebaseAnalytics.setCurrentScreen(this, getClass().getSimpleName(), null);
        if(datos.getBoleta() != null)
            boleta.setText(datos.getBoleta()+" • "+datos.getEscuela());
        else
            boleta.setText("N/D");

        if(datos.getNombre() != null)
            nombre.setText(datos.getNombre());
        else
            nombre.setText("N/D");

        Glide.with(background.getContext()).load(R.drawable.material).into(background);
        Glide.with(superbackground.getContext()).load(R.drawable.supermaterial).into(superbackground);
        superbackground.setColorFilter(datos.getColorSelected(), PorterDuff.Mode.SRC_IN);

        if(datos.getNivel() == 0){
            MedioSuperior item = MedioSuperior.ITEMS[datos.getIdDraw()];
            Glide.with(escuela.getContext())
                    .load(item.getIdDrawable())
                    .into(escuela);
        }else if(datos.getNivel() == 1){
            Superior item = Superior.ITEMS[datos.getIdDraw()];
            Glide.with(escuela.getContext())
                    .load(item.getIdDrawable())
                    .into(escuela);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressWarnings("deprecation")
    private void wipeCookie(String url){
        cookieSyncMngr.startSync();
        cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
        cookieSyncMngr.stopSync();
        cookieSyncMngr.sync();
    }
}
