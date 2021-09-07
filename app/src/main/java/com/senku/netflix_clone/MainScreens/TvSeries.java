package com.senku.netflix_clone.MainScreens;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.senku.netflix_clone.R;

public class TvSeries extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tv_series);
    }
}