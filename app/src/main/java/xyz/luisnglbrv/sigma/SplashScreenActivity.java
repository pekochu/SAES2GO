package xyz.luisnglbrv.sigma;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import xyz.luisnglbrv.sigma.notify.HorarioService;

/**
 * Created by LuisAngel on 20/12/16.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private Intent main;
    private Preferencias data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new Preferencias(this);
        setContentView(R.layout.activity_splash_screen);
        ImageView appIcon = (ImageView) findViewById(R.id.appIconView);
        Intent startServiceIntent = new Intent(SplashScreenActivity.this, HorarioService.class);
        startService(startServiceIntent);
        appIcon.setColorFilter(data.getColorSelected(), PorterDuff.Mode.SRC_IN);
        appIcon.setImageAlpha(appIcon.getImageAlpha() - 10);
        main = new Intent(SplashScreenActivity.this, SetearDatos.class);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(main));
                finish();
            }
        }, 1000);
    }
}
