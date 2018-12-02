package com.mrleo.coachproect;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

import me.relex.circleindicator.CircleIndicator;

public class StudentInterface extends AppCompatActivity {
    private final int REQ_CODE = 915;
    private CardManager cardManager;
    UUID alarmCardId;

    private Button prevCardButton;
    private Button nextCardButton;
    private Button startButton;
    private Button cancelButton;
    private Button backButton;

    private TextView textViewTime;
    private TextView textViewTitle;
    private TextView textViewInstructions;

    ViewPager viewPager;
    CircleIndicator indicator;

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

        alarmCardId = AlarmCardManager.readAlarmCardId();
        cardManager = new CardManager((UUID) getIntent().getExtras().get("ENTRY"));

        viewPager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);

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
                alarmCardId = cardManager.getCurrentCard().getId();
                setAlarm(cardManager.getCurrentCard());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setEnabled(true);
                cancelButton.setEnabled(false);
                alarmCardId = null;
                cancelAlarm();
            }
        });
    }

    private void updateControls(){
        prevCardButton.setEnabled(cardManager.hasPrevCard());
        nextCardButton.setEnabled(cardManager.hasNextCard());
        textViewInstructions.setEnabled(cardManager.getCurrentCard() != null);
        textViewTitle.setEnabled(cardManager.getCurrentCard() != null);
        textViewTime.setEnabled(cardManager.getCurrentCard() != null);
        startButton.setEnabled(cardManager.getCurrentCard() != null && (cardManager.getCurrentCard().getHours() != 0 || cardManager.getCurrentCard().getMinutes() != 0));
    }

    private void updateAlarmButton(Card card){
        if(card.getId().equals(alarmCardId)){
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
            textViewInstructions.setText("");
        }
        else {
            initImageSlider(card.getImageList());
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
    }

    private void initImageSlider(ArrayList<Uri> images) {
        viewPager.setAdapter(new ImageSliderAdapter(this, images));
        indicator.setViewPager(viewPager);
    }
}
