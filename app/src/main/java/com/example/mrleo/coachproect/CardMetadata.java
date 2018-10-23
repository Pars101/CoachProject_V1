package com.example.mrleo.coachproect;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class CardMetadata implements Serializable {
    private UUID id;
    private String title;
    private String message;
    private Integer hours;
    private Integer minutes;
    private ArrayList<String> imageList;
    private Integer seconds;

    public CardMetadata(Card card){
        id = card.getId();
        title = card.getTitle();
        message = card.getMessage();
        hours = card.getHours();
        minutes = card.getMinutes();
        imageList = new ArrayList<>();
        ArrayList<Uri> imageUriList = card.getImageList();
        if(imageUriList != null){
            for (Uri uri: imageUriList) {
                imageList.add(uri.toString());
            }
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
