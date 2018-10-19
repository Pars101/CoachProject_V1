package com.example.mrleo.coachproect;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CardManager {
    private static CardManager instance = null;

    private ArrayList<Card> cardSet = new ArrayList<Card>();
    private int currentCardIndex = 0;

    private CardManager(){
        readCardSet();
    }

    public static CardManager getInstance()
    {
        if (instance == null)
            instance = new CardManager();

        return instance;
    }

    public void addCard(Card card){
        cardSet.add(card);
    }

    public Card getCard(int i){
        if(cardSet.isEmpty()){
            addCard(new Card("", 0));
        }
        return cardSet.get(i);
    }


    public Integer getCardSetLength(){
        return cardSet.size();
    }

    public void setCurrentIndex(int currentIndex){
        currentCardIndex = currentIndex;
    }

    public int getCurrentIndex(){
        return currentCardIndex;
    }

    public void saveCardSet(){
        Log.i("Setting", getCard(0).getIsInStudentInterface() + "");
        Context context = MainApplication.getAppContext();
        try {
            FileOutputStream fout = context.openFileOutput("cardFile.txt", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(getCardMetadataList(cardSet));
            oos.close();
            fout.close();
        } catch(NotSerializableException e){
            Log.i("Not Serializable", e.getMessage());
        } catch(InvalidClassException e){
            Log.i("Invalid Class", e.getMessage());
        } catch(IOException e){
            Log.i("IO Class", e.getMessage());
        }

        try {
            FileInputStream fis = context.openFileInput("cardFile.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<CardMetadata> cardSet1 = (ArrayList<CardMetadata>) ois.readObject();
            ArrayList<Card> cardSet2 = getCardList(cardSet1);
            ois.close();
            fis.close();
        } catch (IOException e) {
            Log.i("IOExceptionCardSet", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.i("ClassNotFound", e.getMessage());
        }
    }

    public void readCardSet(){
        Context context = MainApplication.getAppContext();
        try {
            FileInputStream fis = context.openFileInput("cardFile.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            cardSet = getCardList((ArrayList<CardMetadata>) ois.readObject());
            ois.close();
            fis.close();
        } catch (IOException e) {
            Log.i("IOExceptionCardSet", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.i("ClassNotFound", e.getMessage());
        }
        Log.i("Reading", getCard(0).getIsInStudentInterface() + "");
    }
    
    private static ArrayList<CardMetadata> getCardMetadataList(ArrayList<Card> cards){
        ArrayList<CardMetadata> cardMetadataList = new ArrayList<CardMetadata>();
        if(cards != null){
            for (Card card: cards) {
                ArrayList<Uri> imageUriList = card.getImageList();
                ArrayList<String> imageUriStringList = new ArrayList<String>();
                if(imageUriList != null){
                    for (Uri uri: imageUriList) {
                        imageUriStringList.add(uri.toString());
                    }
                }

                cardMetadataList.add(new CardMetadata(card.getMessage(), imageUriStringList, card.getSeconds()));
            }
        }
        
        return cardMetadataList;
    }

    private static ArrayList<Card> getCardList(ArrayList<CardMetadata> cardMetadataList){
        ArrayList<Card> cards = new ArrayList<Card>();
        if(cardMetadataList != null){
            for (CardMetadata cardMetadata: cardMetadataList) {
                ArrayList<Uri> imageUriList = new ArrayList<Uri>();
                ArrayList<String> imageUriStringList = cardMetadata.getImageList();
                if(imageUriStringList != null){
                    for (String str: imageUriStringList) {
                        imageUriList.add(Uri.parse(str));
                    }
                }

                cards.add(new Card(cardMetadata.getMessage(), imageUriList, cardMetadata.getSeconds()));
            }
        }

        return cards;
    }
}
