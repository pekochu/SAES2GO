package xyz.luisnglbrv.sigma.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.FirebaseMessagingService;

import xyz.luisnglbrv.sigma.Preferencias;
import xyz.luisnglbrv.sigma.R;
import xyz.luisnglbrv.sigma.SplashScreenActivity;
import xyz.luisnglbrv.sigma.UpdatesActivity;
import xyz.luisnglbrv.sigma.escuelas.MedioSuperior;
import xyz.luisnglbrv.sigma.escuelas.Superior;

/**
 * Created by LuisAngel on 02/01/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG ="FCMActivity";
    private Preferencias datos;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "De: " + remoteMessage.getFrom());
        datos = new Preferencias(getApplicationContext());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Datos de la notificación: " + remoteMessage.getData()+ " #");
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().
                    getBody()+" #");
        }

        if(datos.getUpdates()) {
            sendNotification(remoteMessage.getNotification().getBody(),
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getData().get("ticker"),
                    remoteMessage.getData().get("abrir"));
        }
    }

    private void sendNotification(String messageBody, String title, String ticker, String abrir) {
        Intent intent;
        if(abrir.equals("updates")){
            intent = new Intent(this, UpdatesActivity.class);
        }else{
            intent = new Intent(this, SplashScreenActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSound(defaultSoundUri)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSmallIcon(R.drawable.ic_notif_saes2go_white)
                .setColor(Color.parseColor("#009688"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setTicker(ticker)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody)
                        .setBigContentTitle(title)
                        .setSummaryText(ticker));
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(361024, notificationBuilder.build());
    }
}
