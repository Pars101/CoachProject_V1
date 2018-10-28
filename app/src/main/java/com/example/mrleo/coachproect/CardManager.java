package com.example.mrleo.coachproect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class CardManager {
    private final String CARD_FILE_NAME = "cardFile.txt";

    private ArrayList<Card> cardSet;
    private int currentCardIndex;

    public CardManager() {
        this(null);
    }

    public CardManager(UUID cardId){
        cardSet = readCardSet();
        if(cardSet.isEmpty()){
            currentCardIndex = -1;
        }
        else{
            currentCardIndex = 0;
            if(cardId != null){
                for (int i = 0; i < cardSet.size(); i++) {
                    if(cardId.equals(cardSet.get(i).getId())){
                        currentCardIndex = i;
                        break;
                    }
                }
            }
        }
    }

    public Card getCurrentCard(){
        return isValidIndex(currentCardIndex) ? cardSet.get(currentCardIndex) : null;
    }

    public Card getNextCard(){
        if(hasNextCard()){
            currentCardIndex++;
            return cardSet.get(currentCardIndex);
        }

        return null;
    }

    public boolean hasNextCard(){
        return isValidIndex(currentCardIndex + 1);
    }

    public Card getPrevCard(){
        if(hasPrevCard()){
            currentCardIndex--;
            return cardSet.get(currentCardIndex);
        }

        return null;
    }

    public boolean hasPrevCard(){
        return isValidIndex(currentCardIndex - 1);
    }

    public void addNewCard(){
        currentCardIndex++;
        cardSet.add(currentCardIndex, new Card());
    }

    public void removeCard(){
        if(isValidIndex(currentCardIndex)){
            int index = currentCardIndex;
            if(currentCardIndex == cardSet.size() - 1){
                currentCardIndex--;
            }

            cardSet.remove(index);
        }
    }

    public ArrayList<Card> getCardSet(){
        return cardSet;
    }

    public void saveCardSet(){
        grantPersistentAccess(cardSet);

        Context context = MainApplication.getAppContext();
        try {
            FileOutputStream fout = context.openFileOutput(CARD_FILE_NAME, Context.MODE_PRIVATE);
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
    }

    private ArrayList<Card> readCardSet(){
        Context context = MainApplication.getAppContext();
        File file = context.getFileStreamPath(CARD_FILE_NAME);
        if(file != null && file.exists()) {
            try {
                FileInputStream fis = context.openFileInput(CARD_FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                ArrayList<Card> cards = getCardList((ArrayList<CardMetadata>) ois.readObject());
                ois.close();
                fis.close();
                return cards;
            } catch (IOException e) {
                Log.i("IOExceptionCardSet", e.getMessage());
            } catch (ClassNotFoundException e) {
                Log.i("ClassNotFound", e.getMessage());
            }
        }

        return new ArrayList<Card>();
    }
    
    private static ArrayList<CardMetadata> getCardMetadataList(ArrayList<Card> cards){
        ArrayList<CardMetadata> cardMetadataList = new ArrayList<CardMetadata>();
        if(cards != null){
            for (Card card: cards) {
                cardMetadataList.add(new CardMetadata(card));
            }
        }
        
        return cardMetadataList;
    }

    private static ArrayList<Card> getCardList(ArrayList<CardMetadata> cardMetadataList){
        ArrayList<Card> cards = new ArrayList<Card>();
        if(cardMetadataList != null){
            for (CardMetadata cardMetadata: cardMetadataList) {
                cards.add(new Card(cardMetadata));
            }
        }

        return cards;
    }

    private static void grantPersistentAccess(ArrayList<Card> cards){
        if(cards != null){
            for (Card card: cards) {
                ArrayList<Uri> imageUriList = card.getImageList();
                if(imageUriList != null){
                    for (Uri uri: imageUriList) {
                        MainApplication.getAppContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }
            }
        }
    }

    private boolean isValidIndex(int index){
        return index >= 0 && index < cardSet.size();
    }
}
