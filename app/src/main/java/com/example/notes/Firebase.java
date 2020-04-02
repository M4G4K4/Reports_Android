package com.example.notes;

import android.app.Notification;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Firebase extends FirebaseMessagingService {




    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification() != null){
            String title = remoteMessage.getNotification().getTitle();
            String text = remoteMessage.getNotification().getBody();

            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.displayNotification(getApplicationContext(),title,text);
        }
    }



}
