package com.senku.netflix_clone.MainScreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.senku.netflix_clone.R;

public class MainScreen extends AppCompatActivity {
    TextView movietext, tvseriestext;

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
    }
}