package com.sample.honeybuser.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sample.honeybuser.CommonActionBar.NavigationBarActivity;
import com.sample.honeybuser.EnumClass.IntentClasses;
import com.sample.honeybuser.InterFaceClass.DialogBoxInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_settings);
        setTitle("Settings");
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
}
