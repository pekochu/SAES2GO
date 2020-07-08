package xyz.luisnglbrv.sigma.notify;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import xyz.luisnglbrv.sigma.HorarioOffline;
import xyz.luisnglbrv.sigma.Preferencias;
import xyz.luisnglbrv.sigma.R;
import xyz.luisnglbrv.sigma.db.Horario_OpenHelper;

/**
 * Created by LuisAngel on 04/02/17.
 * BroadcastReceiver para el análisis del horario
 * y lanzar notificaciones cuando el tiempo del sistema
 * esté en un rango de 10 minutos antes del inicio de
 * alguna materia.
 */

public class MyHorarioReceiver extends BroadcastReceiver {

    String[] materia;
    String[] edificio;
    String[] salon;
    String[] lunes;
    String[] martes;
    String[] miercoles;
    String[] jueves;
    String[] viernes;
    String[] sabado;

    String [] txt0;
    String [] txt1;
    String [] txt2;
    String [] txt3;

    private Preferencias pref;
    private Context mContexto;
    private int selector;
    private Locale loc = new Locale("es", "MX");

    public void onReceive(Context context, Intent intent) {
        Calendar c = Calendar.getInstance();
        Cursor cursor = Horario_OpenHelper.getInstance(context).obtenerHorario();
        Boolean hacer;
        int today = dayOfWeek(getDate());
        int minutoTermino = c.get(Calendar.HOUR_OF_DAY), horaTermino = c.get(Calendar.MINUTE);
        int datos = cursor.getCount();
        int contador = 0, count = 0;

        materia = new String[datos];
        edificio = new String[datos];
        salon = new String[datos];
        lunes = new String[datos];
        martes = new String[datos];
        miercoles = new String[datos];
        jueves = new String[datos];
        viernes = new String[datos];
        sabado = new String[datos];

        if(cursor.moveToFirst()){
            for(int e = 0; e < datos; e++){
                materia[e] = cursor.getString(2);
                edificio[e] = cursor.getString(4);
                salon[e] = cursor.getString(5);
                lunes[e] = cursor.getString(6);
                martes[e] = cursor.getString(7);
                miercoles[e] = cursor.getString(8);
                jueves[e] = cursor.getString(9);
                viernes[e] = cursor.getString(10);
                sabado[e] = cursor.getString(11);
                cursor.moveToNext();
            }
        }

        hacer = false;

        if(today == 2){
            contador = 0;
            count = 0;
            for(int i = 0; i < lunes.length; i++){
                if(!lunes[i].equals("")){
                    hacer = true;
                    contador++;
                }
            }
            txt0 = new String[contador];
            txt1 = new String[contador];
            txt2 = new String[contador];
            txt3 = new String[contador];
            for(int i=0; i < lunes.length; i++){
                if(!lunes[i].equals("")){
                    txt0[count] = materia[i];
                    txt1[count] = edificio[i];
                    txt2[count] = salon[i];
                    txt3[count] = lunes[i];
                    count++;
                }
            }
        }else if(today == 3){
            contador = 0;
            count = 0;
            for(int i = 0; i < martes.length; i++){
                if(!martes[i].equals("")){
                    hacer = true;
                    contador++;
                }
            }
            txt0 = new String[contador];
            txt1 = new String[contador];
            txt2 = new String[contador];
            txt3 = new String[contador];
            for(int i=0; i < martes.length; i++){
                if(!martes[i].equals("")){
                    txt0[count] = materia[i];
                    txt1[count] = edificio[i];
                    txt2[count] = salon[i];
                    txt3[count] = martes[i];
                    count++;
                }
            }
        }else if(today == 4){
            contador = 0;
            count = 0;
            for(int i = 0; i < miercoles.length; i++){
                if(!miercoles[i].equals("")){
                    hacer = true;
                    contador++;
                }
            }
            txt0 = new String[contador];
            txt1 = new String[contador];
            txt2 = new String[contador];
            txt3 = new String[contador];
            for(int i=0; i < miercoles.length; i++){
                if(!miercoles[i].equals("")){
                    txt0[count] = materia[i];
                    txt1[count] = edificio[i];
                    txt2[count] = salon[i];
                    txt3[count] = miercoles[i];
                    count++;
                }
            }
        }else if(today == 5){
            contador = 0;
            count = 0;
            for(int i = 0; i < jueves.length; i++){
                if(!jueves[i].equals("")){
                    hacer = true;
                    contador++;
                }
            }
            txt0 = new String[contador];
            txt1 = new String[contador];
            txt2 = new String[contador];
            txt3 = new String[contador];
            for(int i=0; i < jueves.length; i++){
                if(!jueves[i].equals("")){
                    txt0[count] = materia[i];
                    txt1[count] = edificio[i];
                    txt2[count] = salon[i];
                    txt3[count] = jueves[i];
                    count++;
                }
            }
        }else if(today == 6){
            contador = 0;
            count = 0;
            for(int i = 0; i < viernes.length; i++){
                if(!viernes[i].equals("")){
                    hacer = true;
                    contador++;
                }
            }
            txt0 = new String[contador];
            txt1 = new String[contador];
            txt2 = new String[contador];
            txt3 = new String[contador];
            for(int i=0; i < viernes.length; i++){
                if(!viernes[i].equals("")){
                    txt0[count] = materia[i];
                    txt1[count] = edificio[i];
                    txt2[count] = salon[i];
                    txt3[count] = viernes[i];
                    count++;
                }
            }
        }else if(today == 7){
            contador = 0;
            count = 0;
            for(int i = 0; i < sabado.length; i++){
                if(!sabado[i].equals("")){
                    hacer = true;
                    contador++;
                }
            }
            txt0 = new String[contador];
            txt1 = new String[contador];
            txt2 = new String[contador];
            txt3 = new String[contador];
            for(int i=0; i < sabado.length; i++){
                if(!sabado[i].equals("")){
                    txt0[count] = materia[i];
                    txt1[count] = edificio[i];
                    txt2[count] = salon[i];
                    txt3[count] = sabado[i];
                    count++;
                }
            }
        }else{
            hacer = false;
        }

        if(hacer) {
            Locale loc = new Locale("es", "MX");
            String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY)) + ":" +
                    String.valueOf(c.get(Calendar.MINUTE));
            Date horaActual = new Date(), d1 = new Date(), materia = new Date();
            try {
                horaActual = new SimpleDateFormat("HH:mm", loc).parse(hour);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int horaAntes, minutoAntes, hora, minuto;
            for (int i = 0; i < txt3.length; i++) {
                try {
                    String horas[] = txt3[i].split("-");
                    horaAntes = Integer.valueOf(horas[0].trim().substring(0, 2));
                    minutoAntes = Integer.valueOf(horas[0].trim().substring(3, 5));
                    if (horas[0].trim().substring(3, 5).equals("00")) {
                        horaAntes -= 1;
                        minutoAntes = 47;
                    } else {
                        minutoAntes -= 13;
                    }
                    /*
                    horaTermino = Integer.valueOf(txt3[i].substring(8, 10));
                    minutoTermino = Integer.valueOf(txt3[i].substring(11, 13));
                    if (txt3[i].substring(11, 13).equals("00")) {
                        horaTermino -= 1;
                        minutoTermino = 51;
                    } else {
                        minutoTermino -= 9;
                    }
                    */
                    selector = i;
                    hora = Integer.valueOf(horas[0].trim().substring(0, 2));
                    minuto = Integer.valueOf(horas[0].trim().substring(3, 5));
                    String actually = hora + ":" + minuto;
                    String checar = horaAntes + ":" + minutoAntes;
                    d1 = new SimpleDateFormat("HH:mm", loc).parse(checar);
                    materia = new SimpleDateFormat("HH:mm", loc).parse(actually);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (horaActual.after(d1) && horaActual.before(materia)) {
                    sendNotification(context);
                    setAlarm(context, horaTermino, minutoTermino);
                }
            }
        }
    }

