package com.sample.honeybuser.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.sample.honeybuser.EnumClass.IntentClasses;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by IM0033 on 10/3/2016.
 */

public class LoginActivity extends AppCompatActivity {
    private Activity activity = LoginActivity.this;
    private String TAG = "LoginActivity";
    private EditText phoneNoEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        phoneNoEditText = (EditText) findViewById(R.id.phoneNoEditText);

        findViewById(R.id.registerTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.commonIntent(activity, IntentClasses.REGISTRATION);
            }
        });
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!phoneNoEditText.getText().toString().equals("")) {
                    phoneNoEditText.setError(null);
                    if (phoneNoEditText.getText().toString().length() == 10) {
                        phoneNoEditText.setError(null);
                        String phono = phoneNoEditText.getText().toString();
                        GetResponseFromServer.getWebService(LoginActivity.this, TAG).login(LoginActivity.this, phono, new VolleyResponseListerner() {
                            @Override
                            public void onResponse(JSONObject response) throws JSONException {
                                if (response.getString("status").equalsIgnoreCase("1")) {
                                    phoneNoEditText.setText("");
                                    JSONObject object = response.getJSONObject("data");
                                    startActivity(new Intent(LoginActivity.this, OTPActivity.class).putExtra("otp_type", "login").putExtra("response", object.getString("user_id")).putExtra("activity","Login"));
                                    finish();
// CommonMethods.commonIntent(activity, IntentClasses.OTP);
                                } else {
                                    CommonMethods.toast(LoginActivity.this, response.getString("message"));
                                }
                            }

                            @Override
                            public void onError(String message, String title) {

                            }
                        });
                    } else {
                        phoneNoEditText.setError("Enter 10 digits valid phone number");
                        phoneNoEditText.requestFocus();
                    }
                } else {
                    phoneNoEditText.setError("Enter phone no");
                    phoneNoEditText.requestFocus();
                }

            }
        });
        findViewById(R.id.phoneNoEditText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
