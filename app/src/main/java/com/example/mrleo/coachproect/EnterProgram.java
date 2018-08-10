package com.example.mrleo.coachproect;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class EnterProgram extends AppCompatActivity {

    private static Intent studentIntent;
    private static Intent editIntent;
    private static Intent enterIntent;
    private static ArrayList<Card> cardSet = new ArrayList<>();
    private static int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_program);

        enterIntent = new Intent(this, EnterProgram.class);

        readCardSet(this.getApplicationContext());

        Button buttonStart = findViewById(R.id.buttonEnterProgram);
        Button buttonEdit = findViewById(R.id.buttonEditProgram);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createStudentInterface();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCoachInterface();
            }
        });

        if(StudentInterface.getTimerIsActive()){
            StudentInterface.setCurrentIndex(EnterProgram.currentIndex);
            createStudentInterface();
        }
    }

    private void createStudentInterface()
    {
        studentIntent = new Intent(this, StudentInterface.class);
        startActivity(studentIntent);
    }

    private void createCoachInterface()
    {
        editIntent = new Intent(this, CoachInterface.class);
        startActivity(editIntent);
    }

    public static void addCard(Card card){
        cardSet.add(card);
    }

    public static Card getCard(int i){
        if(cardSet.isEmpty()){
            addCard(new Card("", 0));
        }
        return cardSet.get(i);
    }

    public static void setCardSet(Context context){
        try {
            FileOutputStream fout = context.openFileOutput("cardFile.txt", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(cardSet);
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

    public static void readCardSet(Context context){
        try {
            FileInputStream fis = context.openFileInput("cardFile.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            cardSet = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            Log.i("IOExceptionCardSet", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.i("ClassNotFound", e.getMessage());
        }
    }

    public static Integer getCardSetLength(){
        return cardSet.size();
    }

    public static void setCurrentIndex(int currentIndex){
        EnterProgram.currentIndex = currentIndex;
    }
}
