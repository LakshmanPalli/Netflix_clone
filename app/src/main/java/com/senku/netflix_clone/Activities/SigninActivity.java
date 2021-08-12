package com.senku.netflix_clone.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.senku.netflix_clone.MainScreens.MainScreen;
import com.senku.netflix_clone.R;

import java.util.Timer;
import java.util.TimerTask;

public class SigninActivity extends AppCompatActivity {
    EditText siginEmail, signinPassword;
    TextView signuptextview, forgotpasswordtextview;
    ProgressBar progressBar;
    Button signinbtn;
    String authemail, authpassword;

    FirebaseAuth firebaseAuth;

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
        siginEmail = findViewById(R.id.emailedittext);
        signinPassword = findViewById(R.id.passwordedittext);

        firebaseAuth = FirebaseAuth.getInstance();

        signinbtn.setOnClickListener(v -> {
            authemail = siginEmail.getText().toString();
            authpassword = signinPassword.getText().toString();
            if (authemail.length()>8 && authemail.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$") && authpassword.length()>7) {
                firebaseAuth.signInWithEmailAndPassword(authemail, authpassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(SigninActivity.this, MainScreen.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong email and password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                if (authemail.length() <= 8 || !authemail.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")){
                    siginEmail.setError("Enter a valid email");
                }
                else if (authpassword.length() < 8){
                    signinPassword.setError("Enter a valid password");
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signuptextview.setOnClickListener(v -> {
            Intent intent = new Intent(SigninActivity.this, SwipeScreen.class);
            startActivity(intent);
        });
        forgotpasswordtextview.setOnClickListener(v -> Toast.makeText(SigninActivity.this, "Forgot password", Toast.LENGTH_SHORT).show());
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