package com.example.mrleo.coachproect;

import android.annotation.TargetApi;
import android.app.*;
import android.app.Notification;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.UUID;

/**
 * Created by mrleo on 8/13/2018.
 */

public class NotificationHelper extends ContextWrapper{

    public static final String channel1ID = "channel1ID";
    public static final String channel1Name = "Channel 1";
    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channel1ID, channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager(){
        if(manager == null){
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message){
        Intent studentIntent = new Intent(this, StudentInterface.class);
        studentIntent.putExtra("ENTRY", (UUID)null);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                studentIntent, 0);
        return new NotificationCompat.Builder(getApplicationContext(), channel1ID)
                .setContentTitle(title)
                .setContentText(message)
                //.setContentIntent(contentIntent)
                .setFullScreenIntent(contentIntent, true)
                .setSmallIcon(R.drawable.ic_one)
                .setPriority(Notification.PRIORITY_MAX);
    }
}
