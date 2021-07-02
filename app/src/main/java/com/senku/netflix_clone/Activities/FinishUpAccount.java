package com.senku.netflix_clone.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.senku.netflix_clone.R;

public class FinishUpAccount extends AppCompatActivity {
    String planName, planCost, planCostFormat;
    TextView step0ne0fThree, signinTextview;
    Button continueBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_finish_up_account);
        step0ne0fThree = findViewById(R.id.step1of3finish);
        signinTextview = findViewById(R.id.signinstepone);
        continueBtn = findViewById(R.id.continueBtnFinish);


        Intent intent = getIntent();
        planName = intent.getStringExtra("PlanName");
        planCost = intent.getStringExtra("PlanCost");
        planCostFormat = intent.getStringExtra("PlanCostFormat");
        Toast.makeText(this, ""+planName+"\n"+planCost+"\n"+planCostFormat, Toast.LENGTH_LONG).show();


        SpannableString st = new SpannableString("Step 1 OF 3");
        StyleSpan boldspan = new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan1 = new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan, 5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan1, 10,11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        step0ne0fThree.setText(st);


        signinTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishUpAccount.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        // on click of continue button , the plan details are being transferred using put extra
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishUpAccount.this, StepTwo.class);
                intent.putExtra("PlanName", planName);
                intent.putExtra("PlanCost", planCost);
                intent.putExtra("PlanCostFormat", planCostFormat);
                startActivity(intent);
            }
        });

    }
}