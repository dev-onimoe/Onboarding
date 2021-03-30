package com.reedtech.electronics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.reedtech.electronics.adapters.Walkthrough_Adapter;

public class walkthrough_Activity extends AppCompatActivity {
    public static ViewPager viewPager;

    private Walkthrough_Adapter walkthroughAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        this.getWindow().setStatusBarColor(Color.TRANSPARENT);


        viewPager = findViewById(R.id.wt_viewpager);



        walkthroughAdapter = new Walkthrough_Adapter(this);
        viewPager.setAdapter(walkthroughAdapter);
        viewPager.addOnPageChangeListener(walkthroughAdapter.viewlistener);


    }
}