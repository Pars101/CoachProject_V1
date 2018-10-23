package com.example.mrleo.coachproect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageUtil {
    public static void setImage(ImageView imageView, Uri uri) {
        if(uri != null && imageView != null){
            try {
                InputStream inputStream = MainApplication.getAppContentResolver().openInputStream(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
