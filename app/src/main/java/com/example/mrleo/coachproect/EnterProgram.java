package com.example.mrleo.coachproect;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class EnterProgram extends AppCompatActivity {

    private static Intent studentIntent;
    private static Intent editIntent;
    private static Intent enterIntent;
    private static ArrayList<Card> cardSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_program);

        enterIntent = new Intent(this, EnterProgram.class);

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

    public static Integer getCardSetLength(){
        return cardSet.size();
    }
}
