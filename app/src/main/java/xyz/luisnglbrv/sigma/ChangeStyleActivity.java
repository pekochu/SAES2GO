package xyz.luisnglbrv.sigma;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import petrov.kristiyan.colorpicker.ColorPicker;

public class ChangeStyleActivity extends AppCompatActivity {

    private Preferencias data;
    private boolean restart = true;
    private Class callerClass;
    private static final String TAG = ChangeStyleActivity.class.getSimpleName();
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private View snackView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new Preferencias(this);
        setTheme(data.getStyle());
        setContentView(R.layout.activity_change_style);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.change_style_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }
        String caller = getIntent().getStringExtra("caller");
        try{ callerClass = Class.forName(caller); }catch(Exception e){ e.printStackTrace();}
        Button cambiarColor, checarUpdates, conocerMas;
        snackView = findViewById(R.id.change_Coordinator);
        final CheckBox updates, notifications;
        cambiarColor = (Button) findViewById(R.id.button_cs_1);
        cambiarColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ColorPicker colorPicker = new ColorPicker(ChangeStyleActivity.this);
                colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {
                        switch(position){
                            case 0:
                                data.setStyle(R.style.AppTheme_0);
                                data.setColorFooter("#90800000");
                                data.setColorTab("#DCB1B1");break;
                            case 1:
                                data.setStyle(R.style.AppTheme_1);
                                data.setColorFooter("#90009688");
                                data.setColorTab("#B2DFDB");break;
                            case 2:
                                data.setStyle(R.style.AppTheme_2);
                                data.setColorFooter("#90E91E63");
                                data.setColorTab("#F8BBD0");break;
                            case 3:
                                data.setStyle(R.style.AppTheme_3);
                                data.setColorFooter("#909C27B0");
                                data.setColorTab("#E1BEE7");break;
                            case 4:
                                data.setStyle(R.style.AppTheme_4);
                                data.setColorFooter("#90673AB7");
                                data.setColorTab("#D1C4E9");break;
                            case 5:
                                data.setStyle(R.style.AppTheme_5);
                                data.setColorFooter("#903F51B5");
                                data.setColorTab("#C5CAE9");break;
                            case 6:
                                data.setStyle(R.style.AppTheme_6);
                                data.setColorFooter("#9003A9F4");
                                data.setColorTab("#B3E5FC");break;
                            case 7:
                                data.setStyle(R.style.AppTheme_7);
                                data.setColorFooter("#9000BCD4");
                                data.setColorTab("#B2EBF2");break;
                            case 8:
                                data.setStyle(R.style.AppTheme_8);
                                data.setColorFooter("#90F44336");
                                data.setColorTab("#FFCDD2");break;
                            case 9:
                                data.setStyle(R.style.AppTheme_9);
                                data.setColorFooter("#904CAF50");
                                data.setColorTab("#C8E6C9");break;
                            case 10:
                                data.setStyle(R.style.AppTheme_10);
                                data.setColorFooter("#908BC34A");
                                data.setColorTab("#DCEDC8");break;
                            case 11:
                                data.setStyle(R.style.AppTheme_11);
                                data.setColorFooter("#90CDDC39");
                                data.setColorTab("#F0F4C3");break;
                            case 12:
                                data.setStyle(R.style.AppTheme_12);
                                data.setColorFooter("#90FFC107");
                                data.setColorTab("#FFECB3");break;
                            case 13:
                                data.setStyle(R.style.AppTheme_13);
                                data.setColorFooter("#90FF9800");
                                data.setColorTab("#FFE0B2");break;
                            case 14:
                                data.setStyle(R.style.AppTheme_14);
                                data.setColorFooter("#90FF5722");
                                data.setColorTab("#FFCCBC");break;
                        }
                        data.setColorSelected(color);
                        ChangeStyleActivity.this.recreate();
                        colorPicker.dismissDialog();
                    }
                    @Override
                    public void onCancel() { colorPicker.dismissDialog(); }
                }).setTitle("Elige tu color favorito").setColors(R.array.lista_colores).
                        setDefaultColorButton(data.getColorSelected()).
                        setRoundColorButton(true).setColumns(5).show();
            }
        });
        updates = (CheckBox) findViewById(R.id.check_cs_1);
        updates.setChecked(data.getUpdates());
        updates.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setUpdates(isChecked);
            }
        });
        notifications = (CheckBox) findViewById(R.id.check_cs_2);
        notifications.setChecked(data.getNotificationActivated());
        notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(data.getHorarioGuardado()){
                    data.setNotificationsActivated(isChecked);
                }else{
                    if(isChecked){
                        Snackbar.make(snackView, "¡No guardaste tu horario!",
                                Snackbar.LENGTH_LONG).show();
                        notifications.setChecked(false);
                    }
                }
            }
        });
        checarUpdates = (Button) findViewById(R.id.button_cs_2);
        checarUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hayConexion()) {
                    startActivity(new Intent(ChangeStyleActivity.this, UpdatesActivity.class));
                }else{
                    Snackbar.make(snackView, "¡No hay conexión a Internet!",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
        conocerMas = (Button) findViewById(R.id.button_cs_3);
        conocerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangeStyleActivity.this, AboutActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            if(restart){
                ChangeStyleActivity.this.finish();
                startActivity(new Intent(ChangeStyleActivity.this, callerClass));
            } else { super.onBackPressed(); }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(restart){
            ChangeStyleActivity.this.finish();
            startActivity(new Intent(ChangeStyleActivity.this, callerClass));
        } else { super.onBackPressed(); }
    }

    public boolean hayConexion(){
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
