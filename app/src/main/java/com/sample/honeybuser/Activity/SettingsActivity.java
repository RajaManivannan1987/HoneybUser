package com.sample.honeybuser.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.sample.honeybuser.Adapter.DistanceSelectRecyclerViewAdapter;
import com.sample.honeybuser.CommonActionBar.NavigationBarActivity;
import com.sample.honeybuser.EnumClass.IntentClasses;
import com.sample.honeybuser.EnumClass.Selected;
import com.sample.honeybuser.InterFaceClass.DialogBoxInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.MapIntegration.LocationServiceUpdated;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.AlertDialogManager;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.Sharedpreferences.Session;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Im033 on 10/7/2016.
 */

public class SettingsActivity extends NavigationBarActivity {
    private String TAG = "SettingsActivity";
    private static final String PREF_NAME = "VVNear";
    public String DEFAULT_LANGUAGE = "default_language";
    private Switch languageSwitch;
    private LinearLayout aniLayout;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    Session session;
    private RecyclerView distanceRecyclerView;
    private TextView animation1;
    private DistanceSelectRecyclerViewAdapter distanceAdapter;
    private List<String> distanceList = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_settings);
        setTitle("Settings");
        setSelected(Selected.SETTINGS);
        pref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        session = new Session(SettingsActivity.this, TAG);
        languageSwitch = (Switch) findViewById(R.id.languageSwitch);

        animation1 = (TextView) findViewById(R.id.animation);
        aniLayout = (LinearLayout) findViewById(R.id.aniLayout);
        distanceRecyclerView = (RecyclerView) findViewById(R.id.settingsChangeDistanceRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        distanceRecyclerView.setLayoutManager(layoutManager);

        distanceAdapter = new DistanceSelectRecyclerViewAdapter(this, distanceList, "Settings");
        distanceRecyclerView.setAdapter(distanceAdapter);


      /*  animation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(
                        getApplicationContext(), R.anim.slide_in_up);
                animation.setDuration(1000);
                aniLayout.setAnimation(animation);
                animation.start();
            }
        });*/

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
                AlertDialogManager.listenerDialogBox(SettingsActivity.this, "", "Are you sure want to logout?", new DialogBoxInterface() {
                    @Override
                    public void yes() {
                        GetResponseFromServer.getWebService(SettingsActivity.this, TAG).logout(SettingsActivity.this, new VolleyResponseListerner() {
                            @Override
                            public void onResponse(JSONObject response) throws JSONException {
                                if (response.getString("status").equalsIgnoreCase("1")) {
                                    Session.getSession(SettingsActivity.this, TAG).clearSession();
                                    stopService(new Intent(SettingsActivity.this, LocationServiceUpdated.class));
                                    startActivity(new Intent(SettingsActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                                    CommonMethods.commonIntent(SettingsActivity.this, IntentClasses.LOGIN);
//                                    finish();
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
                        editor.putString(DEFAULT_LANGUAGE, finalLanguage);
                        editor.commit();
                        CommonMethods.toast(SettingsActivity.this, response.getString("message"));
                        Complete.offerDialogInstance().orderCompleted();
                        Complete.getVendorSearch().orderCompleted();
//                        session.setLanguage(finalLanguage);
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

    @Override
    protected void onResume() {
        super.onResume();
        getSettings();
    }

    private void getSettings() {
        GetResponseFromServer.getWebService(SettingsActivity.this, TAG).getSettings(SettingsActivity.this, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                distanceList.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {

                    for (int i = 0; i < response.getJSONObject("data").getJSONArray("notification_range").length(); i++) {
                        distanceList.add(response.getJSONObject("data").getJSONArray("notification_range").getJSONObject(i).getString("range"));
                        if (response.getJSONObject("data").getJSONArray("notification_range").getJSONObject(i).getString("selected").equalsIgnoreCase("Y")) {
                            distanceAdapter.selectPosition(i);
                        }
                    }
                    distanceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }
}
