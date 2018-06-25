package com.dihanov.musiq.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.dihanov.musiq.R;
import com.dihanov.musiq.ui.main.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by dimitar.dihanov on 2/9/2018.
 */

public class Notificator {
    private static final int NOTIFICATION_ID = 9237;
    private static final String NOTIFICATION_CHANNEL_ID = "musiq_notif_channel";
    private static final String NOTIFICATION_CHANNEL_NAME = "musiq_notif_channel";

    public static void buildNotification(Context context, String title, String content) {
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        //cancel the previous notification
        nm.cancel(NOTIFICATION_ID);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        builder.setContentIntent(contentIntent);
        builder.setContentText(content);
        builder.setContentTitle(title);
        builder.setOngoing(false);
        builder.setSmallIcon(R.drawable.ic_notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_launcher_icon_large));
//        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setVibrate(new long[]{});
        builder.setOnlyAlertOnce(false);

        Notification notification = builder.build();

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_LOW;

                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
                notificationChannel.enableVibration(false);
                nm.createNotificationChannel(notificationChannel);

                nm.notify(NOTIFICATION_CHANNEL_NAME, NOTIFICATION_ID, notification);
            } else {
                nm.notify(NOTIFICATION_ID, notification);
            }
        } catch (RuntimeException e) {
            AppLog.log(Notificator.class.getSimpleName(), e.getMessage());
        }

    }

    public static void cancelNotification(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
    }


}
