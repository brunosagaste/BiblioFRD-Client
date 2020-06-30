package com.bruno.frd.biblio.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private LocalBroadcastManager broadcaster = null;
    public static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: $token");
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        handleMessage(remoteMessage);
    }

    private void handleMessage(final RemoteMessage remoteMessage) {
        //1
        Handler handler = new Handler(Looper.getMainLooper());

        //2
        handler.post(new Runnable() {
            @Override
            public void run () {
                //  Le mandamos la info de la notificación al ActivityMain a través de un broadcast para que lo muestre en un snackbar
                if (remoteMessage.getNotification() != null) {
                    Intent intent = new Intent("MyData");
                    intent.putExtra("message", remoteMessage.getNotification().getTitle());
                    broadcaster.sendBroadcast(intent);
                } else {
                    Log.d(TAG, "Null notification");
                }
            }
        });
    }

}