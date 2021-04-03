package com.example.bitkilerinisula;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("başlık", remoteMessage.getNotification().getTitle());
        Log.e("içerik", remoteMessage.getNotification().getBody());

    }
}
