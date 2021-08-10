package com.senku.netflix_clone.Activities;

import android.app.Activity;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.senku.netflix_clone.MainScreens.MainScreen;
import com.senku.netflix_clone.R;

import org.json.JSONObject;

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
    FirebaseAuth firebaseAuth;

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
                startPayment();
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
                    try {
                        Intent i = new Intent(PaymentGateway.this, MainScreen.class);
                        startActivity(i);
                    }
                    catch (Exception e){
                        Log.e(TAG, "error occurred in onPayment's success - [onComplete]");
                    }
                }
            });
        }
        catch (Exception e){
            Log.e(TAG, "error occurred in onPayment success",e);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "payment unsuccessful",Toast.LENGTH_LONG).show();
    }
}