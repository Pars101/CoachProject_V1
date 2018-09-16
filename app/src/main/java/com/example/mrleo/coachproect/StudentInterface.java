package com.example.mrleo.coachproect;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class StudentInterface extends AppCompatActivity {
    private static ImageView imageView;
    private static MediaPlayer mp;
    private static TextView textView;
    private static int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_interface);

        EnterProgram.getCard(0).setIsInStudentInterface(true);

        Button leftButton = findViewById(R.id.buttonLeft);
        Button rightButton = findViewById(R.id.buttonRight);
        Button nextButton = findViewById(R.id.nextButton);
        final Button cancelButton = findViewById(R.id.buttonCancel);
        imageView = (ImageView)findViewById(R.id.imageInstructions);
        textView = (TextView)findViewById(R.id.textInstructions);
        mp = MediaPlayer.create(this, R.raw.ding);

        currentIndex = EnterProgram.getCard(0).getCurrentIndex();

        saveCardSet();

        for(int i = 0; i < EnterProgram.getCardSetLength(); i++){
            EnterProgram.getCard(i).setImageIndex(0);
        }

        if(EnterProgram.getCard(currentIndex).getFirstImage() != null) {
            setImage(EnterProgram.getCard(currentIndex).getFirstImage());
        }
        else {
            imageView.setImageResource(R.drawable.placeholder);
        }

        textView.setText(EnterProgram.getCard(currentIndex).getMessage());

        Log.i("HasSeconds", (EnterProgram.getCard(currentIndex).getSeconds() != 0) + "");
        Log.i("PendingIntent", "" + (EnterProgram.getCard(currentIndex).getAlarmHasAlreadyPlayed() == false));

        if(EnterProgram.getCard(currentIndex).getSeconds() != 0 && EnterProgram.getCard(currentIndex).getAlarmHasAlreadyPlayed() == false) {
            setAlarm(EnterProgram.getCard(currentIndex).getSeconds() * 1000);
        }

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EnterProgram.getCard(currentIndex).getPrevImage() == null) {
                    imageView.setImageResource(R.drawable.placeholder);
                }
                else{
                    setImage(EnterProgram.getCard(currentIndex).getPrevImage());
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
                    setImage(EnterProgram.getCard(currentIndex).getNextImage());
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentIndex < EnterProgram.getCardSetLength() - 1) {
                    currentIndex++;
                    if (EnterProgram.getCard(currentIndex).getFirstImage() != null) {
                        setImage(EnterProgram.getCard(currentIndex).getFirstImage());
                    } else {
                        imageView.setImageResource(R.drawable.placeholder);
                    }
                    textView.setText(EnterProgram.getCard(currentIndex).getMessage());
                    if (EnterProgram.getCard(currentIndex).getSeconds() != 0) {
                        cancelAlarm();
                        setAlarm(EnterProgram.getCard(currentIndex).getSeconds() * 1000);
                    }
                }
                saveCardSet();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
                EnterProgram.getCard(0).setIsInStudentInterface(false);
                EnterProgram.getCard(0).setCurrentIndex(0);
                for(int i = 0; i < currentIndex + 1; i++){
                    EnterProgram.getCard(i).setAlarmHasAlreadyPlayed(false);
                }
                currentIndex = 0;
                saveCardSet();
                finish();
            }
        });
    }

    private void setAlarm(int milliseconds){
        EnterProgram.getCard(currentIndex).setAlarmHasAlreadyPlayed(true);
        saveCardSet();
        Log.i("Set", "Setting");
        Log.i("Milliseconds", milliseconds + "");
        EnterProgram.setCurrentIndex(StudentInterface.currentIndex);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + milliseconds, pendingIntent);
    }

    private void cancelAlarm(){
        Log.i("Canceled", "Canceling");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    public static void setCurrentIndex(int currentIndex){
        StudentInterface.currentIndex = currentIndex;
    }

    public static int getCurrentIndex(){
        return currentIndex;
    }

    private void saveCardSet(){
        EnterProgram.getCard(0).setCurrentIndex(currentIndex);
        EnterProgram.setCardSet(getApplicationContext());
    }

    private void setImage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
