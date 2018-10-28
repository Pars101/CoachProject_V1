package com.example.mrleo.coachproect;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class StudentInterface extends AppCompatActivity {
    private final int REQ_CODE = 915;
    private CardManager cardManager;
    UUID cardId;

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

        UUID tempId = null;
        try {
            tempId = (UUID) getIntent().getExtras().get("ENTRY");
        }
        catch (Exception e){

        }
        cardId = tempId != null ? tempId : AlarmCardManager.readAlarmCardId();
        cardManager = new CardManager(cardId);

        updateControls();

        Card currentCard = cardManager.getCurrentCard();
        if(currentCard != null){
            updateCardInfoFields(currentCard);
            updateAlarmButton(currentCard);
        }

        prevCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(cardManager.hasPrevCard()) {
                Card prevCard = cardManager.getPrevCard();
                updateCardInfoFields(prevCard);
                updateControls();
                updateAlarmButton(prevCard);
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
                updateAlarmButton(nextCard);
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
                startButton.setEnabled(false);
                cancelButton.setEnabled(true);
                cardId = cardManager.getCurrentCard().getId();
                setAlarm(cardManager.getCurrentCard());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setEnabled(true);
                cancelButton.setEnabled(false);
                cardId = null;
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

    private void updateAlarmButton(Card card){
        if(card.getId().equals(cardId)){
            startButton.setEnabled(false);
            cancelButton.setEnabled(true);
        }
        else{
            startButton.setEnabled(true);
            cancelButton.setEnabled(false);
        }
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

    private void setAlarm(Card card){
        long hours = card.getHours();
        long minutes = card.getMinutes();
        long milliseconds = (hours * 3600 + minutes * 60) * 1000;
        if(milliseconds != 0) {

            AlarmCardManager.saveAlarmCardId(card.getId());
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, Alarm.class);
            intent.putExtra("Title", card.getTitle());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQ_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + milliseconds, pendingIntent);
        }
    }

    private void cancelAlarm(){
        Log.i("Canceled", "Canceling");
        AlarmCardManager.saveAlarmCardId(null);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQ_CODE, intent, 0);
        alarmManager.cancel(pendingIntent);

        Alarm.stop();
    }
}
