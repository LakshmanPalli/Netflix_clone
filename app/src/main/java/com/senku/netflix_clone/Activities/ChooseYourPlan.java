package com.senku.netflix_clone.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.senku.netflix_clone.R;

public class ChooseYourPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_choose_your_plan);
    }
}