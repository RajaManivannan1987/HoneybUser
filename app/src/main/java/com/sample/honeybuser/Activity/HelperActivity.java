package com.sample.honeybuser.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sample.honeybuser.Adapter.HelperViewPager;
import com.sample.honeybuser.EnumClass.IntentClasses;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by Im033 on 11/24/2016.
 */

public class HelperActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    SharedPreferences pref = null;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private Button skipButton;
    private int[] mImageResources = {
            R.drawable.helpscreen,
            R.drawable.helpscreen1,

    };
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helps);

        init();
        /*pref = getSharedPreferences("com.sample.honeybuser.HelperActivity", MODE_APPEND);
        if (pref.getBoolean("firstrun", true)) {
            setContentView(R.layout.activity_helps);
            init();
            pref.edit().putBoolean("firstrun", false).commit();
        } else {
            CommonMethods.commonIntent(HelperActivity.this, IntentClasses.SPLASH);
        }*/


//        toolbar.setVisibility(View.GONE);

    }

    private void init() {
        for (int i = 0; i < mImageResources.length; i++) {
            ImagesArray.add(mImageResources[i]);
        }
        skipButton = (Button) findViewById(R.id.skipButton);
        mPager = (ViewPager) findViewById(R.id.pager_introduction);
        mPager.setAdapter(new HelperViewPager(HelperActivity.this, ImagesArray));
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);
        NUM_PAGES = mImageResources.length;

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.commonIntent(HelperActivity.this, IntentClasses.LOCATIONCHECK);
            }
        });

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
