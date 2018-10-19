package com.example.mrleo.coachproect;

import android.net.Uri;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Card implements Serializable{
    private String message = new String();
    private ArrayList<Uri> imageList = new ArrayList<>();
    private Integer seconds = new Integer(0);
    private int imageIndex = -1;

    private boolean isInStudentInterface = false;
    private int currentIndex = 0;
    private boolean alarmHasAlreadyPlayed = false;

    public Card(String message, Integer seconds){
        this.message = message;
        this.seconds = seconds;
    }

    public Card(String message, ArrayList<Uri> imageList, Integer seconds) {
        this.message = message;
        this.imageList = imageList;
        this.seconds = seconds;
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

    public Integer getSeconds(){
        return seconds;
    }

    public Uri getNextImage(){
        if(!imageList.isEmpty()) {
            if (imageIndex < imageList.size() - 1) {
                ++imageIndex;
            }
            return imageList.get(imageIndex);
        }
        else{
            return null;
        }
    }

    public Uri getPrevImage(){
        if(!imageList.isEmpty()) {
            if (imageIndex > 0) {
                --imageIndex;
            }
            return imageList.get(imageIndex);
        }
        else{
            return null;
        }
    }

    public Uri getFirstImage(){
        if(!imageList.isEmpty()) {
            return imageList.get(0);
        }
        else {
            return null;
        }
    }

    public Uri getCurrentImage(){
        if(!imageList.isEmpty()) {
            return imageList.get(imageIndex);
        }
        else {
            return null;
        }
    }

    public Integer getImageListLength(){
        return imageList.size();
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setSeconds(Integer seconds){
        this.seconds = seconds;
    }

    public void addImage(Uri image){
        imageIndex++;
        imageList.add(imageIndex, image);
    }

    public Uri removeImage(){
        if(!imageList.isEmpty()) {
            imageList.remove(imageIndex);
            imageIndex--;
            if(!imageList.isEmpty()) {
                imageIndex++;
                return imageList.get(imageIndex);
            }
        }
        return null;
    }

    public void setImageIndex(int index){
        this.imageIndex = index;
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

    public int getCurrentIndex(){
        return currentIndex;
    }

    public void setAlarmHasAlreadyPlayed(boolean alarmHasAlreadyPlayed){
        this.alarmHasAlreadyPlayed = alarmHasAlreadyPlayed;
    }

    public boolean getAlarmHasAlreadyPlayed(){
        return alarmHasAlreadyPlayed;
    }
}
