package com.example.esp_health_monitor;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



public class FCM_Message_service extends FirebaseMessagingService {
    public static final String TAG = "MyTag";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG,"on messege receive : Data");

        if (remoteMessage.getNotification() != null){
            String title= remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_person_black_24dp );

            Notification notification = new NotificationCompat.Builder(this,MainActivity.FCM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_person_black_24dp)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setLargeIcon(icon)
                    .build();
            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1002,notification);
        }
        if (remoteMessage.getData().size() >0){
            Log.d(TAG,"on messege receive : Data"+remoteMessage.getData().toString());
        }

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG,"new token : Data");
    }
}
