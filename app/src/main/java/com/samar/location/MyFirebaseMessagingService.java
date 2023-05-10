package com.samar.location;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.samar.location.BottomNavigationBar.ChatFragment;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService

{

    private static final int NOTIFICATION_ID = 1;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if the message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            // Handle data payload.
            handleDataMessage(remoteMessage.getData());
        }

        // Check if the message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            // Handle notification payload.
            handleNotification(remoteMessage.getNotification());
        }
    }

    private void handleDataMessage(Map<String, String> data) {
        // Extract data from the message payload.
        String title = data.get("title");
        String body = data.get("body");

        // Build and display the notification.
        showNotification(title, body);
    }

    private void handleNotification(RemoteMessage.Notification notification) {
        // Extract notification details.
        String title = notification.getTitle();
        String body = notification.getBody();

        // Build and display the notification.
        showNotification(title, body);
    }

    private void showNotification(String title, String body) {
        // Build the notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Set the notification's intent.
        Intent intent = new Intent(this, ChatFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Display the notification.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}





