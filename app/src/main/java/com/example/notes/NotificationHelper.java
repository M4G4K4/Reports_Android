package com.example.notes;

import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "reportsID";


    public static void displayNotification(Context context,String title, String text){

        NotificationCompat.Builder mBuilder = new NotificationCompat
                .Builder(context,CHANNEL_ID )
                .setSmallIcon(R.drawable.point)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,mBuilder.build());

    }



}
