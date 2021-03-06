package com.senku.netflix_clone.MainScreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.senku.netflix_clone.R;

public class MainScreen extends AppCompatActivity {
    TextView movietext, tvseriestext;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_screen);
        movietext = findViewById(R.id.moviesId);
        tvseriestext = findViewById(R.id.tvseriesId);
        movietext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainScreen.this, Movies.class);
                startActivity(i);
            }
        });
        tvseriestext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainScreen.this, TvSeries.class);
                startActivity(i);
            }
        });
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuHomeItem = menu.getItem(0);
        menuHomeItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeicon:
                        break;
                    case R.id.serachicon:
                        Intent i = new Intent(MainScreen.this, Search.class);
                        startActivity(i);
                        break;
                    case R.id.settingsicon:
                        Intent j = new Intent(MainScreen.this, Settings.class);
                        startActivity(j);
                        break;
                }
                return false;
            }
        });
    }
}