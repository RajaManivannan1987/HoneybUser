package com.sample.honeybuser.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sample.honeybuser.EnumClass.IntentClasses;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.Sharedpreferences.Session;

public class SplashScreen extends AppCompatActivity {
    private String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (Session.getSession(SplashScreen.this, TAG).isLogin()) {
                        CommonMethods.commonIntent(SplashScreen.this, IntentClasses.LOCATIONCHECK);
                    } else {
                        CommonMethods.commonIntent(SplashScreen.this, IntentClasses.REGISTRATION);
                    }

                }

            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
