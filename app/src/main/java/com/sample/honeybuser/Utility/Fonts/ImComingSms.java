package com.sample.honeybuser.Utility.Fonts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.sample.honeybuser.Activity.OTPActivity;
import com.sample.honeybuser.InterFaceClass.SmsListener;

/**
 * Created by Im033 on 12/16/2016.
 */

public class ImComingSms extends BroadcastReceiver {
    private static SmsListener smsListener;
    private String TAG = ImComingSms.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNumber = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();


                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + phoneNumber);
                   /* OTPActivity sms = new OTPActivity();
                    sms.receivedSms(message);*/
                    try {
                        if (senderNumber.equals("IM-WAYSMS")) {
                            smsListener.messageReceived(message);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public static void bindlistener(SmsListener listener) {
        smsListener = listener;
    }
}
