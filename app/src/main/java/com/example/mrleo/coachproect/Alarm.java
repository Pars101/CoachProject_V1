package com.example.mrleo.coachproect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by mrleo on 8/8/2018.
 */

public class Alarm extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer player = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        Log.i("Alarm", "IT'S WORKING OH MY GOD YES");
        player.start();
    }
}
