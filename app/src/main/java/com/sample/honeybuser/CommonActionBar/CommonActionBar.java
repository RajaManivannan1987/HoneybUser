package com.sample.honeybuser.CommonActionBar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.honeybuser.R;

/**
 * Created by Im033 on 10/14/2016.
 */

public class CommonActionBar extends AppCompatActivity {
    private FrameLayout frameLyout;
    private Toolbar toolbar;
    private ImageView actionBarViewBackImageView, actionBarIsOnlineImageView;
    private TextView titleTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_actionbar);
        frameLyout = (FrameLayout) findViewById(R.id.actionBarViewFrameLayout);
        actionBarViewBackImageView = (ImageView) findViewById(R.id.actionBarViewBackImageView);
        actionBarIsOnlineImageView = (ImageView) findViewById(R.id.backActionBarViewStatusImageView);
        titleTextView = (TextView) findViewById(R.id.actionBarViewTitleTextView);
        toolbar = (Toolbar) findViewById(R.id.actionBarViewToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarViewBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setView(int layoutId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutId, null, false);
        frameLyout.addView(view);
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setNotification(String status) {
        if (status.equalsIgnoreCase("Y")) {
            actionBarIsOnlineImageView.setImageResource(R.drawable.on);
        } else if (status.equalsIgnoreCase("N")) {
            actionBarIsOnlineImageView.setImageResource(R.drawable.off);
        } else {
            actionBarIsOnlineImageView.setVisibility(View.GONE);
        }
    }

    public void hideNotification() {
        actionBarIsOnlineImageView.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();

    }
}
