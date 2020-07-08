package xyz.luisnglbrv.sigma;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

import xyz.luisnglbrv.sigma.escuelas.MedioSuperior;
import xyz.luisnglbrv.sigma.escuelas.Superior;

public class LoginSaes extends AppCompatActivity {

    private static final String TAG = "LoginSaesActivity";
    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText boleta_Text, contrasena_Text, captcha_Text;
    private TextInputLayout mFloatLabelBoleta, mFloatLabelContrasena, mFloatLabelCaptcha;
    private Button submit_Boton, forgot_Boton;
    private CheckBox recordar;
    private WebView webview;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private Preferencias datos;
    private CookieSyncManager cookieSyncMngr;
    private CookieManager cookieManager;
    private ImageView iconEscuela;
    private String boleta = "", contrasena = "", captcha = "";
    private String saes_escuela = "about:blank";
    private boolean activityActive = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datos = new Preferencias(this);
        cookieSyncMngr = CookieSyncManager.createInstance(this);
        cookieManager = CookieManager.getInstance();
        setTheme(datos.getStyle());
        setContentView(R.layout.activity_login_saes);
        Toolbar mToolbar = findViewById(R.id.loginSaes_toolbar);
        setSupportActionBar(mToolbar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        if(datos.getURL() == null){
            startActivity(new Intent(LoginSaes.this, EscuelaActivity.class));
            this.finish();
        }else{ saes_escuela = datos.getURL(); }

        if(datos.getIsValid()) {
            startActivity(new Intent(LoginSaes.this, SetearDatos.class));
            this.finish();
        }

        // ===== Asignar las Views a sus variables ===== //
        iconEscuela = (ImageView) findViewById(R.id.iconEscuela);
        boleta_Text = (EditText) findViewById(R.id.boleta_editText);
        contrasena_Text = (EditText) findViewById(R.id.contrasena_editText);
        captcha_Text = (EditText) findViewById(R.id.captcha_editText);
        mFloatLabelBoleta = (TextInputLayout) findViewById(R.id.float_label_boleta);
        mFloatLabelContrasena = (TextInputLayout) findViewById(R.id.float_label_contrasena);
        mFloatLabelCaptcha = (TextInputLayout) findViewById(R.id.float_label_captcha);
        recordar = (CheckBox) findViewById(R.id.recordar_checkBox);
        submit_Boton = (Button) findViewById(R.id.submit_boton);
        forgot_Boton = (Button) findViewById(R.id.forgot_boton);
        // ===== Obtener datos para escribirlos en la UI ===== //
        if(datos.getBoleta() != null && datos.getRecordar()){ boleta_Text.setText(datos.getBoleta()); }
        if(datos.getPass() != null && datos.getRecordar()){ contrasena_Text.setText(datos.getPass()); }
        if(datos.getRecordar()) {
            recordar.setChecked(true);
            mFloatLabelContrasena.setPasswordVisibilityToggleEnabled(true);
            contrasena_Text.setEnabled(false);
            boleta_Text.setEnabled(false);
            captcha_Text.requestFocus();
        }
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
        webview.loadUrl(saes_escuela);
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
                    startActivity(new Intent(LoginSaes.this, EscuelaActivity.class));
                    LoginSaes.this.finish();
                }
        });
        if(datos.getHorarioGuardado()){
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "VER HORARIO",
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressDialog.dismiss();
                    startActivity(new Intent(LoginSaes.this, HorarioOffline.class));
                    LoginSaes.this.finish();
                }
            });
        }
        progressDialog.show();
        // ===== Cargar el WebView y configurar sus eventos ===== //
        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "Carga del captcha finalizada. #");
                super.onPageFinished(view, url);
                aplicarCSS();
                view.setVisibility(View.VISIBLE);
                if (LoginSaes.this.progressDialog.isShowing()) {
                    LoginSaes.this.progressDialog.dismiss();
                }
                if (checkCookie(saes_escuela)) {
                    Log.d(TAG, " Cookie almacenada. Inicio de sesión exitoso. #");
                    startActivity(new Intent(LoginSaes.this, SetearDatos.class));
                    LoginSaes.this.finish();
                }else{
                    if(!captcha_Text.getText().toString().equals("")){
                        webview.loadUrl("javascript: (function(){ var message = " +
                                "document.getElementsByClassName('failureNotification')[2].innerText;"+
                                "alert(message);})()");
                        captcha_Text.setText("");
                    }
                }
            }


            // disable scroll on touch
            @SuppressWarnings("deprecation")
            public void onReceivedError(WebView view, int errorCod ,String description,
                                        String failingUrl){
                Log.e(TAG, "Error."+description+" #");
                if (LoginSaes.this.progressDialog.isShowing()) {
                    LoginSaes.this.progressDialog.dismiss();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginSaes.this);
                builder.setTitle("¡Atención!");
                builder.setMessage("El SAES de "+datos.getEscuela()+" esta offline. "+
                        "Lamentamos los incovenientes.\nDetalle: "+
                        String.valueOf(description));
                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        datos.setURL(null);
                        alertDialog.dismiss();
                        startActivity(new Intent(LoginSaes.this, EscuelaActivity.class));
                        LoginSaes.this.finish();
                    }
                });
                if(datos.getHorarioGuardado()){
                    builder.setMessage("El SAES de "+datos.getEscuela()+" esta offline. "+
                            "Lamentamos los incovenientes, pero si así lo deseas, " +
                            "puedes checar tu horario.\nDetalle: "+
                            String.valueOf(description));
                    builder.setNegativeButton("VER HORARIO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            alertDialog.dismiss();
                            startActivity(new Intent(LoginSaes.this, HorarioOffline.class));
                        }
                    });
                }
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginSaes.this);
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
                        startActivity(new Intent(LoginSaes.this, EscuelaActivity.class));
                        LoginSaes.this.finish();
                    }
                });
                final AlertDialog dialog = builder.create();
                if(activityActive){
                    dialog.show();
                }
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                // # Obtener mensaje de error del SAES
                //int toLetter = 2, toEndLetter = 0;
                Log.i(TAG, " # OnJsAlert: message = "+message);
                String saesMessage;
                if(message.length() == 0) {
                    result.confirm();
                    return true;
                }
                /*
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
                */
                //Log.i(TAG, "## toEndLetter: "+String.valueOf(toEndLetter)+" ##");
                //saesMessage = message.substring(toLetter, message.length() - (toEndLetter + 3));
                //saesMessage = saesMessage.toLowerCase();
                saesMessage = message.toLowerCase();
                saesMessage = saesMessage.substring(0, 1).toUpperCase() + saesMessage.substring(1);
                // # Obtener mensaje de error del SAES
                Snackbar.make(findViewById(R.id.login_Coordinator), saesMessage,
                        Snackbar.LENGTH_LONG).show();
                result.confirm();
                return true;
            }
        });


        submit_Boton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hayConexion()){
                    intentarIniciarSesion();
                }else{
                    Snackbar.make(findViewById(R.id.login_Coordinator), "No tienes conexión a Internet :(",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        forgot_Boton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hayConexion()){
                    startActivity(new Intent(LoginSaes.this, ForgotPassword.class));
                }else{
                    Snackbar.make(findViewById(R.id.login_Coordinator), "No tienes conexión a Internet :(",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        recordar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatLabelContrasena.setPasswordVisibilityToggleEnabled(!recordar.isChecked());
                contrasena_Text.setEnabled(!recordar.isChecked());
                boleta_Text.setEnabled(!recordar.isChecked());
                datos.setRecordar(recordar.isChecked());
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
            startActivity(new Intent(LoginSaes.this, EscuelaActivity.class));
            this.finish();
            return true;
        }else if(id == R.id.action_settings_horario){
            if(datos.getHorarioGuardado()){
                startActivity(new Intent(LoginSaes.this, HorarioOffline.class));
            }else{
                Toast.makeText(this, "No guardaste tu horario", Toast.LENGTH_SHORT).show();
            }
            return true;
        }else if(id == R.id.action_config){
            Intent i = new Intent(LoginSaes.this, ChangeStyleActivity.class);
            i.putExtra("caller", LoginSaes.class.getName());
            startActivity(i);
            LoginSaes.this.finish();
            return true;
        }else if(id == R.id.action_reload_captcha){
            webview.setVisibility(View.INVISIBLE);
            webview.reload();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void intentarIniciarSesion() {
        // Reiniciar errores
        mFloatLabelBoleta.setError(null);
        mFloatLabelContrasena.setError(null);
        mFloatLabelCaptcha.setError(null);

        // Guardando valores
        boleta = boleta_Text.getText().toString();
        contrasena = contrasena_Text.getText().toString();
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

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(contrasena)) {
            mFloatLabelContrasena.setError(getString(R.string.error_campo_requerido));
            if(focusView == null)
                focusView = mFloatLabelContrasena;
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
            datos.setBoleta(boleta);
            datos.setPass(contrasena);
            iniciarSesion();
        }
    }

    public void aplicarCSS(){
        try{
            /*
            localObject = getAssets().open("main.css");
            byte[] arrayOfByte = new byte[((InputStream)localObject).available()];
            ((InputStream)localObject).read(arrayOfByte);
            ((InputStream)localObject).close();
            localObject = Base64.encodeToString(arrayOfByte, 2);
            webview.loadUrl("javascript:(function() {var parent = document.getElementsByTagName" +
                    "('head').item(0);var style = document.createElement('style');style.type = " +
                    "'text/css';style.innerHTML = window.atob('" + localObject + "');" +
                    "parent.appendChild(style)" + "})()");
            */
            /*
            webview.loadUrl("javascript:(function() {" +
                    "var imageSrc = document.getElementById('c_default_ctl00_leftcolumn_loginuser_logincaptcha_CaptchaImage').src;" +
                    "var innerBody = document.getElementsByTagName('body')[0].innerHTML;"+
                    "document.getElementsByTagName('div')[0].innerHTML += " +
                    "\"<div style='position:absolute;z-index:1000;width:100%;height:100%;top:0;left:0;background-color:#fff'>" +
                    "<img src='\"+imageSrc+\"'style='width:initial;height:100%;z-index:5000;' alt='CAPTCHA'>" +
                    "</div>\";"+
                    "if(document.getElementsByTagName('header')[0] != undefined)document.getElementsByTagName('header')[0].style.display ='none';})()");
                    */
            StringBuilder divScript = new StringBuilder();
            divScript.append("\"<div style='position:absolute;z-index:5000;width:100%;height:100%;top:0;left:0;background-color:#fff;text-align:center;'>");
            divScript.append("<img src='\"+imageSrc+\"' style='width:initial;height:100%' alt='CAPTCHA'>");
            divScript.append("</div>\"");
            StringBuilder injectScript = new StringBuilder();
            injectScript.append("javascript:(function() {");
            injectScript.append("var imageSrc = document.getElementById('c_default_ctl00_leftcolumn_loginuser_logincaptcha_CaptchaImage').src;");
            injectScript.append("if(document.getElementsByTagName('header')[0] == undefined){");
            injectScript.append("var innerBody = document.getElementsByTagName('body')[0].innerHTML;");
            injectScript.append("document.getElementsByTagName('body')[0].innerHTML = ");
            injectScript.append(divScript);
            injectScript.append("+innerBody;");
            injectScript.append("}else{");
            injectScript.append("document.getElementsByTagName('header')[0].innerHTML = ");
            injectScript.append(divScript);
            injectScript.append(";}})()");
            Log.i("JsScript", injectScript.toString());
            webview.loadUrl(injectScript.toString());
            Log.i(TAG, "CSS Aplicado. #");
        }catch (Exception e){
            Log.e(TAG, "aplicarCSS ~ Error: "+e.getMessage()+". #");
            e.printStackTrace();
        }
    }

    public void iniciarSesion(){
        try{
            webview.loadUrl("javascript:(function() {" +
                    "document.aspnetForm.ctl00_leftColumn_LoginUser_UserName.value = \""+boleta+"\""
                    +";document.aspnetForm.ctl00_leftColumn_LoginUser_Password.value = \""+contrasena+"\""
                    +";document.aspnetForm.ctl00_leftColumn_LoginUser_CaptchaCodeTextBox.value = \""+captcha+"\""
                    +";document.getElementById('ctl00_leftColumn_LoginUser_LoginButton').click();})()");
            Log.d(TAG, "Iniciando sesión... #");
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

    private boolean checkCookie(String url){
        String cookie;
        cookie = getCookie(url);
        Boolean cookieValid = false;
        if (cookie != null){
            cookieValid = false;
            if (!cookie.isEmpty()){
                cookieValid = cookie.toUpperCase().contains("ASPXFORMSAUTH");
            }
        }
        Log.d(TAG, "Cookies verificadas.");
        return cookieValid;
    }

    private String getCookie(String url) {
        String str = "";
        if (cookieManager.hasCookies()) {
            str = cookieManager.getCookie(url);
        }
        Log.d(TAG, "Cookies obtenidas.");
        return str;
    }

    @SuppressWarnings("deprecation")
    private void wipeCookie(String url){
        saes_escuela = getCookie(url);
        cookieSyncMngr.startSync();
        cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
        cookieSyncMngr.stopSync();
        cookieSyncMngr.sync();
        Log.d(TAG, "Cookies limpiadas.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityActive = true;
        mFirebaseAnalytics.setCurrentScreen(this, getClass().getSimpleName(), null);
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
    }

    @Override
    protected void onStop(){
        super.onStop();
        activityActive = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activityActive = false;
        if (LoginSaes.this.progressDialog.isShowing()) {
            LoginSaes.this.progressDialog.dismiss();
        }
    }
}
