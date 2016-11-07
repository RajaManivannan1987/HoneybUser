package com.sample.honeybuser.Utility.Fonts.WebServices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.sample.honeybuser.Activity.MapsActivity;
import com.sample.honeybuser.Activity.VendorDetailActivity;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Singleton.ActionCompletedSingleton;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Raja M on 10/15/2016.
 */

public class CommonWebserviceMethods extends AppCompatActivity {
    Context context;
    String TAG = "";

   /* public CommonWebserviceMethods(Context context, String tag) {
        this.context = context;
        this.TAG = tag;
    }*/

    public static void getVendorLocation(final Activity context, String tag, final String vendor_Id, final String name) {
        GetResponseFromServer.getWebService(context, tag).getVendorLocation(context, vendor_Id, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    context.startActivity(new Intent(context, MapsActivity.class).putExtra(ConstandValue.latitude, jsonObject.getString("latitude")).putExtra(ConstandValue.longitude, jsonObject.getString("longitude")).putExtra(ConstandValue.vendorName, name).putExtra(ConstandValue.IS_ONLINE, jsonObject.getString("is_available")).putExtra("vendor_id", vendor_Id));
//                    CommonMethods.locationDirection(context, jsonObject.getString("latitude"), jsonObject.getString("longitude"));
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    public static void setFollows(final Activity context, String tag, String vendor_Id, final String type) {
        GetResponseFromServer.getWebService(context, tag).setFollow(context, vendor_Id, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    if (type.equalsIgnoreCase("0")) {
                        Complete.getInstance().orderCompleted();
                    } else if (type.equalsIgnoreCase("1")) {
                        Complete.offerDialogInstance().orderCompleted();
                    } else if (type.equalsIgnoreCase("3")) {
                        ActionCompletedSingleton.getActionCompletedSingleton().ActionCompleted();
                    } else if (type.equalsIgnoreCase("4")) {
                        Complete.getVendorSearch().orderCompleted();
                    } else if (type.equalsIgnoreCase("5")) {
                        Complete.getBusinessList().orderCompleted();
                    } else if (type.equalsIgnoreCase("6")) {
                        Complete.getGetMapList().orderCompleted();
                    } else {
                        Complete.ratingDialogInstance().orderCompleted();

                    }

                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    public static void removeFollows(final Activity context, String tag, String vendor_Id, final String type) {
        GetResponseFromServer.getWebService(context, tag).removeFollow(context, vendor_Id, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    if (type.equalsIgnoreCase("0")) {
                        Complete.getInstance().orderCompleted();
                    } else if (type.equalsIgnoreCase("1")) {
                        Complete.offerDialogInstance().orderCompleted();
                    } else if (type.equalsIgnoreCase("3")) {
                        ActionCompletedSingleton.getActionCompletedSingleton().ActionCompleted();
                    } else if (type.equalsIgnoreCase("4")) {
                        Complete.getVendorSearch().orderCompleted();
                    } else if (type.equalsIgnoreCase("5")) {
                        Complete.getBusinessList().orderCompleted();
                    } else if (type.equalsIgnoreCase("6")) {
                        Complete.getGetMapList().orderCompleted();
                    } else {
                        Complete.ratingDialogInstance().orderCompleted();
                    }
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    public static void removeNtification(Activity context, String tag, String vendor_Id) {
        GetResponseFromServer.getWebService(context, tag).removeNotification(context, vendor_Id, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    ActionCompletedSingleton.getActionCompletedSingleton().ActionCompleted();
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    public static void setNtification(final Activity context, String tag, String vendor_Id) {
        GetResponseFromServer.getWebService(context, tag).setNotification(context, vendor_Id, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    ActionCompletedSingleton.getActionCompletedSingleton().ActionCompleted();
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

}
