package com.example.mrleo.coachproect;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class StudentInterface extends AppCompatActivity {
    private static ImageView imageView;
    //private static MediaPlayer mp;
    private static TextView textView;
    private static int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_interface);

        //EnterProgram.getCard(0).setIsInStudentInterface(true);

        Button leftButton = findViewById(R.id.buttonLeft);
        Button rightButton = findViewById(R.id.buttonRight);
        Button nextButton = findViewById(R.id.nextButton);
        Button cancelButton = findViewById(R.id.buttonCancel);
        imageView = (ImageView)findViewById(R.id.imageInstructions);
        textView = (TextView)findViewById(R.id.textInstructions);
        //mp = MediaPlayer.create(this, R.raw.ding);

        //currentIndex = EnterProgram.getCard(0).getCurrentIndex();
        currentIndex = CardManager.getInstance().getCurrentIndex();

        //saveCardSet();

        //for(int i = 0; i < CardManager.getInstance().getCardSetLength(); i++){
        //    CardManager.getInstance().getCard(i).setCurrentImageIndex(0);
        //}

        final Card currentCard = CardManager.getInstance().getCard(currentIndex);
        Uri currentImageUri = currentCard.getCurrentImage();
        if(currentImageUri == null){
            imageView.setImageResource(R.drawable.placeholder);
        }
        else{
            ImageUtil.setImage(imageView, currentImageUri);
        }

        textView.setText(CardManager.getInstance().getCard(currentIndex).getMessage());

        //Log.i("HasSeconds", (CardManager.getInstance().getCard(currentIndex).getSeconds() != 0) + "");
        //Log.i("PendingIntent", "" + (CardManager.getInstance().getCard(currentIndex).getAlarmHasAlreadyPlayed() == false));

        if(CardManager.getInstance().getCard(currentIndex).getSeconds() != 0 && CardManager.getInstance().getCard(currentIndex).getAlarmHasAlreadyPlayed() == false) {
            setAlarm(CardManager.getInstance().getCard(currentIndex).getSeconds() * 1000);
        }

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtil.setImage(imageView, currentCard.getPrevImage());
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtil.setImage(imageView, currentCard.getNextImage());
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentIndex < CardManager.getInstance().getCardSetLength() - 1) {
                    currentIndex++;
                    if (CardManager.getInstance().getCard(currentIndex).getFirstImage() != null) {
                        ImageUtil.setImage(imageView, CardManager.getInstance().getCard(currentIndex).getFirstImage());
                    } else {
                        imageView.setImageResource(R.drawable.placeholder);
                    }
                    textView.setText(CardManager.getInstance().getCard(currentIndex).getMessage());
                    if (CardManager.getInstance().getCard(currentIndex).getSeconds() != 0) {
                        cancelAlarm();
                        setAlarm(CardManager.getInstance().getCard(currentIndex).getSeconds() * 1000);
                    }
                }
                //saveCardSet();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
                CardManager.getInstance().getCard(0).setIsInStudentInterface(false);
                CardManager.getInstance().getCard(0).setCurrentIndex(0);
                for(int i = 0; i < currentIndex + 1; i++){
                    CardManager.getInstance().getCard(i).setAlarmHasAlreadyPlayed(false);
                }
                currentIndex = 0;
                //saveCardSet();
                finish();
            }
        });
    }

    private void setAlarm(int milliseconds){
        CardManager.getInstance().getCard(currentIndex).setAlarmHasAlreadyPlayed(true);
        //saveCardSet();
        Log.i("Set", "Setting");
        Log.i("Milliseconds", milliseconds + "");
        CardManager.getInstance().setCurrentIndex(StudentInterface.currentIndex);
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

    /*
    private void saveCardSet(){
        CardManager.getInstance().getCard(0).setCurrentIndex(currentIndex);
        CardManager.getInstance().saveCardSet(getApplicationContext());
    }
    */
}
