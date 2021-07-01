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

import com.senku.netflix_clone.R;

public class StepOne extends AppCompatActivity {
    TextView signinTextview, steponeofthree;
    Button seeyourplanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_step_one);
        signinTextview = findViewById(R.id.signinstepone);
        seeyourplanBtn = findViewById(R.id.seeyourplanbtn);
        steponeofthree = findViewById(R.id.steponeofthree);

        signinTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepOne.this, SigninActivity.class);
                startActivity(intent);
            }
        });
        seeyourplanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepOne.this, ChooseYourPlan.class);
                startActivity(intent);
            }
        });

        SpannableString st = new SpannableString("Step 1 OF 3");
        StyleSpan boldspan = new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan1 = new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan, 5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan1, 10,11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        steponeofthree.setText(st);


    }
}