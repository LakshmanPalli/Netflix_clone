package com.senku.netflix_clone.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.senku.netflix_clone.MainScreens.MainScreen;
import com.senku.netflix_clone.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference reference;
    Date today, validDate;
    String Uid, firebaseFirstname, firebaseLastname, firebaseEmail, firebaseContact;

    static int counter;
    static int duration = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressbar);
        Calendar calendar = Calendar.getInstance();
        today = calendar.getTime();

        progress();
        start();
    }

    public void progress(){
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
                progressBar.setProgress(counter);
                if (counter == duration){
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }
    public void start(){
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseAuth.getCurrentUser() != null) //if you are a pre-existing user then its == null
                {
                    Uid=firebaseAuth.getCurrentUser().getUid();
                    reference = firebaseFirestore.collection("Users").document(Uid); //document referencing to the current user
                    reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            // Getting the values from firebase 'FireStore' using `documentSnapshot`
                            validDate = documentSnapshot.getDate("Valid_date");
                            firebaseFirstname = documentSnapshot.getString("First_Name");
                            firebaseLastname = documentSnapshot.getString("Last_Name");
                            firebaseEmail = documentSnapshot.getString("Email");
                            firebaseContact = documentSnapshot.getString("Contact_Number");

                            if (validDate != null && today != null) { //this if is to remove some errors if in case validDate & today variables are nulls
                                if (validDate.compareTo(today) >= 0) { //if the validDate > today => 1; validDate == today => 0; validDate < today => -ve;
                                    Intent i = new Intent(SplashScreen.this, MainScreen.class);
                                    startActivity(i);
                                    finish();

                                } else {
                                    Intent i = new Intent(SplashScreen.this, PaymentOverdue.class);
                                    i.putExtra("firstname", firebaseFirstname);
                                    i.putExtra("lasttname", firebaseLastname);
                                    i.putExtra("email", firebaseEmail);
                                    i.putExtra("contact", firebaseContact);
                                    i.putExtra("userid", Uid);
                                    startActivity(i);
                                    finish();
                                }
                            }
                            else Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseNetworkException)
                            {
                                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(getApplicationContext(), "Error data not fetched", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                startActivity(new Intent(SplashScreen.this,SigninActivity.class));
                finish();}
            }
        }, duration);
    };
}