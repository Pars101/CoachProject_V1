package com.example.mrleo.coachproect;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Card implements Serializable{
    private String message = new String();
    private ArrayList<Uri> imageList;
    private Integer seconds = new Integer(0);
    private int currentImageIndex;

    private boolean isInStudentInterface = false;
    private int currentIndex = 0;
    private boolean alarmHasAlreadyPlayed = false;

    public Card(String message, Integer seconds){
        this.message = message;
        this.seconds = seconds;
        imageList = new ArrayList<>();
        currentImageIndex = -1;
    }

    public Card(String message, ArrayList<Uri> imageList, Integer seconds) {
        this.message = message;
        this.seconds = seconds;
        this.imageList = imageList == null ? new ArrayList<Uri>() : imageList;
        currentImageIndex = imageList.size() - 1;
    }

    public ArrayList<Uri> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<Uri> imageList) {
        this.imageList = imageList;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public Integer getSeconds(){
        return seconds;
    }

    public void setSeconds(Integer seconds){
        this.seconds = seconds;
    }

    public Uri getCurrentImage(){
        return isValidIndex(currentImageIndex) ? imageList.get(currentImageIndex) : null;
    }

    public Uri getNextImage(){
        if(isValidIndex(currentImageIndex + 1)){
            currentImageIndex++;
            return imageList.get(currentImageIndex);
        }

        return null;
    }

    public Uri getPrevImage(){
        if(isValidIndex(currentImageIndex - 1)){
            currentImageIndex--;
            return imageList.get(currentImageIndex);
        }

        return null;
    }

    public Uri getFirstImage(){
        if(!imageList.isEmpty()) {
            return imageList.get(0);
        }
        else {
            return null;
        }
    }

    public void addImage(Uri image){
        currentImageIndex++;
        imageList.add(currentImageIndex, image);
    }

    public Uri removeImage(){
        if(isValidIndex(currentImageIndex)){
            int index = currentImageIndex;
            if(currentImageIndex == imageList.size() - 1){
                currentImageIndex--;
            }

            imageList.remove(index);
        }

        return getCurrentImage();
    }

    public void setCurrentImageIndex(int index){
        this.currentImageIndex = index;
    }

    public void setIsInStudentInterface(boolean newIsInStudentInterface){
        isInStudentInterface = newIsInStudentInterface;
    }

    public boolean getIsInStudentInterface(){
        return isInStudentInterface;
    }

    public void setCurrentIndex(int newCurrentIndex){
        currentIndex = newCurrentIndex;
    }

    public void setAlarmHasAlreadyPlayed(boolean alarmHasAlreadyPlayed){
        this.alarmHasAlreadyPlayed = alarmHasAlreadyPlayed;
    }

    public boolean getAlarmHasAlreadyPlayed(){
        return alarmHasAlreadyPlayed;
    }

    private boolean isValidIndex(int index){
        return index >= 0 && index < imageList.size();
    }
}
