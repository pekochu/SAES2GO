package xyz.luisnglbrv.sigma;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class NavegadorActivity extends AppCompatActivity {

    private static final String TAG = "NavegadorActivity";
    private FirebaseAnalytics mFirebaseAnalytics;

    private WebView webview;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private Preferencias datos;
    private String saes_escuela = "about:blank";
    private boolean activityActive;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datos = new Preferencias(this);
        setTheme(datos.getStyle());
        setContentView(R.layout.activity_navegador);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.navegador_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){actionBar.setDisplayHomeAsUpEnabled(true);}

        activityActive = true;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if(datos.getURL() != null){
            saes_escuela = datos.getURL();
        }
        webview = (WebView) findViewById(R.id.navegador);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.loadUrl(saes_escuela+"/alumnos/default.aspx");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Por favor, espera...");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "CANCELAR",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.dismiss();
                        NavegadorActivity.this.finish();
                }
        });
        progressDialog.show();

        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "Carga del captcha finalizada. #");
                super.onPageFinished(view, url);
                if (NavegadorActivity.this.progressDialog.isShowing()) {
                    NavegadorActivity.this.progressDialog.dismiss();
                }
                if(!datos.getFirstNotice()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(NavegadorActivity.this);
                    builder.setTitle("¡Atención!");
                    builder.setMessage(getResources().getString(R.string.warning_2));
                    builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            alertDialog.dismiss();
                            datos.setFirstNotice(true);
                        }
                    });
                    alertDialog = builder.create();
                    if(activityActive){
                        alertDialog.show();
                    }
                }
            }
            @SuppressWarnings("deprecation")
            public void onReceivedError(WebView view, int errorCod ,String description,
                                        String failingUrl){
                Log.e(TAG, "Error."+description+" #");
                if (NavegadorActivity.this.progressDialog.isShowing()) {
                    NavegadorActivity.this.progressDialog.dismiss();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(NavegadorActivity.this);
                builder.setTitle("¡Atención!");
                builder.setMessage("El SAES de tu escuela esta offline. " +
                        "Vuelve a la pantalla anterior.\n\n"
                        +String.valueOf(description).substring(5));
                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialog.dismiss();
                        NavegadorActivity.this.finish();
                    }
                });
                alertDialog = builder.create();
                if(activityActive){
                    alertDialog.show();
                }
            }
            @SuppressWarnings("deprecation")
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "Se cargará la URL: "+url+" #");
                if(url.contains("saes")){
                    return false;
                }else return !url.contains("148.204.250.26");
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                if(datos.getBoolean(Preferencias.KEY_SSL_ACCEPTED+saes_escuela)){
                    handler.proceed();
                    return;
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(NavegadorActivity.this);
                builder.setMessage("Hay un error con el certificado SSL.\n¿Desea continuar?");
                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                        datos.setBoolean(Preferencias.KEY_SSL_ACCEPTED+saes_escuela, true);
                        datos.setSsl(true);
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                        datos.setBoolean(Preferencias.KEY_SSL_ACCEPTED+saes_escuela, false);
                        datos.setSsl(false);
                        startActivity(new Intent(NavegadorActivity.this, EscuelaActivity.class));
                        NavegadorActivity.this.finish();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                // # Obtener mensaje de error del SAES
                int toLetter = 2, toEndLetter = 0;
                String saesMessage;
                for(int i = 0; i < message.substring(2, message.length()-2).length(); i++){
                    if(message.substring(2).charAt(i) == ' '){
                        toLetter++;
                    }else{
                        break;
                    }
                }
                for(int i = message.substring(2, message.length()-2).length()-1; i > 0; i--){
                    if(message.substring(2, message.length()-2).charAt(i) == ' '){
                        toEndLetter++;
                    }else{
                        break;
                    }
                }
                Log.i(TAG, "## toEndLetter: "+String.valueOf(toEndLetter)+" ##");
                saesMessage = message.substring(toLetter, message.length() - (toEndLetter + 3));
                saesMessage = saesMessage.toLowerCase();
                saesMessage = saesMessage.substring(0, 1).toUpperCase() + saesMessage.substring(1);
                // # Obtener mensaje de error del SAES
                Snackbar.make(findViewById(R.id.login_Coordinator), saesMessage,
                        Snackbar.LENGTH_LONG).show();
                result.confirm();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            NavegadorActivity.this.finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAnalytics.setCurrentScreen(this, getClass().getSimpleName(), null);
    }

    @Override
    protected void onStop(){
        super.onStop();
        activityActive = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (NavegadorActivity.this.progressDialog.isShowing()) {
            NavegadorActivity.this.progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavegadorActivity.this.finish();
    }
}
