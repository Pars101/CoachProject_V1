package com.example.mrleo.coachproect;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CoachInterface extends AppCompatActivity {

    private static int currentIndex = 0;
    private int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_interface);

        CardManager.getInstance().getCard(0).setIsInStudentInterface(false);

        Button editPictures = findViewById(R.id.editPictures);
        Button nextButton = findViewById(R.id.nextButton);
        Button backButton = findViewById(R.id.backButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button saveButton = findViewById(R.id.saveButton);

        final EditText timeOne = findViewById(R.id.timeOne);
        final EditText timeTwo = findViewById(R.id.timeTwo);
        final EditText currentMessage = findViewById(R.id.editText);

        currentMessage.setText(CardManager.getInstance().getCard(currentIndex).getMessage());
        timeOne.setText(String.valueOf(CardManager.getInstance().getCard(currentIndex).getSeconds()/60));
        timeTwo.setText(String.valueOf(CardManager.getInstance().getCard(currentIndex).getSeconds()%60));

        editPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CoachInterface.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    createPictureEdit();
                }
                else {
                    //request permission
                    requestStoragePermission();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardManager.getInstance().getCard(currentIndex).setMessage(currentMessage.getText().toString());
                CardManager.getInstance().getCard(currentIndex).setSeconds(Integer.parseInt(timeOne.getText().toString())*60 + Integer.parseInt(timeTwo.getText().toString()));
                if(currentIndex < CardManager.getInstance().getCardSetLength()){
                    currentIndex++;
                }
                if(currentIndex == CardManager.getInstance().getCardSetLength()){
                    CardManager.getInstance().addCard(new Card("", 0));
                }
                currentMessage.setText(CardManager.getInstance().getCard(currentIndex).getMessage());
                timeOne.setText(String.valueOf(CardManager.getInstance().getCard(currentIndex).getSeconds()/60));
                timeTwo.setText(String.valueOf(CardManager.getInstance().getCard(currentIndex).getSeconds()%60));

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardManager.getInstance().getCard(currentIndex).setMessage(currentMessage.getText().toString());
                CardManager.getInstance().getCard(currentIndex).setSeconds(Integer.parseInt(timeOne.getText().toString())*60 + Integer.parseInt(timeTwo.getText().toString()));
                if(currentIndex > 0){
                    currentIndex--;
                }
                currentMessage.setText(CardManager.getInstance().getCard(currentIndex).getMessage());
                timeOne.setText(String.valueOf(CardManager.getInstance().getCard(currentIndex).getSeconds()/60));
                timeTwo.setText(String.valueOf(CardManager.getInstance().getCard(currentIndex).getSeconds()%60));
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardManager.getInstance().getCard(currentIndex).setMessage(currentMessage.getText().toString());
                CardManager.getInstance().getCard(currentIndex).setSeconds(Integer.parseInt(timeOne.getText().toString())*60 + Integer.parseInt(timeTwo.getText().toString()));
                currentIndex = 0;
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardManager.getInstance().getCard(currentIndex).setMessage(currentMessage.getText().toString());
                CardManager.getInstance().getCard(currentIndex).setSeconds(Integer.parseInt(timeOne.getText().toString())*60 + Integer.parseInt(timeTwo.getText().toString()));

                CardManager.getInstance().saveCardSet();
            }
        });
    }

    private void createPictureEdit() {
        Intent intent = new Intent(this, PictureEdit.class);
        startActivity(intent);
    }

    public static Card getCurrentCard() {
        return CardManager.getInstance().getCard(currentIndex);
    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this).setTitle("Permission Needed").setMessage("This app must access your photo gallery to load pictures").setPositiveButton("ok", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    ActivityCompat.requestPermissions(CoachInterface.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
                }
            }).setNegativeButton("cancel", new  DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.dismiss();
                }
            }).create().show();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == this.requestCode){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                createPictureEdit();
            }
        }
    }
}
