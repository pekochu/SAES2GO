package xyz.luisnglbrv.sigma;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "AboutActivity";
    private int versionCode;
    private String versionName;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Button donar;
    private ImageView logoDev, logoApp;
    private int easter_1, easter_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        easter_1 = 0;
        versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        logoDev = (ImageView) findViewById(R.id.logo_developer);
        logoApp = (ImageView) findViewById(R.id.logo_app);
        TextView mTextViewVersion = (TextView) findViewById(R.id.about_textView_13);
        TextView mTextViewAbout = (TextView) findViewById(R.id.about_textView_6);
        TextView mTextViewAbout1 = (TextView) findViewById(R.id.about_textView_8);
        TextView mTextViewAbout2 = (TextView) findViewById(R.id.about_textView_10);
        TextView mTextViewAbout3 = (TextView) findViewById(R.id.about_textView_12);
        donar = (Button) findViewById(R.id.donar_Button);
        mTextViewAbout.setMovementMethod(LinkMovementMethod.getInstance());
        mTextViewAbout1.setMovementMethod(LinkMovementMethod.getInstance());
        mTextViewAbout2.setMovementMethod(LinkMovementMethod.getInstance());
        mTextViewAbout3.setMovementMethod(LinkMovementMethod.getInstance());
        mTextViewVersion.setMovementMethod(LinkMovementMethod.getInstance());
        mTextViewVersion.setText("Versión: "+versionName+"\nBuild: "+versionCode);
        donar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=7JGKJM6Y8YRDJ"
                ));
                startActivity(browserIntent);
            }
        });
        logoApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(easter_1 != 5){
                    easter_1++;
                }else{
                    Toast.makeText(AboutActivity.this,
                            "¡Huélum! ¡Huélum! ¡Gloria!",
                            Toast.LENGTH_SHORT).show();
                    easter_1 = 0;
                }
            }
        });
        logoDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(easter_2 != 5){
                    easter_2++;
                }else{
                    Toast.makeText(AboutActivity.this,
                            "Hello, friend...",
                            Toast.LENGTH_SHORT).show();
                    easter_2++;
                }
                if(easter_2 == 26){
                    Toast.makeText(AboutActivity.this,
                            "261015",
                            Toast.LENGTH_SHORT).show();
                    easter_2 = 0;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about_action_settings_1) {
            sendEmail();
            return true;
        }else if(id == android.R.id.home){
            AboutActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void sendEmail() {
        Log.i(TAG, "Enviar sugerencia o recomendación. #");

        String[] TO = {"luisnglbrv@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("application/octet-stream");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback: SAES2GO v"+versionName+
                " - Build: "+versionCode);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Sugerencia, queja o recomendación:\n\n");

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar correo por medio de..."));
            Log.i(TAG, "Abriendo cliente de correo. #");
        } catch (Exception ex) {
            Log.e(TAG, "No se ha encontrado cliente de correo. "+ex.getMessage()+"#");
            ex.printStackTrace();
            Toast.makeText(AboutActivity.this,
                    "No tienes un cliente de correos instalado. Instala, por ej. Gmail.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(logoApp.getContext())
                .load(R.drawable.aboutapp)
                .into(logoApp);
        Glide.with(logoDev.getContext())
                .load(R.drawable.aboutdev)
                .into(logoDev);
        mFirebaseAnalytics.setCurrentScreen(this, getClass().getSimpleName(), null);
    }
}
