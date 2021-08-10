package com.senku.netflix_clone.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.senku.netflix_clone.R;

import java.util.Objects;

public class ChooseYourPlan extends AppCompatActivity {
    TextView signinTextview;
    Button continueButton;
    RadioButton radioBasic, radioStandard, radioPremium;
    String planName, planCost, planFormatOfCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_choose_your_plan);
        signinTextview = findViewById(R.id.signinstepone);
        continueButton = findViewById(R.id.continue_btn);
        radioBasic = findViewById(R.id.radiobtnforbasic);
        radioBasic.setOnCheckedChangeListener(new Radio_check()); // making a onCheckedChangeListener through class implementation
        radioPremium = findViewById(R.id.premieumradiobtnforbasic);
        radioPremium.setOnCheckedChangeListener(new Radio_check());
        radioStandard = findViewById(R.id.standardradiobtnforbasic);
        radioStandard.setOnCheckedChangeListener(new Radio_check());
        radioPremium.setChecked(true); // by default checked

        signinTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseYourPlan.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        // on click of continue button , the plan details are being transferred using put extra
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseYourPlan.this, FinishUpAccount.class);
                intent.putExtra("PlanName", planName);
                intent.putExtra("PlanCost", planCost);
                intent.putExtra("PlanCostFormat", planFormatOfCost);
                startActivity(intent);
            }
        });

    }

    // listener for radio buttons on plan activity
    class Radio_check implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                if(buttonView.getId() == R.id.radiobtnforbasic){
                    planName = "Basic";
                    planCost = "349";
                    planFormatOfCost = "₹349/month";
                    radioStandard.setChecked(false);
                    radioPremium.setChecked(false);
                }
                if(buttonView.getId() == R.id.standardradiobtnforbasic){
                    planName = "Standard";
                    planCost = "649";
                    planFormatOfCost = "₹649/month";
                    radioBasic.setChecked(false);
                    radioPremium.setChecked(false);
                }
                if(buttonView.getId() == R.id.premieumradiobtnforbasic){
                    planName = "Premium";
                    planCost = "799";
                    planFormatOfCost = "₹799/month";
                    radioStandard.setChecked(false);
                    radioBasic.setChecked(false);
                }
            }
        }
    }
}