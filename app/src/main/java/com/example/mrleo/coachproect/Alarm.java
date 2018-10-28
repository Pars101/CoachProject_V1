package com.example.mrleo.coachproect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by mrleo on 8/8/2018.
 */

public class Alarm extends BroadcastReceiver{
    //private static MediaPlayer player = MediaPlayer.create(MainApplication.getAppContext(), Settings.System.DEFAULT_RINGTONE_URI);

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("Title");
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("Congrats!", "You finished " + title);
        notificationHelper.getManager().notify(1, nb.build());
        //player.start();
        //MediaPlayer.create(MainApplication.getAppContext(), Settings.System.DEFAULT_RINGTONE_URI).start();
    }

    //public static void stop(){
    //    player.stop();
    //}
}
