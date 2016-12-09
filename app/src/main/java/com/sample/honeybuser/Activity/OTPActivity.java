package com.sample.honeybuser.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sample.honeybuser.EnumClass.IntentClasses;
import com.sample.honeybuser.GCMClasses.RegistrationIntentService;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.Sharedpreferences.Session;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by IM0033 on 10/5/2016.
 */

public class OTPActivity extends AppCompatActivity {
    private String TAG = "OTPActivity";
    private EditText otpEditText;
    private String otpType = "login", userId, activity;
    public boolean languageFlag = false;
    private Button optSubmitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otpEditText = (EditText) findViewById(R.id.otpEditText);
        optSubmitButton = (Button) findViewById(R.id.optSubmitButton);
        if (getIntent().getExtras() != null) {
            otpType = getIntent().getExtras().getString("otp_type");
            userId = getIntent().getExtras().getString("response");
            activity = getIntent().getExtras().getString("activity");
        }
       /* optSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        findViewById(R.id.optSubmitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!otpEditText.getText().toString().equals("")) {
                    otpEditText.setError(null);
                    GetResponseFromServer.getWebService(OTPActivity.this, TAG).submitOtp(OTPActivity.this, otpEditText.getText().toString(), otpType, userId, new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            languageFlag = false;
//                            if (response.getString("token_status").equalsIgnoreCase("1")) {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                Session.getSession(OTPActivity.this, TAG).createSession(response.getJSONObject("data"));
                                startService(new Intent(OTPActivity.this, RegistrationIntentService.class));
                                if (activity.equalsIgnoreCase("Register")) {
                                    CommonMethods.commonIntent(OTPActivity.this, IntentClasses.HELPER);
                                    finish();
                                } else {
                                    CommonMethods.commonIntent(OTPActivity.this, IntentClasses.DASHBOARD);
                                    finish();
                                }

                            } else {
                                CommonMethods.toast(OTPActivity.this, response.getString("message"));
                            }
//                            }else {
                            // Session.getSession(OTPActivity.this,TAG).clearSession();
//                                CommonMethods.commonIntent(OTPActivity.this,IntentClasses.LOGIN);
//                            }


                        }

                        @Override
                        public void onError(String message, String title) {

                        }
                    });

                } else {
                    otpEditText.setText("Enter OPT");
                    otpEditText.requestFocus();
                }
//                CommonMethods.commonIntent(OTPActivity.this, IntentClasses.DASHBOARD);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
