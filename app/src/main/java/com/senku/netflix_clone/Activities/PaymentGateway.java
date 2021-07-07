package com.senku.netflix_clone.Activities;

import
        androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.senku.netflix_clone.R;

public class PaymentGateway extends AppCompatActivity {
    String planName, planCost, planCostFormat;
    String useremail, userpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_payment_gateway);

        Intent intent = getIntent();
        planName = intent.getStringExtra("PlanName");
        planCost = intent.getStringExtra("PlanCost");
        planCostFormat = intent.getStringExtra("PlanCostFormat");
        useremail = intent.getStringExtra("EmailId");
        userpassword = intent.getStringExtra("Password");
        Toast.makeText(this, ""+planName+"\n"+planCost+"\n"+planCostFormat+"\n"+useremail+"\n"+userpassword, Toast.LENGTH_LONG).show();

    }
}