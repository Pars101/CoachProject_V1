package com.example.mrleo.coachproect;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentInterface extends AppCompatActivity {
    private static ImageView imageView;
    private static MediaPlayer mp;
    private static TextView textView;
    private static int currentIndex = 0;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_interface);

        Button leftButton = findViewById(R.id.buttonLeft);
        Button rightButton = findViewById(R.id.buttonRight);
        Button nextButton = findViewById(R.id.nextButton);
        Button cancelButton = findViewById(R.id.buttonCancel);
        imageView = (ImageView)findViewById(R.id.imageInstructions);
        textView = (TextView)findViewById(R.id.textInstructions);
        mp = MediaPlayer.create(this, R.raw.ding);

        if(EnterProgram.getCard(currentIndex).getFirstImage() != null) {
            EnterProgram.getCard(currentIndex).setImageIndex(0);
            imageView.setImageURI(EnterProgram.getCard(currentIndex).getFirstImage());
        }
        else {
            imageView.setImageResource(R.drawable.placeholder);
        }
        textView.setText(EnterProgram.getCard(currentIndex).getMessage());

        if(EnterProgram.getCard(currentIndex).getSeconds() != 0) {
            startTimer();
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
                    countDownTimer.cancel();
                    currentIndex++;
                    if (EnterProgram.getCard(currentIndex).getFirstImage() != null) {
                        imageView.setImageURI(EnterProgram.getCard(currentIndex).getFirstImage());
                    } else {
                        imageView.setImageResource(R.drawable.placeholder);
                    }
                    textView.setText(EnterProgram.getCard(currentIndex).getMessage());
                    if (EnterProgram.getCard(currentIndex).getSeconds() != 0) {
                        startTimer();
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Maybedue to change. Currently, it resets the order every time you exit.
                currentIndex = 0;
                finish();
            }
        });
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(EnterProgram.getCard(currentIndex).getSeconds() * 1000, 1000) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                mp.start();
            }
        }.start();
    }
}
