package com.example.mrleo.coachproect;

import android.content.Intent;
import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.UUID;

public class EnterProgram extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_program);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    drawerLayout.closeDrawers();
                    createCoachInterface();
                    return true;
                }
            });

        Button buttonStart = findViewById(R.id.buttonEnterProgram);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createStudentInterface(null);
            }
        });

        CardManager cardManager = new CardManager(AlarmCardManager.readAlarmCardId());
        ListView cardListView = findViewById(R.id.cardListView);
        ListAdapter listAdapter = new CardAdapter(this, cardManager.getCardSet());
        cardListView.setAdapter(listAdapter);
        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Card card = (Card)adapterView.getItemAtPosition(i);
                createStudentInterface(card.getId());
            }
        });

        //MediaPlayer.create(MainApplication.getAppContext(), Settings.System.DEFAULT_RINGTONE_URI).start();
    }

    private void createStudentInterface(UUID cardId)
    {
        Intent studentIntent = new Intent(this, StudentInterface.class);
        studentIntent.putExtra("ENTRY", cardId);
        startActivity(studentIntent);
    }

    private void createCoachInterface()
    {
        Intent editIntent = new Intent(this, CoachInterface.class);
        startActivity(editIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
