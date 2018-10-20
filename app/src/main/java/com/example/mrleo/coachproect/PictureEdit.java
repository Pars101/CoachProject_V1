package com.example.mrleo.coachproect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
        Uri currentImageUri = currentCard.getCurrentImage();
        if(currentImageUri == null){
            imageView.setImageResource(R.drawable.placeholder);
        }
        else{
            ImageUtil.setImage(imageView, currentImageUri);
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

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri temp = currentCard.removeImage();
                if(temp != null) {
                    ImageUtil.setImage(imageView, temp);
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
            ImageUtil.setImage(imageView, imageUri);
            currentCard.addImage(data.getData());
        }
    }
}
