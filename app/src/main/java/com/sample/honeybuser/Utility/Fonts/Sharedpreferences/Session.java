package com.sample.honeybuser.Utility.Fonts.Sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM028 on 5/12/16.
 */
public class Session {
    private static Session session = null;
    private String TAG = "Session";
    private static final String PREF_NAME = "VVNear";
    private static final String IS_LOGIN = "IsLoggedIn";

    private final String USER_ID = "user_id";
    private final String NAME = "name";
    private final String EMAIL = "email";
    private final String PHONE = "phone";
    private static final String STATUS = "status";
    private static final String MOBILE_VERIFIED = "mobile_no_verified";


    private static String language;
    //private static final String UUID = "uuid";
    // private static final String SEND_BIRD_USER_ID = "sendbird_user_id";
    // private static final String SEND_BIRD_PROFILE_PICTURE = "sendbird_profile_picture";
    private final String API_KEY = "api_key";
    public String DEFAULT_LANGUAGE = "default_language";
    // private static final String SEND_BIRD_API = "sendbird_api";
    //private static final String OTP_TYPE = "otp_type";
    //private static final String REFERRAL_CODE = "referral_code";


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    Context context;

    public static Session getSession(Context context, String tag) {
        if (session == null) {
            session = new Session(context, tag);
        }
        return session;
    }

    public Session(Context context, String TAG) {
        this.context = context;
        this.TAG = "Session " + TAG;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createSession(JSONObject logincheck) {
        Log.d(TAG, logincheck.toString());
        try {
            editor.putBoolean(IS_LOGIN, true);
            editor.putString(USER_ID, logincheck.getString(USER_ID));
            editor.putString(NAME, logincheck.getString(NAME));
            editor.putString(EMAIL, logincheck.getString(EMAIL));
            editor.putString(PHONE, logincheck.getString(PHONE));
            editor.putString(MOBILE_VERIFIED, logincheck.getString(MOBILE_VERIFIED));
            editor.putString(API_KEY, logincheck.getString(API_KEY));
            editor.putString(DEFAULT_LANGUAGE, logincheck.getString(DEFAULT_LANGUAGE));
            setLanguage(logincheck.getString(DEFAULT_LANGUAGE));
            editor.commit();
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static String getLanguage() {
        return language;
    }

    public static void setLanguage(String language) {
        Session.language = language;
    }

    public boolean isLogin() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getUserId() {
        return pref.getString(USER_ID, "0");
    }

    public String getName() {
        return pref.getString(NAME, "");
    }

    public String getEmail() {
        return pref.getString(EMAIL, "");
    }

    public String getPhone() {
        return pref.getString(PHONE, "");
    }

    public String getMobileVerified() {
        return pref.getString(MOBILE_VERIFIED, "");
    }

    public String getApiKey() {
        return pref.getString(API_KEY, "");
    }


//    public String getUUID() {
//        return pref.getString(UUID, "");
//    }

//    public String getOtp() {
//        return pref.getString(OTP_TYPE, "");
//    }


//    public String getReferralCode() {
//        return pref.getString(REFERRAL_CODE, "");
//    }


    public String getDefaultLanguage() {
        return pref.getString(DEFAULT_LANGUAGE, "");
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }
}
