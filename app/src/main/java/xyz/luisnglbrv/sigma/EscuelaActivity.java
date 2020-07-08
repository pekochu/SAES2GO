package xyz.luisnglbrv.sigma;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import xyz.luisnglbrv.sigma.escuelas.AdaptadorMedio;
import xyz.luisnglbrv.sigma.escuelas.AdaptadorSuperior;
import xyz.luisnglbrv.sigma.escuelas.EscuelaTabAdapter;

/**
 * Created by LuisAngel on 21/12/16.
 */

public class EscuelaActivity extends AppCompatActivity {

    private Preferencias datos;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datos = new Preferencias(this);
        setTheme(datos.getStyle());
        setContentView(R.layout.activity_elegir_escuela);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.escuela_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.escuela_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.escuela_ViewPager);
        mTabLayout.setTabTextColors( //Color de los textos del tablayout
                Color.parseColor(datos.getColorTab()),
                Color.parseColor(getResources().getString(R.string.tab_SelectedText)));
        mTabLayout.setSelectedTabIndicatorColor( //Color del indicador
                Color.parseColor(getResources().getString(R.string.tab_Indicator)));
        setSupportActionBar(mToolbar);
        setupViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        EscuelaTabAdapter adapter = new EscuelaTabAdapter(getSupportFragmentManager());
        adapter.add("MEDIO SUPERIOR");
        adapter.add("SUPERIOR");
        viewPager.setAdapter(adapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_escuela, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.escuela_settings_horario) {
            if(datos.getHorarioGuardado()){
                startActivity(new Intent(EscuelaActivity.this, HorarioOffline.class));
            }else{
                Toast.makeText(this, "No guardaste tu horario", Toast.LENGTH_SHORT).show();
            }
            return true;
        }else if(id == R.id.action_config){
            Intent i = new Intent(EscuelaActivity.this, ChangeStyleActivity.class);
            i.putExtra("caller", EscuelaActivity.class.getName());
            startActivity(i);
            EscuelaActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
