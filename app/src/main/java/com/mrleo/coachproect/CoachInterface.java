package com.mrleo.coachproect;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CoachInterface extends AppCompatActivity {
    private final int REQUEST_CODE = 1;
    private final int PICK_IMAGE = 100;

    private Button nextCardButton;
    private Button prevCardButton;
    private Button addCardButton;
    private Button removeCardButton;

    private EditText timeOne;
    private EditText timeTwo;
    private EditText title;
    private EditText description;

    private Button addImageButton;
    private Button deleteImageButton;
    private Button prevImageButton;
    private Button nextImageButton;
    private ImageView imageView;

    private CardManager cardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_interface);

        nextCardButton = findViewById(R.id.nextButton);
        prevCardButton = findViewById(R.id.prevButton);
        addCardButton = findViewById(R.id.addCardButton);
        removeCardButton = findViewById(R.id.removeCardButton);

        addImageButton = findViewById(R.id.addImageButton);
        deleteImageButton = findViewById(R.id.deleteImageButton);
        prevImageButton = findViewById(R.id.leftButton);
        nextImageButton = findViewById(R.id.rightButton);
        imageView = findViewById(R.id.imageView);

        Button cancelButton = findViewById(R.id.cancelButton);
        Button saveButton = findViewById(R.id.saveButton);

        timeOne = findViewById(R.id.timeOne);
        timeTwo = findViewById(R.id.timeTwo);
        title = findViewById(R.id.editTextTitle);
        description = findViewById(R.id.editTextDescription);

        cardManager = new CardManager();
        updateCardInfoFields(cardManager.getCurrentCard());

        updateControls();

        if (ContextCompat.checkSelfPermission(CoachInterface.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        }

        prevCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCurrentCardInfo();
                updateCardInfoFields(cardManager.getPrevCard());
                updateControls();
            }
        });

        nextCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCurrentCardInfo();
                updateCardInfoFields(cardManager.getNextCard());
                updateControls();
            }
        });

        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCurrentCardInfo();
                cardManager.addNewCard();
                updateCardInfoFields(cardManager.getCurrentCard());
                updateControls();
            }
        });

        removeCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardManager.removeCard();
                updateCardInfoFields(cardManager.getCurrentCard());
                updateControls();
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardManager.getCurrentCard().removeImage();
                updateControls();
                Uri temp = cardManager.getCurrentCard().getCurrentImage();
                if(temp != null) {
                    ImageUtil.setImage(imageView, temp);
                }
                else {
                    imageView.setImageResource(R.drawable.placeholder);
                }
            }
        });

        prevImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtil.setImage(imageView, cardManager.getCurrentCard().getPrevImage());
                prevImageButton.setEnabled(cardManager.getCurrentCard().hasPrevImage());
                nextImageButton.setEnabled(cardManager.getCurrentCard().hasNextImage());
            }
        });

        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtil.setImage(imageView, cardManager.getCurrentCard().getNextImage());
                prevImageButton.setEnabled(cardManager.getCurrentCard().hasPrevImage());
                nextImageButton.setEnabled(cardManager.getCurrentCard().hasNextImage());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCurrentCardInfo();
                cardManager.saveCardSet();
                finish();
            }
        });
    }

    private void updateControls(){
        prevCardButton.setEnabled(cardManager.hasPrevCard());
        nextCardButton.setEnabled(cardManager.hasNextCard());
        removeCardButton.setEnabled(cardManager.getCurrentCard() != null);
        addImageButton.setEnabled(cardManager.getCurrentCard() != null);
        deleteImageButton.setEnabled(cardManager.getCurrentCard() != null && cardManager.getCurrentCard().getCurrentImage() != null);
        prevImageButton.setEnabled(cardManager.getCurrentCard() != null && cardManager.getCurrentCard().hasPrevImage());
        nextImageButton.setEnabled(cardManager.getCurrentCard() != null && cardManager.getCurrentCard().hasNextImage());

        timeOne.setEnabled(cardManager.getCurrentCard() != null);
        timeTwo.setEnabled(cardManager.getCurrentCard() != null);
        title.setEnabled(cardManager.getCurrentCard() != null);
        description.setEnabled(cardManager.getCurrentCard() != null);
    }

    private void updateCardInfoFields(Card card){
        if(card == null){
            title.setText("");
            description.setText("");
            timeOne.setText("");
            timeTwo.setText("");
            imageView.setImageResource(R.drawable.placeholder);
        }
        else {
            title.setText(card.getTitle());
            description.setText(card.getMessage());
            timeOne.setText(String.valueOf(card.getHours()));
            timeTwo.setText(String.valueOf(card.getMinutes()));
            Uri currentImageUri = card.getCurrentImage();
            if (currentImageUri == null) {
                imageView.setImageResource(R.drawable.placeholder);
            } else {
                ImageUtil.setImage(imageView, currentImageUri);
            }
        }
    }

    private void saveCurrentCardInfo(){
        Card card = cardManager.getCurrentCard();
        if(card != null){
            card.setTitle(title.getText().toString());
            card.setMessage(description.getText().toString());
            card.setHours(Integer.parseInt(timeOne.getText().toString()));
            card.setMinutes(Integer.parseInt(timeTwo.getText().toString()));
        }
    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this).setTitle("Permission Needed").setMessage("This app must access your photo gallery to load pictures").setPositiveButton("ok", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    ActivityCompat.requestPermissions(CoachInterface.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                }
            }).setNegativeButton("cancel", new  DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.dismiss();
                }
            }).create().show();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE){
            Uri imageUri = data.getData();
            ImageUtil.setImage(imageView, imageUri);
            cardManager.getCurrentCard().addImage(imageUri);
            updateControls();
        }
    }
}
