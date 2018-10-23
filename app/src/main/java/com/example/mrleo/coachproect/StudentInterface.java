package com.example.mrleo.coachproect;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentInterface extends AppCompatActivity {
    private CardManager cardManager;

    private Button prevCardButton;
    private Button nextCardButton;
    private Button startButton;
    private Button cancelButton;
    private Button backButton;

    private TextView textViewTime;
    private TextView textViewTitle;
    private TextView textViewInstructions;

    private Button prevImageButton;
    private Button nextImageButton;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_interface);

        prevCardButton = findViewById(R.id.prevButton);
        nextCardButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.buttonBack);
        startButton = findViewById(R.id.buttonStart);
        cancelButton = findViewById(R.id.buttonCancel);

        textViewTime = findViewById(R.id.textTime);
        textViewTitle = findViewById(R.id.textTitle);
        textViewInstructions = findViewById(R.id.textInstructions);

        prevImageButton = findViewById(R.id.buttonLeft);
        nextImageButton = findViewById(R.id.buttonRight);
        imageView = findViewById(R.id.imageInstructions);

        cardManager = new CardManager();
        updateControls();

        Card currentCard = cardManager.getCurrentCard();
        if(currentCard != null){
            updateCardInfoFields(currentCard);
        }

        prevCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(cardManager.hasPrevCard()) {
                Card prevCard = cardManager.getPrevCard();
                updateCardInfoFields(prevCard);
                updateControls();
            }
            }
        });

        nextCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(cardManager.hasNextCard()) {
                Card nextCard = cardManager.getNextCard();
                updateCardInfoFields(nextCard);
                updateControls();
            }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
                long hours = cardManager.getCurrentCard().getHours();
                long minutes = cardManager.getCurrentCard().getMinutes();
                long seconds = hours * 3600 + minutes * 60;
                if(seconds != 0) {
                    setAlarm(seconds * 1000);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
            }
        });

        prevImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            ImageUtil.setImage(imageView, cardManager.getCurrentCard().getPrevImage());
            updateControls();
            }
        });

        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            ImageUtil.setImage(imageView, cardManager.getCurrentCard().getNextImage());
            updateControls();
            }
        });
    }

    private void updateControls(){
        prevCardButton.setEnabled(cardManager.hasPrevCard());
        nextCardButton.setEnabled(cardManager.hasNextCard());
        prevImageButton.setEnabled(cardManager.getCurrentCard() != null && cardManager.getCurrentCard().hasPrevImage());
        nextImageButton.setEnabled(cardManager.getCurrentCard() != null && cardManager.getCurrentCard().hasNextImage());
        textViewInstructions.setEnabled(cardManager.getCurrentCard() != null);
        textViewTitle.setEnabled(cardManager.getCurrentCard() != null);
        textViewTime.setEnabled(cardManager.getCurrentCard() != null);
        startButton.setEnabled(cardManager.getCurrentCard() != null && (cardManager.getCurrentCard().getHours() != 0 || cardManager.getCurrentCard().getMinutes() != 0));
    }

    private void updateCardInfoFields(Card card){
        if(card == null){
            imageView.setImageResource(R.drawable.placeholder);
            textViewInstructions.setText("");
        }
        else {
            Uri currentImageUri = card.getCurrentImage();
            if(currentImageUri == null){
                imageView.setImageResource(R.drawable.placeholder);
            }
            else{
                ImageUtil.setImage(imageView, currentImageUri);
            }

            textViewTime.setText(card.getHours() + "H:" + card.getMinutes() + "M");
            textViewTitle.setText(card.getTitle());
            textViewInstructions.setText(card.getMessage());
        }
    }

    private void setAlarm(long milliseconds){
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
}
