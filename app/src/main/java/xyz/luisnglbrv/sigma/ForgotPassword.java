package xyz.luisnglbrv.sigma;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.InputStream;

import xyz.luisnglbrv.sigma.escuelas.MedioSuperior;
import xyz.luisnglbrv.sigma.escuelas.Superior;

public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";
    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText boleta_Text, captcha_Text;
    private TextInputLayout mFloatLabelBoleta, mFloatLabelCaptcha;
    private Button submit_Boton;
    private WebView webview;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private ImageView iconEscuela;
    private Preferencias datos;
    private String boleta = "", captcha = "";
    private String saes_escuela = "about:blank";
    private boolean activityActive;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datos = new Preferencias(this);
        setTheme(datos.getStyle());
        setContentView(R.layout.activity_forgot_password);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.forgotPassword_toolbar);
        setSupportActionBar(mToolbar);
        activityActive = true;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        if(datos.getURL() == null){
            startActivity(new Intent(ForgotPassword.this, EscuelaActivity.class));
            this.finish();
        }else{ saes_escuela = datos.getURL(); }

        if(datos.getIsValid()) {
            startActivity(new Intent(ForgotPassword.this, SetearDatos.class));
            this.finish();
        }

        // ===== Asignar las Views a sus variables ===== //
        iconEscuela = (ImageView) findViewById(R.id.iconEscuela);
        boleta_Text = (EditText) findViewById(R.id.boleta_editText);
        captcha_Text = (EditText) findViewById(R.id.captcha_editText);
        mFloatLabelBoleta = (TextInputLayout) findViewById(R.id.float_label_boleta);
        mFloatLabelCaptcha = (TextInputLayout) findViewById(R.id.float_label_captcha);
        submit_Boton = (Button) findViewById(R.id.submit_boton);
        // ===== Obtener datos para escribirlos en la UI ===== //
        if(datos.getBoleta() != null && datos.getRecordar()){ boleta_Text.setText(datos.getBoleta()); }
        // ===== Configurar el WebView ===== //
        webview = (WebView) findViewById(R.id.captcha_webView);
        webview.getSettings().setJavaScriptEnabled(true);
        // Desactivar zoom y scrolls
        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setDisplayZoomControls(false);
        webview.setVerticalScrollBarEnabled(false);
        webview.setHorizontalScrollBarEnabled(false);
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        // Cargar URL
        webview.loadUrl(saes_escuela+"/SendEmail/PruebaSendMail.aspx");
        // ===== Cargar la barra de progreso hasta que cargue el Captcha ===== //
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Obteniendo un captcha");
        progressDialog.setMessage("Por favor, espera...");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "CANCELAR",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.dismiss();
                        startActivity(new Intent(ForgotPassword.this, EscuelaActivity.class));
                        ForgotPassword.this.finish();
                    }
                });
        progressDialog.show();
        // ===== Cargar el WebView y configurar sus eventos ===== //
        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "Carga del captcha finalizada. #");
                super.onPageFinished(view, url);
                aplicarCSS();
                view.setVisibility(View.VISIBLE);
                if (ForgotPassword.this.progressDialog.isShowing()) {
                    ForgotPassword.this.progressDialog.dismiss();
                }

                if(!captcha_Text.getText().toString().equals("")){
                    webview.loadUrl("javascript: (function(){ alert(" +
                            "(document.getElementById('ctl00_mainCopy_Lbl_correo') == null) ? " +
                            "document.getElementById('ctl00_mainCopy_Lbl_error').innerText+'|'+" +
                            "'|'+" +
                            "document.getElementById('ctl00_mainCopy_Lbl_dato').innerText : " +
                            "document.getElementById('ctl00_mainCopy_Lbl_error').innerText+'|'+" +
                            "document.getElementById('ctl00_mainCopy_Lbl_correo').innerText+'|'+" +
                            "document.getElementById('ctl00_mainCopy_Lbl_dato').innerText)" +
                            "})()");
                    captcha_Text.setText("");
                }
            }
            @SuppressWarnings("deprecation")
            public void onReceivedError(WebView view, int errorCod ,String description,
                                        String failingUrl){
                Log.e(TAG, "Error."+description+" #");
                if (ForgotPassword.this.progressDialog.isShowing()) {
                    ForgotPassword.this.progressDialog.dismiss();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                builder.setTitle("¡Atención!");
                builder.setMessage("El SAES de "+datos.getEscuela()+" esta offline. "+
                        "Lamentamos los incovenientes.\nDetalle: "
                        +String.valueOf(description));
                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        datos.setURL(null);
                        alertDialog.dismiss();
                        startActivity(new Intent(ForgotPassword.this, EscuelaActivity.class));
                        ForgotPassword.this.finish();
                    }
                });

                alertDialog = builder.create();
                if(activityActive){
                    alertDialog.show();
                }
            }
            @SuppressWarnings("deprecation")
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "Se cargará la URL: " + url + " #");
                return !url.contains("saes") && !url.contains("148.204.250.26");
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                if(datos.getBoolean(Preferencias.KEY_SSL_ACCEPTED+saes_escuela)){
                    handler.proceed();
                    return;
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
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
                        startActivity(new Intent(ForgotPassword.this, EscuelaActivity.class));
                        ForgotPassword.this.finish();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                // # Obtener mensaje del SAES
                String[] saesMensaje = message.split("\\|");
                // # Crear Dialogo de Alerta
                AlertDialog alertDialog;
                AlertDialog.Builder aviso = new AlertDialog.Builder(ForgotPassword.this);
                aviso.setTitle("Información");
                aviso.setMessage("Estado: "+saesMensaje[0]+"\n"+saesMensaje[1]+"\n"+saesMensaje[2]);
                aviso.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alertDialog = aviso.create();
                if(activityActive){
                    alertDialog.show();
                }
                result.confirm();
                return true;
            }
        });

        submit_Boton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hayConexion()){
                    intentarMandarCorreo();
                }else{
                    Snackbar.make(findViewById(R.id.forgot_Coordinator), "No tienes conexión a Internet :(",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            datos.setEscuela(null);
            datos.setURL(null);
            startActivity(new Intent(ForgotPassword.this, EscuelaActivity.class));
            this.finish();
            return true;
        }else if(id == R.id.action_settings_horario){
            if(datos.getHorarioGuardado()){
                startActivity(new Intent(ForgotPassword.this, HorarioOffline.class));
            }else{
                Toast.makeText(this, "No guardaste tu horario", Toast.LENGTH_SHORT).show();
            }
            return true;
        }else if(id == R.id.action_config){
            Intent i = new Intent(ForgotPassword.this, ChangeStyleActivity.class);
            i.putExtra("caller", LoginSaes.class.getName());
            startActivity(i);
            ForgotPassword.this.finish();
            return true;
        }else if(id == R.id.action_reload_captcha){
            webview.setVisibility(View.INVISIBLE);
            webview.reload();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void intentarMandarCorreo() {
        // Reiniciar errores
        mFloatLabelBoleta.setError(null);
        mFloatLabelCaptcha.setError(null);

        // Guardando valores
        boleta = boleta_Text.getText().toString();
        captcha = captcha_Text.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Verificar si la boleta tiene contenido
        if (TextUtils.isEmpty(boleta)) {
            mFloatLabelBoleta.setError(getString(R.string.error_campo_requerido));
            if(focusView == null)
                focusView = mFloatLabelBoleta;
            cancel = true;
        }

        // Verificar si el ID tiene contenido.
        if (TextUtils.isEmpty(captcha)) {
            mFloatLabelCaptcha.setError(getString(R.string.error_campo_requerido));
            if(focusView == null)
                focusView = mFloatLabelCaptcha;
            cancel = true;
        }

        if (cancel) {
            // Hubo un error, enfocar en el campo con error.
            focusView.requestFocus();
        } else {
            mandarCorreo();
        }
    }

    public void aplicarCSS(){
        try{
            webview.loadUrl("javascript:(function() {" +
                    "var imageSrc = document.getElementById('c_sendemail_pruebasendmail_ctl00_maincopy_examplecaptcha_CaptchaImage').src;" +
                    "document.getElementsByTagName('div')[0].innerHTML += " +
                    "\"<div style='position:absolute;z-index:1000;width:100%;height:100%;top:0;left:0;background-color:#fff'>" +
                    "<img src='\"+imageSrc+\"'style='width:initial;height:100%' alt='CAPTCHA'>" +
                    "</div>\";"+
                    "})()");
            Log.d(TAG, "CSS Aplicado. #");
        }catch (Exception e){
            Log.e(TAG, "aplicarCSS ~ Error: "+e.getMessage()+". #");
            e.printStackTrace();
        }
    }

    public void mandarCorreo(){
        try{
            webview.loadUrl("javascript:(function() {" +
                    "document.aspnetForm.ctl00_mainCopy_Txt_usuario.value = \""+boleta+"\""
                    +";document.aspnetForm.ctl00_mainCopy_CaptchaCodeTextBox.value = \""+captcha+"\""
                    +";document.getElementById('ctl00_mainCopy_sndmail').click();})()");
            Log.d(TAG, "Enviando solicitud #");
        }catch (Exception e){
            Log.e(TAG, "Submit ~ Error: "+e.getMessage()+". #");
            e.printStackTrace();
        }
    }

    public boolean hayConexion(){
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityActive = true;
        if(datos.getNivel() == 0){
            MedioSuperior item = MedioSuperior.ITEMS[datos.getIdDraw()];
            Glide.with(iconEscuela.getContext())
                    .load(item.getAlphaDrawable())
                    .into(iconEscuela);
        }else if(datos.getNivel() == 1){
            Superior item = Superior.ITEMS[datos.getIdDraw()];
            Glide.with(iconEscuela.getContext())
                    .load(item.getAlphaDrawable())
                    .into(iconEscuela);
        }
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
        if (ForgotPassword.this.progressDialog.isShowing()) {
            ForgotPassword.this.progressDialog.dismiss();
        }
    }
}
