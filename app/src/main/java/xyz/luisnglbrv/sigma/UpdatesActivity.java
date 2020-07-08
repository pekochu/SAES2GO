package xyz.luisnglbrv.sigma;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Iterator;

public class UpdatesActivity extends AppCompatActivity {

    //private WebView webview;
    private ProgressDialog progressDialog;
    private static final String TAG = "UpdatesActivity";
    private FirebaseAnalytics mFirebaseAnalytics;
    private String url = "file:///android_asset/default.html";
    private String url_update = "http://www.luisnglbrv.xyz/p/sigma-updates.html";
    private int timeout = 15*1000, versionToInt = 0;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private boolean hasPermission;
    private AlertDialog alertDialog;
    private boolean activityActive;
    private TextView version_installed, new_version, last_version_date, change_log, message;
    private TextView view1;
    //private RelativeLayout relative_webView;
    private Preferencias datos;
    private String version = "0", update = "", date = "No se pudo obtener el dato.",
            changes = "Sin datos.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datos = new Preferencias(this);
        setTheme(datos.getStyle());
        setContentView(R.layout.activity_updates);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.updates_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //relative_webView = (RelativeLayout) findViewById(R.id.updates_relative_webView);
        version_installed = (TextView) findViewById(R.id.update_installed_version);
        new_version = (TextView) findViewById(R.id.update_new_version);
        last_version_date = (TextView) findViewById(R.id.update_new_version_date);
        last_version_date.setTextColor(datos.getColorSelected());
        change_log = (TextView) findViewById(R.id.update_change_log);
        change_log.setTextColor(datos.getColorSelected());
        message = (TextView) findViewById(R.id.update_message);
        view1 = (TextView) findViewById(R.id.textView8);
        version_installed.setText(BuildConfig.VERSION_NAME);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        activityActive = true;

        hasPermission = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.
                WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        /*
        webview = (WebView) findViewById(R.id.updates_webView);
        webview.loadUrl(url);
        */
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Conectándose con el servidor");
        progressDialog.setMessage("Por favor, espera...");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "CANCELAR",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UpdatesActivity.this.finish();
                    }
                });
        new JsoupAsyncTask().execute();
        if(activityActive){progressDialog.show();}

        //---------------- Escucha de descarga -------------------
        /*
        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                if (!hasPermission) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdatesActivity.this);
                    builder.setMessage("Para descargar esta actualización, necesitamos que " +
                            "nos otorgues permisos para poder escribir datos en tu unidad de " +
                            "almacenamiento.")
                            .setTitle("¡Permisos requeridos!");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            alertDialog.dismiss();
                            ActivityCompat.requestPermissions(UpdatesActivity.this,
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_WRITE_STORAGE);
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            UpdatesActivity.this.finish();
                            alertDialog.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.
                            VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                            "saes2go_update_"+update+".apk");
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("");
                    Toast.makeText(getApplicationContext(), "Descargando actualización...",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "Carga de actualizaciones finalizada. #");
                super.onPageFinished(view, url);
            }

            @SuppressWarnings("deprecation")
            public void onReceivedError(WebView view, int errorCod ,String description,
                                        String failingUrl){
                Log.e(TAG, "Error."+description+" #");
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdatesActivity.this);
                builder.setTitle("¡Atención!");
                builder.setMessage("No pudimos contactar con el servidor." +
                        " Intenta de nuevo más tarde." +
                        "\n\n"+String.valueOf(description).substring(5));
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                if(activityActive){ dialog.show();}
            }

            @SuppressWarnings("deprecation")
            public boolean shouldOverrideUrlLoading(WebView view, String url) { return false; }
        });
         */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_WRITE_STORAGE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    hasPermission = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.
                            WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    Toast.makeText(this, "¡Gracias! Intenta descargar nuevamente",
                            Toast.LENGTH_SHORT).show();
                }else{
                    hasPermission = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.
                            WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    Toast.makeText(this, "No nos otorgaste permisos :(",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            UpdatesActivity.this.finish();
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

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Document doc = Jsoup.connect(url_update).timeout(timeout).get();
                if(doc.select("span[id=version_number]").hasText()){
                    Element span = doc.select("span[id=version_number]").first();
                    version = span.text();
                }
                if(doc.select("span[id=version_code]").hasText()){
                    Element span = doc.select("span[id=version_code]").first();
                    update = span.text();
                }
                if(doc.select("span[id=version_date]").hasText()){
                    Element span = doc.select("span[id=version_date]").first();
                    date = span.text();
                }
                if(doc.select("table[id=change_log]").hasText()){
                    Element table = doc.select("table[id=change_log]").first();
                    Element tbody = table.select("tbody").first();
                    Iterator<Element> td = tbody.select("td").iterator();
                    changes = "";
                    while(td.hasNext()) {
                        changes += td.next().text()+"\n";
                    }
                }
            }catch(Exception e){e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            versionToInt = Integer.valueOf(version);
            new_version.setText(update);
            last_version_date.setText(date);
            change_log.setText(changes);
            checar_build();
            if (UpdatesActivity.this.progressDialog.isShowing()) {
                UpdatesActivity.this.progressDialog.dismiss();
            }
        }
    }

    public void checar_build(){
        if(versionToInt <= BuildConfig.VERSION_CODE){
            message.setText(getResources().getString(R.string.update_5));
            //relative_webView.setVisibility(View.GONE);
            //new_version.setVisibility(View.GONE);
            //webview.setVisibility(View.GONE);
            //view1.setVisibility(View.GONE);
        }else{
            message.setText(getResources().getString(R.string.update_6));
        }
    }
}
