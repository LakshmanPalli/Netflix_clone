package com.senku.netflix_clone.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
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
    String authemail, authpassword, resetemail;

    String firebaseEmail, firebaseFirstname, firebaseLastname, firebaseContact;
    String UserId;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference userReference;

    Date validDate, today; //to check plan tenure

    static int duration= 1000;
    static int counter = 0;
    final String DEBUG = "Debugging check proprietary";
    final String TAG = "SigninActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signin);
        progressBar = findViewById(R.id.signinprogressbar);
        progressBar.setVisibility(View.GONE);
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
            progressBar.setVisibility(View.VISIBLE);

            if (authemail.length()>8 && authemail.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$") && authpassword.length()>7) {
                firebaseAuth.signInWithEmailAndPassword(authemail, authpassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        UserId = firebaseAuth.getCurrentUser().getUid(); //getting current authorised user ID from Firebase Auth
                        userReference = firebaseFirestore.collection("Users").document(UserId); //document referencing to the current user
                        userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                // Getting the values from firebase 'FireStore' using `documentSnapshot`
                                validDate = documentSnapshot.getDate("Valid_date");
                                firebaseFirstname = documentSnapshot.getString("First_Name");
                                firebaseLastname = documentSnapshot.getString("Last_Name");
                                firebaseEmail = documentSnapshot.getString("Email");
                                firebaseContact = documentSnapshot.getString("Contact_Number");

                                Log.i(DEBUG, "value of validDate: "+validDate+"value of today"+today); //--testing
                                if (validDate != null && today != null) { //this if is to remove some errors if in case validDate & today variables are nulls
                                    if (validDate.compareTo(today) >= 0) { //if the validDate > today => 1; validDate == today => 0; validDate < today => -ve;
                                        Intent i = new Intent(SigninActivity.this, MainScreen.class);
                                        startActivity(i);
                                    } else {
                                        Intent i = new Intent(SigninActivity.this, PaymentOverdue.class);
                                        i.putExtra("firstname", firebaseFirstname);
                                        i.putExtra("lasttname", firebaseLastname);
                                        i.putExtra("email", authemail);
                                        i.putExtra("contact", firebaseContact);
                                        i.putExtra("userid", UserId);
                                        startActivity(i);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                                else Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        if(task.getException() instanceof FirebaseNetworkException){
                            Toast.makeText(getApplicationContext(), "No internet connection",Toast.LENGTH_LONG).show(); //when a network failure
                            progressBar.setVisibility(View.GONE);
                        }
                        if (task.getException() instanceof FirebaseAuthInvalidUserException){
                            Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_LONG).show();
                        }
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                           Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "onCreate: ",task.getException());
                        }
                        progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.GONE);
            }
        });
        signuptextview.setOnClickListener(v -> {
            Intent intent = new Intent(SigninActivity.this, SwipeScreen.class);
            startActivity(intent);
        });
        forgotpasswordtextview.setOnClickListener(new View.OnClickListener() {//when clicked forgot password, we offer a dialog box to ask if wanted a reset link or not through options: "YES"/"NO"
            @Override
            public void onClick(View view) {
                resetemail = siginEmail.getText().toString();
                if(resetemail.length()>8 && resetemail.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")){
                    AlertDialog.Builder passwordreset = new AlertDialog.Builder(view.getContext());
                    passwordreset.setTitle("Reset Password");
                    passwordreset.setMessage("Press YES to receive the reset link");
                    passwordreset.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseAuth.sendPasswordResetEmail(resetemail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Email reset link sent", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Sorry, email reset link not sent, NO USER FOUND!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    passwordreset.create().show();
                }
                else{siginEmail.setError("Enter a valid email");}
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