package com.senku.netflix_clone.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.senku.netflix_clone.R;

public class StepTwo extends AppCompatActivity {
    String planName, planCost, planCostFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_step_two);
        Intent intent = getIntent();
        planName = intent.getStringExtra("PlanName");
        planCost = intent.getStringExtra("PlanCost");
        planCostFormat = intent.getStringExtra("PlanCostFormat");
        Toast.makeText(this, ""+planName+"\n"+planCost+"\n"+planCostFormat, Toast.LENGTH_LONG).show();

    }
}