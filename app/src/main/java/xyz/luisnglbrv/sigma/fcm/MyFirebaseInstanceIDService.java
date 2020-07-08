package xyz.luisnglbrv.sigma.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by LuisAngel on 02/01/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MFIDSActivity";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token actualizado: " + refreshedToken+" #");
    }
}


