package com.sample.honeybuser.GCMClasses;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.Sharedpreferences.Session;

import org.json.JSONObject;

/**
 * Created by IM028 on 4/20/16.
 */

public class GCMListener extends GcmListenerService {

    private static final String TAG = "GCMListener";
    private String receiverUUID = "";


    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
         Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.bell);
         Uri soundUriDefault = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.defaulttone);

        String message = data.getString("message");
        String type = data.getString("topic", "message");
        String id = data.getString("id", "");
        String priority = data.getString("priority", "N");
        String channelUrl = "";
        String senderName = "";
        receiverUUID = "";
        if (type.equalsIgnoreCase("message")) {
            try {
                JSONObject sendbird = new JSONObject(data.get("sendbird").toString());
                channelUrl = sendbird.getJSONObject("channel").getString("channel_url");
                receiverUUID = sendbird.getJSONObject("recipient").getString("id");
                senderName = sendbird.getJSONObject("sender").getString("name");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "type: " + type);
        Log.d(TAG, "channelUrl: " + channelUrl);
        Log.d(TAG, "receiverUUID: " + receiverUUID);
        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        if (new Session(this, "").isLogin()) {

            sendNotification(message, type, id, channelUrl, (priority.equalsIgnoreCase("Y")?soundUri:soundUriDefault));
            // By raja
            /*if (type.equalsIgnoreCase("message")) {
                if (receiverUUID.equalsIgnoreCase(new Session(this, "").getUUID())) {
                    sendNotification(ConstantFunction.boldForSenderName(senderName, message), type, id, channelUrl, (priority.equalsIgnoreCase("Y")?soundUri:soundUriDefault));
                }
            } else
                sendNotification(message, type, id, channelUrl, (priority.equalsIgnoreCase("Y")?soundUri:soundUriDefault));*/
            // [END_EXCLUDE]
        }
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */

    private void sendNotification(CharSequence message, String type, String id, String channelUrl, Uri soundUri) {
        //  By raja
        /*Intent intent = new Intent(this, LocationCheckActivity.class).putExtra(ConstantValues.notificationType, type).putExtra(ConstantValues.vendorId, id).putExtra(ConstantValues.channelUrl, channelUrl);
        Log.d(TAG, "Notification Type - " + type);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) Calendar.getInstance().getTimeInMillis(), intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_media_route_on_0_mono_dark)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)

                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) Calendar.getInstance().getTimeInMillis(), notificationBuilder.build());*/
    }
}
