package com.bignerdranch.android.mockwalker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MockWalkerService extends Service {
    private static final String TAG = "MockWalkerService";

    private static final int NOTIFICATION_ID = 1;
    private static final int PENDING_SHUTDOWN_ID = 1;
    public static final String CHANNEL_ID = "MockWalkerService";
    public static final String CHANNEL_NAME = "MockWalkerService Notification";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent shutdownIntent = new Intent(this, ShutdownReceiver.class);
        PendingIntent shutdownPI = PendingIntent.getBroadcast(
                this, PENDING_SHUTDOWN_ID, shutdownIntent, 0
        );

//        Notification notification;
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
//            Log.i(TAG, "notification: mChannel:" + channel.toString());
//            notificationManager.createNotificationChannel(channel);
//        }
//        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(android.R.drawable.ic_dialog_map)
//                .setContentTitle(getString(R.string.notification_title))
//                .setContentText(getString(R.string.notification_text))
//                .setTicker(getString(R.string.app_name))
//                .setContentIntent(shutdownPI)
//                .build();
//
//        notificationManager.notify(0, notification);

        Notification notification;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            Log.i(TAG, "notification: DEFAULT_CHANNEL_ID=" + NotificationChannel.DEFAULT_CHANNEL_ID + ", mChannel:" + mChannel.toString());
            notificationManager.createNotificationChannel(mChannel);
        }

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_map)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setTicker(getString(R.string.app_name))
                .setContentIntent(shutdownPI)
                .build();

        notificationManager.notify(0, notification);

        startForeground(NOTIFICATION_ID, notification);
        MockWalker.get(this).setStarted(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopForeground(true);
        MockWalker.get(this).setStarted(false);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("" + getClass().getName() + " is not a bindable service");
    }


}