    public void setAlarm(Context context, int hora, int minuto){
        mContexto = context;
        Calendar cal = Calendar.getInstance();
        int hour, minute;
        if(hora == 99){ hour = cal.get(Calendar.HOUR_OF_DAY); }else{ hour = hora; }
        if(minuto == 99){ minute = cal.get(Calendar.MINUTE); }else{ minute = minuto; }
        //sendMessage(mContexto, "Servicio notify establecido a las "+hour+":"+minute+" horas.");
        pref = new Preferencias(mContexto);
        if(pref.getNotificationActivated()){
            Intent notificationIntent = new Intent(mContexto, MyHorarioReceiver.class);
            long recurring = (12*1000*60); //Checar cada 12 minutos (posiblemente óptmo?)
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContexto, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, 15);
            AlarmManager alarmManager = (AlarmManager) mContexto.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC, cal.getTimeInMillis(), recurring, pendingIntent);
        }
    }

    public void sendNotification(Context context){
        Intent intent = new Intent(context, HorarioOffline.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{500, 500, 500, 500, 1000})
                .setSmallIcon(R.drawable.ic_notif_saes2go_white)
                .setColor(Color.parseColor("#009688"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(txt0[selector].substring(0,1)+txt0[selector].substring(1).toLowerCase())
                .setContentText("Esta clase será en el edificio " +
                        txt1[selector].substring(10).toLowerCase() +
                        ", salón " + txt2[selector].substring(7).toLowerCase() +
                        " a las " + txt3[selector].substring(0, 5) + ".")
                .setTicker("Notificación de clase")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle(txt0[selector].substring(0,1)+txt0[selector].substring(1).toLowerCase())
                        .bigText("Esta clase será en el edificio " +
                                txt1[selector].substring(10).toLowerCase() +
                                ", salón " + txt2[selector].substring(7).toLowerCase() +
                                " a las " + txt3[selector].substring(0, 5) + ".")
                        .setSummaryText("Notificación de clase"));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(361024, notificationBuilder.build());
    }

    public void sendMessage(Context context, String message){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notif_saes2go_white)
                .setColor(Color.parseColor("#009688"))
                .setAutoCancel(true)
                .setContentTitle("SAES2GO")
                .setContentText(message)
                .setTicker("Receiver: setAlarm")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message)
                        .setBigContentTitle("SAES2GO")
                        .setSummaryText("Receiver: setAlarm"));
        NotificationManager notificationManager;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(3650, notificationBuilder.build());
    }

    public void sendToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public String getDate() {
        loc = new Locale("es", "MX");
        Date myDate = new Date();
        // new SimpleDateFormat("hh-mm-a").format(myDate);
        return new SimpleDateFormat("yyyy-MM-dd", loc).format(myDate);
    }

    public int dayOfWeek(String date) {
        try {
            int day = 0;
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy-MM-dd", loc);
            Date now = simpleDateformat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            day = calendar.get(Calendar.DAY_OF_WEEK);
            return day;
        } catch (Exception e) {
            return -1;
        }
    }
}
