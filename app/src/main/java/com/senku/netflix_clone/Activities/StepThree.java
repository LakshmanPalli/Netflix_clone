package com.senku.netflix_clone.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.senku.netflix_clone.R;

public class StepThree extends AppCompatActivity {
    String planName, planCost, planCostFormat;
    String useremail, userpassword;
    TextView signoutTextview, stepthreeofthree;
    LinearLayout paymentlinearlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_step_three);

        signoutTextview = findViewById(R.id.signoutstepthree);
        stepthreeofthree = findViewById(R.id.step3of3);
        paymentlinearlayout = findViewById(R.id.paymentlinearlayout);

        signoutTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepThree.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        SpannableString st = new SpannableString("Step 1 OF 3");
        StyleSpan boldspan = new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan1 = new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan, 5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan1, 10,11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stepthreeofthree.setText(st);

        Intent intent = getIntent();
        planName = intent.getStringExtra("PlanName");
        planCost = intent.getStringExtra("PlanCost");
        planCostFormat = intent.getStringExtra("PlanCostFormat");
        useremail = intent.getStringExtra("EmailId");
        userpassword = intent.getStringExtra("Password");
        Toast.makeText(this, ""+planName+"\n"+planCost+"\n"+planCostFormat+"\n"+useremail+"\n"+userpassword, Toast.LENGTH_LONG).show();

        paymentlinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StepThree.this, PaymentGateway.class);
                intent.putExtra("PlanName", planName);
                intent.putExtra("PlanCost", planCost);
                intent.putExtra("PlanCostFormat", planCostFormat);
                startActivity(intent);
            }
        });
    }
}