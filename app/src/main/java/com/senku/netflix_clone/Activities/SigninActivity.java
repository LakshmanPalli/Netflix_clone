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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.senku.netflix_clone.MainScreens.MainScreen;
import com.senku.netflix_clone.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SigninActivity extends AppCompatActivity {
    EditText siginEmail, signinPassword;
    TextView signuptextview, forgotpasswordtextview;
    ProgressBar progressBar;
    Button signinbtn;
    String authemail, authpassword;

    String UserId, valiDateString;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference userReference;

    Date validDate, today; //to check plan tenure

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

        firebaseFirestore = FirebaseFirestore.getInstance();

        //For checking today date(still in plan or not) calendar
        Calendar calendar = Calendar.getInstance();
        today = calendar.getTime();



        signinbtn.setOnClickListener(v -> {
            authemail = siginEmail.getText().toString();
            authpassword = signinPassword.getText().toString();
            if (authemail.length()>8 && authemail.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$") && authpassword.length()>7) {
                firebaseAuth.signInWithEmailAndPassword(authemail, authpassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        UserId = firebaseAuth.getCurrentUser().getUid(); //getting current authorised user ID from Firebase Auth
                        userReference = firebaseFirestore.collection("Users").document(UserId); //document referencing to the current user
                        userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                /*
                                // if date format doesn't work then use following string method
                                valiDateString = documentSnapshot.getString("Valid_date");
                                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                                try {
                                    validDate=format.parse(valiDateString);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                } **/
                                validDate = documentSnapshot.getDate("Valid_date");
                                if (validDate.compareTo(today)>=0){ //if the validDate > today => 1; validDate == today => 0; validDate < today => -ve;
                                    Intent i = new Intent(SigninActivity.this, MainScreen.class);
                                    startActivity(i);
                                }else {
                                    Toast.makeText(getApplicationContext(), "Plan Expired", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        Toast.makeText(getApplicationContext(), "Wrong email and password", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                if (authemail.length()==0) siginEmail.setError("Email field cannot be empty");
                else if (authpassword.length()==0) signinPassword.setError("Password field cannot be empty");
                else if (authemail.length() <= 8 || !authemail.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")){
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