package com.mrleo.coachproect;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Card implements Serializable{
    private UUID id;
    private String title;
    private String message;
    private Integer hours;
    private Integer minutes;
    private ArrayList<Uri> imageList;

    private int currentImageIndex;

    public Card(){
        id = UUID.randomUUID();
        title = "";
        message = "";
        hours = 0;
        minutes = 0;
        imageList = new ArrayList<>();
        currentImageIndex = -1;
    }

    public Card(CardMetadata cardMetadata) {
        id = cardMetadata.getId();
        title = cardMetadata.getTitle();
        message = cardMetadata.getMessage();
        hours = cardMetadata.getHours();
        minutes = cardMetadata.getMinutes();
        imageList = new ArrayList<>();
        ArrayList<String> imageUriStringList = cardMetadata.getImageList();
        if(imageUriStringList != null){
            for (String str: imageUriStringList) {
                imageList.add(Uri.parse(str));
            }
        }
        currentImageIndex = imageList.isEmpty() ? -1 : 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public ArrayList<Uri> getImageList() {
        return imageList;
    }

    public Uri getCurrentImage(){
        return isValidIndex(currentImageIndex) ? imageList.get(currentImageIndex) : null;
    }

    public Uri getNextImage(){
        if(hasNextImage()){
            currentImageIndex++;
            return imageList.get(currentImageIndex);
        }

        return null;
    }

    public boolean hasNextImage(){
        return isValidIndex(currentImageIndex + 1);
    }

    public Uri getPrevImage(){
        if(hasPrevImage()){
            currentImageIndex--;
            return imageList.get(currentImageIndex);
        }

        return null;
    }

    public boolean hasPrevImage(){
        return isValidIndex(currentImageIndex - 1);
    }

    public void addImage(Uri image){
        currentImageIndex++;
        imageList.add(currentImageIndex, image);
    }

    public void removeImage(){
        if(isValidIndex(currentImageIndex)){
            int index = currentImageIndex;
            if(currentImageIndex == imageList.size() - 1){
                currentImageIndex--;
            }

            imageList.remove(index);
        }
    }

    private boolean isValidIndex(int index){
        return index >= 0 && index < imageList.size();
    }
}
