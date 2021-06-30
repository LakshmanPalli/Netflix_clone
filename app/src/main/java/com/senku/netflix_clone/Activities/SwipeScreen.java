package com.senku.netflix_clone.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.senku.netflix_clone.Adapters.ViewPagerAdapter;
import com.senku.netflix_clone.R;

public class SwipeScreen extends AppCompatActivity {
    TextView signin, help, privacy;
    Button getstarted;
    ViewPager viewpagerswipe;
    LinearLayout sliderdots;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_screen);
        getSupportActionBar().hide();

        help=findViewById(R.id.helptextview);
        signin=findViewById(R.id.signintextview);
        privacy=findViewById(R.id.privacytextview);
        getstarted=findViewById(R.id.gettingstartedbtn);
        viewpagerswipe=findViewById(R.id.viewpager);
        sliderdots=findViewById(R.id.sliderdots);
        ViewPagerAdapter viewadapter = new ViewPagerAdapter(this);
        viewpagerswipe.setAdapter(viewadapter);
        // coding for slider dots(viewPager) functionality
        dotscount = viewadapter.getCount();
        dots=new ImageView[dotscount];
        for (int i=0; i<dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.inactivedots));
            //into linear layout
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,10,4,0);
            sliderdots.addView(dots[i],params);
        }
            viewpagerswipe.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i=0; i<dotscount; i++){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.inactivedots));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.activedots));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        // for toolbar text views & getting started button
        signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SwipeScreen.this, SigninActivity.class);
                    startActivity(intent);
                }
            });

        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwipeScreen.this, StepOne.class);
                startActivity(intent);
            }
        });


        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/en/")));
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/en/node/100628")));
            }
        });


    }
}