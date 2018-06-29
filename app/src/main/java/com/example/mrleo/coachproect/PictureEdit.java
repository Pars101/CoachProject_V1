package com.example.mrleo.coachproect;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class PictureEdit extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private static ImageView imageView;
    private Card currentCard;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_edit);

        currentCard = CoachInterface.getCurrentCard();

        Button addImage = findViewById(R.id.addButton);
        Button deleteImage = findViewById(R.id.deleteButton);
        Button leftButton = findViewById(R.id.leftButton);
        Button rightButton = findViewById(R.id.rightButton);
        Button backButton = findViewById(R.id.backButton);
        imageView = findViewById(R.id.imageView);

        if (currentCard.getFirstImage() == null) {
            imageView.setImageResource(R.drawable.placeholder);
            currentCard.setImageIndex(-1);
        } else {
            imageView.setImageURI(currentCard.getFirstImage());
        }

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentCard.getPrevImage() == null) {
                    imageView.setImageResource(R.drawable.placeholder);
                }
                else{
                    imageView.setImageURI(currentCard.getPrevImage());
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentCard.getNextImage() == null) {
                    imageView.setImageResource(R.drawable.placeholder);
                }
                else{
                    imageView.setImageURI(currentCard.getNextImage());
                }
            }
        });

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri temp = currentCard.removeImage();
                if(temp != null) {
                    imageView.setImageURI(temp);
                }
                else {
                    imageView.setImageResource(R.drawable.placeholder);
                }
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            currentCard.addImage(data.getData());
        }
    }

}
