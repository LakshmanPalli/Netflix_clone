package com.senku.netflix_clone.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class PaymentOverdue extends AppCompatActivity implements PaymentResultListener {
    TextView signinTextview;
    Button payButton;
    RadioButton radioBasic, radioStandard, radioPremium;
    String planName, planCost, planFormatOfCost;
    String getFirstName, getLastName, getEmail, getUserId, getContact; // variables received from sent Intent
    final String TAG = "PaymentOverdue Error";

    Date validDate, today; //to check plan tenure

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //receiving the `Extras` sent through Intent from [SigninActivity]
        Intent i = getIntent();
        getFirstName = i.getStringExtra("firstname");
        getLastName = i.getStringExtra("lastname");
        getEmail = i.getStringExtra("email");
        getUserId = i.getStringExtra("userid");
        getContact = i.getStringExtra("contact");

        setContentView(R.layout.activity_payment_overdue);
        signinTextview = findViewById(R.id.signinstepone);
        payButton = findViewById(R.id.pay_btn);
        radioBasic = findViewById(R.id.radiobtnforbasic);
        radioBasic.setOnCheckedChangeListener(new Radio_check()); // making a onCheckedChangeListener through class implementation
        radioPremium = findViewById(R.id.premieumradiobtnforbasic);
        radioPremium.setOnCheckedChangeListener(new Radio_check());
        radioStandard = findViewById(R.id.standardradiobtnforbasic);
        radioStandard.setOnCheckedChangeListener(new Radio_check());
        radioPremium.setChecked(true); // by default checked

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        //For checking today date, compute validDate accordingly using calendar
        Calendar calendar = Calendar.getInstance();
        today = calendar.getTime();
        calendar.add(Calendar.MONTH, 1); //valid date in one month later
        validDate = calendar.getTime();

        signinTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentOverdue.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        // on click of continue button , the plan details are being transferred using put extra
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });
    }
    public void startPayment(){
        Checkout checkout = new Checkout();
        final Activity activity = this;
        String name = getFirstName+getLastName;
        try {
            JSONObject options = new JSONObject(); // to sent json data to web
            options.put("name", name);
            options.put("description", "APP PAYMENT");
            options.put("currency", "INR");
            // by default it is in paisa , so to change it in rupees
            double cost = Double.parseDouble(planCost);
            cost = cost*100;
            options.put("amount", cost);
            options.put("prefill.email", getEmail);
            options.put("prefill.contact", getContact);
            checkout.open(activity, options); // checkout.open() takes activity, the JSON object to work, open payment
        } catch (Exception e){
            Log.e(TAG, "error occurs",e);
        }
    }

    @Override //both onPaymentSuccess , onPaymentError comes with the `PaymentResultListener` Interface implementation to the class
    public void onPaymentSuccess(String s)
    {
        try {
            DocumentReference documentReference = firebaseFirestore.collection("Users").document(getUserId);
            Map<String,Object> user = new HashMap<>();
            user.put("Email", getEmail);
            user.put("First_Name", getFirstName);
            user.put("Last_Name", getLastName);
            user.put("Plan_Cost", planCost);
            user.put("Contact_Number", getContact);
            user.put("Valid_date", validDate);
            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent i = new Intent(PaymentOverdue.this, MainScreen.class);
                    startActivity(i);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Values not stored",Toast.LENGTH_SHORT).show();
                }
            });
            }
        catch (Exception e){
            Log.e(TAG, "error occurred in onPayment success",e);
        }
    }

    @Override
    public void onPaymentError(int i, String s)
    {
        Toast.makeText(this, "payment unsuccessful",Toast.LENGTH_LONG).show();
    }

    // listener for radio buttons on plan activity
    class Radio_check implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                if(buttonView.getId() == R.id.radiobtnforbasic){
                    planName = "Basic";
                    planCost = "349";
                    planFormatOfCost = "₹349/month";
                    radioStandard.setChecked(false);
                    radioPremium.setChecked(false);
                }
                if(buttonView.getId() == R.id.standardradiobtnforbasic){
                    planName = "Standard";
                    planCost = "649";
                    planFormatOfCost = "₹649/month";
                    radioBasic.setChecked(false);
                    radioPremium.setChecked(false);
                }
                if(buttonView.getId() == R.id.premieumradiobtnforbasic){
                    planName = "Premium";
                    planCost = "799";
                    planFormatOfCost = "₹799/month";
                    radioStandard.setChecked(false);
                    radioBasic.setChecked(false);
                }
            }
        }
    }
}