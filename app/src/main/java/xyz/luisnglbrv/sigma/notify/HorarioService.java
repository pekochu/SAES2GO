package xyz.luisnglbrv.sigma.notify;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by LuisAngel on 04/02/17.
 */

public class HorarioService extends Service {

    MyHorarioReceiver alarm = new MyHorarioReceiver();

    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        alarm.setAlarm(this, 99, 99);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        alarm.setAlarm(this, 99, 99);
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
