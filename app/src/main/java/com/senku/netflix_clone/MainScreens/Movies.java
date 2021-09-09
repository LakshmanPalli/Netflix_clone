package com.senku.netflix_clone.MainScreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.senku.netflix_clone.R;

public class Movies extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_movies);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuHomeItem = menu.getItem(0);
        menuHomeItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeicon:
                        Intent k = new Intent(Movies.this, MainScreen.class);
                        startActivity(k);
                        break;
                    case R.id.serachicon:
                        Intent i = new Intent(Movies.this, Search.class);
                        startActivity(i);
                        break;
                    case R.id.settingsicon:
                        Intent j = new Intent(Movies.this, Settings.class);
                        startActivity(j);
                        break;
                }
                return false;
            }
        });
    }
}