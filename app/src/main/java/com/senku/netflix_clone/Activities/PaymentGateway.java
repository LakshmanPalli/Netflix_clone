package com.senku.netflix_clone.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.senku.netflix_clone.MainScreens.MainScreen;
import com.senku.netflix_clone.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PaymentGateway extends AppCompatActivity implements  PaymentResultListener {
    String planName, planCost, planCostFormat; // to receive, display(toast) sent extras from StepThree.java
    String firstname, lastname, contactno;
    String useremail, userpassword;
    EditText firstNameEditText, lastNameEditText, contactNumberedEditText;
    Button startYourMembership;
    CheckBox iAgree;
    TextView termstext, step3of3,changebtn, costset, planset;
    String TAG = "Payment Error";
    String userId; //for firebase firestore user id.

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    Date today, validDate;
    ProgressDialog progressDialog;// loading dialog box

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_payment_gateway);

        Intent intent = getIntent();
        planName = intent.getStringExtra("PlanName");
        planCost = intent.getStringExtra("PlanCost");
        planCostFormat = intent.getStringExtra("PlanCostFormat");
        useremail = intent.getStringExtra("EmailId");
        userpassword = intent.getStringExtra("Password");

        Checkout.preload(getApplicationContext()); //To quickly load the Checkout form, the preload method of Checkout must be called much earlier than the other methods in the payment flow.

        firstNameEditText = findViewById(R.id.firstnameEdittext);
        lastNameEditText = findViewById(R.id.lastnameEdittext);
        contactNumberedEditText = findViewById(R.id.contactnoEdittext);
        startYourMembership = findViewById(R.id.startyourmemberbtn);
        iAgree = findViewById(R.id.iagreebtn);
        termstext = findViewById(R.id.termstext);
        step3of3 = findViewById(R.id.step3of3);
        changebtn = findViewById(R.id.change);
        costset = findViewById(R.id.costtoset);
        planset = findViewById(R.id.plannametoset);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //For tallying the one month(+1) using calendar
        Calendar calendar = Calendar.getInstance();
                    today = calendar.getTime();
                    calendar.add(Calendar.MONTH, 1);
                    validDate = calendar.getTime();

        //
        planset.setText(planName);
        costset.setText(planCostFormat);

        // spannable text edit of step3 of 3
        SpannableString st = new SpannableString("STEP 3 OF 3");
        StyleSpan boldspan = new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan1 = new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan, 5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan1, 10,11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        step3of3.setText(st);

        // spanning terms n conditions statement with refing at specific terms, setting up link to url
        SpannableString ss = new SpannableString("By checking the checkbox below, you agree to our Terms of Use and Privacy");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/en")));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint dt) {
                super.updateDrawState(dt);
                dt.setColor(Color.BLUE);
            }
        };
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/legal/privacy")));
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };
        ss.setSpan(clickableSpan, 49, 61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan1, 66, 73, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termstext.setText(ss);
        termstext.setMovementMethod(LinkMovementMethod.getInstance());

        // when click on change text view, migrate to chooseplanactivity.class
        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentGateway.this, ChooseYourPlan.class);
                startActivity(intent);
            }
        });

        //when click on startyourmembership button..get the values from edittexts..
        startYourMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting strings in varaibles
                firstname = firstNameEditText.getText().toString();
                lastname = lastNameEditText.getText().toString();
                contactno = contactNumberedEditText.getText().toString();

                if (firstname.length()>3 && lastname.length()>3 && firstname.matches("[a-z A-Z]+") && lastname.matches("[a-z A-Z]+") && contactno.length()==10 && iAgree.isChecked()){ //does validation for the edittexts <firstname>,<lastname>,<contact.no>
                    progressDialog = new ProgressDialog(PaymentGateway.this);
                    progressDialog.setMessage("Loading..."); //defining the progressDailog when clicked 'start membership' btn
                    progressDialog.show();
                    startPayment();
                }
                else{ //throwing error red awareness for invalid inputs
                    if (firstname.length()<=3 || !firstname.matches("[a-z A-Z]+")){
                        firstNameEditText.setError("Enter a valid First name");
                    }
                    else if (lastname.length()<=3 || !lastname.matches("[a-z A-Z]+")){
                        lastNameEditText.setError("Enter a valid Last name");
                    }
                    else if (contactno.length() != 10){
                        contactNumberedEditText.setError("Enter a 10 digit phone number");
                    }
                    else if (!iAgree.isChecked()){
                        Toast.makeText(getApplicationContext(), "Please agree to the policy", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                }
            }
        });
    }
    public void startPayment(){
        Checkout checkout = new Checkout();
        final Activity activity = this;
        firstname = firstNameEditText.getText().toString();
        lastname = lastNameEditText.getText().toString();
        contactno = contactNumberedEditText.getText().toString();
        String name = firstname+lastname;
        try {
            JSONObject options = new JSONObject(); // to sent json data to web
            options.put("name", name);
            options.put("description", "APP PAYMENT");
            options.put("currency", "INR");
            // by default it is in paisa , so to change it in rupees
            double cost = Double.parseDouble(planCost);
            cost = cost*100;
            options.put("amount", cost);
            options.put("prefill.email", useremail);
            options.put("prefill.contact", contactno);
            checkout.open(activity, options); // checkout.open() takes activity, the JSON object to work, open payment
        } catch (Exception e){
            Log.e(TAG, "error occurs",e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(useremail, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        userId = firebaseAuth.getCurrentUser().getUid(); //to get the unique user id given by firebase auth
                        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
                        Map<String,Object> user = new HashMap<>();
                        user.put("Email", useremail);
                        user.put("First_Name", firstname);
                        user.put("Last_Name", lastname);
                        user.put("Plan_Cost", planCost);
                        user.put("Contact_Number", contactno);
                        user.put("Valid_date", validDate);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent i = new Intent(PaymentGateway.this, MainScreen.class);
                                startActivity(i);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(task.getException() instanceof FirebaseNetworkException){
                                    Toast.makeText(getApplicationContext(), "No internet connection",Toast.LENGTH_LONG).show(); //when a network failure
                                    progressDialog.cancel();
                                }
                                Toast.makeText(getApplicationContext(), "Values not stored",Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        });
                }}
            });
        }
        catch (Exception e){
            Log.e(TAG, "error occurred in onPayment success",e);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "payment unsuccessful",Toast.LENGTH_LONG).show();
        progressDialog.cancel();
    }
}