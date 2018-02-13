package com.dihanov.musiq.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.dihanov.musiq.R;
import com.dihanov.musiq.ui.main.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by dimitar.dihanov on 2/9/2018.
 */

public class Notificator {
    private static final int NOTIFICATION_ID = 9237;

    public static void buildNotification(Context context, String title, String content) {
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        //cancel the previous notification
        nm.cancel(NOTIFICATION_ID);

        Notification.Builder builder = new Notification.Builder(context);
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
        nm.notify(NOTIFICATION_ID, notification);
    }

    public static void cancelNotification(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
    }
}
