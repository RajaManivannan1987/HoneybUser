package com.sample.honeybuser.GCMClasses;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Utility.Fonts.Sharedpreferences.Session;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM028 on 4/20/16.
 */

public class RegistrationIntentService extends IntentService {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken("470333061598",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);
            if (new Session(this, TAG).isLogin()) {
                // By raja
                GetResponseFromServer.getWebService(this, TAG).saveDeviceId(this,token, new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {

                    }

                    @Override
                    public void onError(String message, String title) {

                    }
                });
            }
            MyApplication.deviceId = token;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putString("SendBirdGCMToken", token);
            e.commit();
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }


}
