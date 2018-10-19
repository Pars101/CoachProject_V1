package com.example.mrleo.coachproect;

import java.io.Serializable;
import java.util.ArrayList;

public class CardMetadata implements Serializable {
    private String message;
    private ArrayList<String> imageList;
    private Integer seconds;

    public CardMetadata(String message, ArrayList<String> imageList, Integer seconds) {
        this.message = message;
        this.imageList = imageList;
        this.seconds = seconds;
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
}
