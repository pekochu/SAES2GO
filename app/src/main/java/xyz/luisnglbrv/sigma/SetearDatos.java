package xyz.luisnglbrv.sigma;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.skyfishjy.library.RippleBackground;

import xyz.luisnglbrv.sigma.dataProvider.DataSAES;

public class SetearDatos extends AppCompatActivity {

    private static final String TAG = SetearDatos.class.getSimpleName();
    // Variables para Firebase
    private FirebaseAnalytics mFirebaseAnalytics;
    // Clases
    private Preferencias pref;
    private DataSAES obtainData;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private AlertDialog alertDialog;
    private static ProgressBar barraProgreso;
    // Variables de diseño
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new Preferencias(SetearDatos.this);
        obtainData = new DataSAES(SetearDatos.this, 0);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_setear_datos);
        btnCancel = findViewById(R.id.cancelButton);
        ImageView appIcon = findViewById(R.id.appIconView);
        barraProgreso = findViewById(R.id.barraProgresoDatos);
        appIcon.setColorFilter(pref.getColorSelected(), PorterDuff.Mode.SRC_IN);
        appIcon.setImageAlpha(appIcon.getImageAlpha() - 10);

        final RippleBackground rippleBackground = findViewById(R.id.contentRipple);
        rippleBackground.startRippleAnimation();

        if (haveInternet()) {
            try{
                Thread.sleep(50);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            obtainData.execute();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("¡No tienes conexión!");
            builder.setMessage("No pudimos conectarnos al SAES.");
            builder.setPositiveButton("CERRAR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    alertDialog.dismiss();
                    SetearDatos.this.finish();
                }
            });
            builder.setCancelable(false);
            if(pref.getHorarioGuardado()){
                builder.setMessage("No pudimos conectarnos al SAES. Pero tienes la opción de ver" +
                        " tu horario o cerrar la app.");
                builder.setNegativeButton("VER HORARIO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialog.dismiss();
                        startActivity(new Intent(SetearDatos.this, HorarioOffline.class));
                        SetearDatos.this.finish();
                        }
                });
            }
            alertDialog = builder.create();
            alertDialog.show();
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetearDatos.this, "Cancelando...", Toast.LENGTH_SHORT).show();
                try{ obtainData.cancel(true); }
                catch(Exception ioe){ ioe.printStackTrace(); }
                SetearDatos.this.finish();
            }
        });
    }

    public boolean haveInternet(){
        try {
            connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connMgr.getActiveNetworkInfo();
        }catch (NullPointerException ex){ ex.printStackTrace();}
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static void actualizarBarra(int arg){
        if(Build.VERSION.SDK_INT <= 23)
            barraProgreso.setProgress(arg);
        else
            barraProgreso.setProgress(arg, true);
    }

    @Override
    public void onBackPressed(){
        // Nothing
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAnalytics.setCurrentScreen(this, getClass().getSimpleName(), null);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }
}
