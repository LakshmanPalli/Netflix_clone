package com.senku.netflix_clone.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.senku.netflix_clone.R;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();
    }
}