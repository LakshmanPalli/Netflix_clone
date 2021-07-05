package com.senku.netflix_clone.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.senku.netflix_clone.MainScreens.MainScreen;
import com.senku.netflix_clone.R;

import java.util.Timer;
import java.util.TimerTask;

public class SigninActivity extends AppCompatActivity {
    TextView signuptextview, forgotpasswordtextview;
    ProgressBar progressBar;
    Button signinbtn;

    static int duration= 1000;
    static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signin);
        progressBar = findViewById(R.id.signinprogressbar);
        progressBar.setVisibility(View.GONE);
        progress(); //initially progressbar is absent, it appears later
        signuptextview = findViewById(R.id.signuptextview);
        forgotpasswordtextview = findViewById(R.id.forgotpasswordtextview);
        signinbtn = findViewById(R.id.Btn_signin);

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, MainScreen.class);
                startActivity(intent);
            }
        });
        signuptextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, SwipeScreen.class);
                startActivity(intent);
            }
        });
        forgotpasswordtextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SigninActivity.this, "Forgot password", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // code for progress bar to work for a counter time
    public void progress(){
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
                if (counter == duration){
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }
}