package com.sample.honeybuser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.sample.honeybuser.CommonActionBar.NavigationBarActivity;
import com.sample.honeybuser.EnumClass.IntentClasses;
import com.sample.honeybuser.InterFaceClass.DialogBoxInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.MapIntegration.LocationServiceUpdated;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.AlertDialogManager;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.Sharedpreferences.Session;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Im033 on 10/7/2016.
 */

public class SettingsActivity extends NavigationBarActivity {
    private String TAG = "SettingsActivity";
    private Switch languageSwitch;
    Session session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_settings);
        setTitle("Settings");
        session = new Session(SettingsActivity.this, TAG);
        languageSwitch = (Switch) findViewById(R.id.languageSwitch);
        if (Session.getSession(SettingsActivity.this, TAG).getDefaultLanguage().equalsIgnoreCase("ta")) {
            languageSwitch.setChecked(true);
        } else {
            languageSwitch.setChecked(false);
        }
        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                upDateLanguage(isChecked);
            }
        });
        findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogManager.listenerDialogBox(SettingsActivity.this, "Logout!", "Are you sure want to logout?", new DialogBoxInterface() {
                    @Override
                    public void yes() {
                        GetResponseFromServer.getWebService(SettingsActivity.this, TAG).logout(SettingsActivity.this, new VolleyResponseListerner() {
                            @Override
                            public void onResponse(JSONObject response) throws JSONException {
                                if (response.getString("status").equalsIgnoreCase("1")) {
                                    Session.getSession(SettingsActivity.this, TAG).clearSession();
                                    stopService(new Intent(SettingsActivity.this, LocationServiceUpdated.class));
                                    CommonMethods.commonIntent(SettingsActivity.this, IntentClasses.REGISTRATION);
                                }
                            }

                            @Override
                            public void onError(String message, String title) {

                            }
                        });
                    }

                    @Override
                    public void no() {

                    }
                });

            }
        });
    }

    private void upDateLanguage(boolean isChecked) {
        String language = "";
        if (isChecked) {
            language = "ta";
        } else {
            language = "en";
        }
        final String finalLanguage = language;
        GetResponseFromServer.getWebService(SettingsActivity.this, TAG).updataLanguage(SettingsActivity.this, language, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("token_status").equalsIgnoreCase("1")) {
                    if (response.getString("status").equalsIgnoreCase("1")) {
                        session.setLanguage(finalLanguage);
                        //PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit().putString(Session.DEFAULT_LANGUAGE, finalLanguage).commit();
                    } else {
                        CommonMethods.toast(SettingsActivity.this, response.getString("message"));
                    }
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
