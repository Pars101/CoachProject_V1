package com.example.mrleo.coachproect;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentInterface extends AppCompatActivity {
    private static ImageView imageView;
    private static MediaPlayer mp;
    private static TextView textView;
    private static int currentIndex;
    private static boolean timerIsActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_interface);

        Button leftButton = findViewById(R.id.buttonLeft);
        Button rightButton = findViewById(R.id.buttonRight);
        Button nextButton = findViewById(R.id.nextButton);
        final Button cancelButton = findViewById(R.id.buttonCancel);
        imageView = (ImageView)findViewById(R.id.imageInstructions);
        textView = (TextView)findViewById(R.id.textInstructions);
        mp = MediaPlayer.create(this, R.raw.ding);
        currentIndex = 0;

        for(int i = 0; i < EnterProgram.getCardSetLength(); i++){
            EnterProgram.getCard(i).setImageIndex(0);
        }

        if(EnterProgram.getCard(currentIndex).getFirstImage() != null) {
            imageView.setImageURI(EnterProgram.getCard(currentIndex).getFirstImage());
        }
        else {
            imageView.setImageResource(R.drawable.placeholder);
        }

        textView.setText(EnterProgram.getCard(currentIndex).getMessage());

        if(EnterProgram.getCard(currentIndex).getSeconds() != 0) {
            setAlarm(EnterProgram.getCard(currentIndex).getSeconds() * 1000);
        }

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EnterProgram.getCard(currentIndex).getPrevImage() == null) {
                    imageView.setImageResource(R.drawable.placeholder);
                }
                else{
                    imageView.setImageURI(EnterProgram.getCard(currentIndex).getPrevImage());
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EnterProgram.getCard(currentIndex).getNextImage() == null) {
                    imageView.setImageResource(R.drawable.placeholder);
                }
                else{
                    imageView.setImageURI(EnterProgram.getCard(currentIndex).getNextImage());
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentIndex < EnterProgram.getCardSetLength() - 1) {
                    currentIndex++;
                    if (EnterProgram.getCard(currentIndex).getFirstImage() != null) {
                        imageView.setImageURI(EnterProgram.getCard(currentIndex).getFirstImage());
                    } else {
                        imageView.setImageResource(R.drawable.placeholder);
                    }
                    textView.setText(EnterProgram.getCard(currentIndex).getMessage());
                    if (EnterProgram.getCard(currentIndex).getSeconds() != 0) {
                        cancelAlarm();
                        setAlarm(EnterProgram.getCard(currentIndex).getSeconds() * 1000);
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Maybe; due to change. Currently, it resets the order every time you exit.
                cancelAlarm();
                currentIndex = 0;
                finish();
            }
        });
    }

    private void setAlarm(int milliseconds){
        Log.i("Set", "Setting");
        Log.i("Milliseconds", milliseconds + "");
        EnterProgram.setCurrentIndex(StudentInterface.currentIndex);
        StudentInterface.timerIsActive = true;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + milliseconds, pendingIntent);
    }

    private void cancelAlarm(){
        Log.i("Canceled", "Canceling");
        StudentInterface.timerIsActive = false;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    public static void setCurrentIndex(int currentIndex){
        StudentInterface.currentIndex = currentIndex;
    }

    public static boolean getTimerIsActive(){
        return timerIsActive;
    }
}
