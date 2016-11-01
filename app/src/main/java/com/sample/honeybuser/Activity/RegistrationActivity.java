package com.sample.honeybuser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sample.honeybuser.EnumClass.IntentClasses;
import com.sample.honeybuser.GCMClasses.RegistrationIntentService;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.Sharedpreferences.Session;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 10/5/2016.
 */

public class RegistrationActivity extends AppCompatActivity {
    private String TAG = "RegistrationActivity";
    private RadioButton ValueRadioButton;
    private RadioGroup radioGroup;
    private String language = "en";
    private EditText nameEditText, phoneNumEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        phoneNumEditText = (EditText) findViewById(R.id.phoneNumEditText);
        findViewById(R.id.registerTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.commonIntent(RegistrationActivity.this, IntentClasses.LOGIN);
            }
        });
        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameEditText.getText().toString().equals("")) {
                    nameEditText.setError(null);
                    if (!phoneNumEditText.getText().toString().equals("")) {
                        phoneNumEditText.setError(null);
                        if (phoneNumEditText.getText().toString().length() == 10) {
                            phoneNumEditText.setError(null);
                            GetResponseFromServer.getWebService(RegistrationActivity.this, TAG).register(RegistrationActivity.this, nameEditText.getText().toString(), phoneNumEditText.getText().toString(), language, new VolleyResponseListerner() {
                                @Override
                                public void onResponse(JSONObject response) throws JSONException {
                                    if (response.getString("status").equalsIgnoreCase("1")) {
                                        nameEditText.setText("");
                                        phoneNumEditText.setText("");
                                        JSONObject object = response.getJSONObject("data");

//                                    Session.getSession(RegistrationActivity.this, TAG).createSession(response.getJSONObject("data"));
                                        startService(new Intent(RegistrationActivity.this, RegistrationIntentService.class));
                                        startActivity(new Intent(RegistrationActivity.this, OTPActivity.class).putExtra("otp_type", "register").putExtra("response", object.getString("user_id")));
                                        finish();
// CommonMethods.commonIntent(RegistrationActivity.this, IntentClasses.OTP);
                                    } else {
                                        CommonMethods.toast(RegistrationActivity.this, response.getString("message"));
                                    }
                                }

                                @Override
                                public void onError(String message, String title) {

                                }
                            });
                        } else {
                            phoneNumEditText.setError("Enter 10 digits valid phone number");
                            phoneNumEditText.requestFocus();
                        }
                    } else {
                        phoneNumEditText.setError("Enter phone number");
                        phoneNumEditText.requestFocus();
                    }
                } else {
                    nameEditText.setError("Enter name");
                    nameEditText.requestFocus();
                }
            }
        });
    }

    public void onRadioButtonClicked(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.englishRadioButton:
                if (checked) {
                    int SelectedButtonId = radioGroup.getCheckedRadioButtonId();
                    ValueRadioButton = (RadioButton) findViewById(SelectedButtonId);
                    language = "en";
                }
                break;
            case R.id.tamilRadioButton:
                if (checked) {
                    int SelectedButtonId = radioGroup.getCheckedRadioButtonId();
                    ValueRadioButton = (RadioButton) findViewById(SelectedButtonId);
                    language = "ta";
                }
                break;

        }
    }
}
