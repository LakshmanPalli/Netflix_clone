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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.senku.netflix_clone.R;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class StepTwo extends AppCompatActivity {
    String planName, planCost, planCostFormat;
    String useremail, userpassword;
    ProgressBar progressBar;
    Button continueBtn;
    TextView signinTextview, steptwoofthree;
    int counter =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_step_two);
        signinTextview = findViewById(R.id.signinstepone);
        steptwoofthree = findViewById(R.id.steptwoofthree);
        progressBar = findViewById(R.id.steptwoprogressBar);
        progressBar.setVisibility(View.GONE);
        continueBtn = findViewById(R.id.continue_btn_step_two);
        EditText email = findViewById(R.id.emailedittextstep2) ;
        EditText password = findViewById(R.id.passwordedittextstep2);


        Intent intent = getIntent();
        planName = intent.getStringExtra("PlanName");
        planCost = intent.getStringExtra("PlanCost");
        planCostFormat = intent.getStringExtra("PlanCostFormat");
        Toast.makeText(this, ""+planName+"\n"+planCost+"\n"+planCostFormat, Toast.LENGTH_LONG).show();


        signinTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepTwo.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        // on click of continue button , the plan details are being transferred using put extra
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useremail = email.getText().toString();
                userpassword = password.getText().toString();
                Intent intent = new Intent(StepTwo.this, StepThree.class);
                intent.putExtra("PlanName", planName);
                intent.putExtra("PlanCost", planCost);
                intent.putExtra("PlanCostFormat", planCostFormat);
                intent.putExtra("EmailId", useremail);
                intent.putExtra("Password", userpassword);
                startActivity(intent);
            }
        });
    // to bold step 2 of 3 text
        SpannableString st = new SpannableString("Step 2 OF 3");
        StyleSpan boldspan = new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan1 = new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan, 5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan1, 10,11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        steptwoofthree.setText(st);
    }
    // for progress bar manual timer schedule for rotation
    public void progress(){
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
                if (counter == 500){
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask,0,500);
    }
}