package com.sample.honeybuser.Utility.Fonts.WebServices;

import android.content.Context;
import android.util.Log;

import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Utility.Fonts.Sharedpreferences.Session;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 10/4/2016.
 */

public class GetResponseFromServer {
    public static GetResponseFromServer webService = null;
    private String TAG;
    private VolleyClass volleyClass;
    private Session session;

    public static GetResponseFromServer getWebService(Context context, String Tag) {
        if (webService == null) {
            webService = new GetResponseFromServer(context, Tag);
        }
        return webService;
    }

    public GetResponseFromServer(Context context, String tag) {
        this.TAG = tag;
        volleyClass = new VolleyClass(context, TAG);
        session = new Session(context, TAG);
    }

    public void saveDeviceId(Context context, String deviceId, final VolleyResponseListerner listerner) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("device_id", deviceId);
            jsonObject.put("type_of_os", "android");
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse1(context, ConstandValue.SERVER_URL + "user/save_device_id", listerner, jsonObject);
    }

    public void login(Context context, String phoneNo, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("login_id", phoneNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setResponse(context, ConstandValue.SERVER_URL + "user/login_api", listerner, jsonObject);
    }

    public void register(Context context, String name, String phoneNo, String language, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("phone", phoneNo);
            jsonObject.put("default_language", language);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setResponse(context, ConstandValue.SERVER_URL + "user/register_api", listerner, jsonObject);
    }

    public void submitOtp(Context context, String otp, String type, String userid, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("otp", otp);
            jsonObject.put("user_id", userid);
            if (type.startsWith("register")) {
                jsonObject.put("otp_type", "register");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (type.startsWith("register")) {
            setResponse(context, ConstandValue.SERVER_URL + "user/check_otp", listerner, jsonObject);
        } else {
            setResponse(context, ConstandValue.SERVER_URL + "user/login_otp", listerner, jsonObject);
        }
    }

    public void getOnlineVendor(Context context, String latitude, String longitude, String type, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("os_type", "android");
            jsonObject.put("status", type);
            jsonObject.put("language", session.getLanguage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setResponse(context, ConstandValue.SERVER_URL + "vendor/list_api", listerner, jsonObject);
    }

    public void geOfflineVendor(Context context, String latitude, String longitude, String type, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("os_type", "android");
            jsonObject.put("type", type);
            jsonObject.put("language", session.getLanguage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setResponse1(context, ConstandValue.SERVER_URL + "vendor/list_api", listerner, jsonObject);
    }

    public void getFollowerList(Context context, String latitude, String longitude, String type, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("type", type);
            jsonObject.put("language", session.getLanguage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setResponse(context, ConstandValue.SERVER_URL + "user/follow_list_api", listerner, jsonObject);
    }

    public void addressSave(Context context, String title, String address, String latitude, String longitude, String distance, String newAddress, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("title", title);
            jsonObject.put("address", address);
            jsonObject.put("longitude", longitude);
            jsonObject.put("latitude", latitude);
            jsonObject.put("distance", distance);
            if (newAddress.equalsIgnoreCase("Y")) {
                jsonObject.put("new_address", "Y");
            } else {
                jsonObject.put("new_address", "N");
            }
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse(context, ConstandValue.SERVER_URL + "user/save_address", listerner, jsonObject);
    }

    public void addressDistanceList(Context context, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse(context, ConstandValue.SERVER_URL + "user/get_address", listerner, jsonObject);

    }

    public void pingTime(Context context, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        setResponse(context, ConstandValue.SERVER_URL + "user/ping_interval", listerner, jsonObject);
    }

    public void saveLocation(Context context, double latitude, double longitude, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("latitude", latitude + "");
            jsonObject.put("longitude", longitude + "");
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse1(context, ConstandValue.SERVER_URL + "user/location", listerner, jsonObject);
    }

    public void setFollow(Context context, String vendorId, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("vendor_id", vendorId);
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse(context, ConstandValue.SERVER_URL + "vendor/set_follow", listerner, jsonObject);

    }

    public void removeFollow(Context context, String vendorId, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("vendor_id", vendorId);
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse1(context, ConstandValue.SERVER_URL + "vendor/remove_follow", listerner, jsonObject);
    }

    public void setNotification(Context context, String vendorId, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("vendor_id", vendorId);
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse(context, ConstandValue.SERVER_URL + "vendor/set_notification_status", listerner, jsonObject);
    }

    public void removeNotification(Context context, String vendorId, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("vendor_id", vendorId);
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse1(context, ConstandValue.SERVER_URL + "vendor/remove_notification_status", listerner, jsonObject);
    }


    public void removeAddress(Context context, String addressId, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("address_id", addressId);
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse(context, ConstandValue.SERVER_URL + "user/delete_address", listerner, jsonObject);

    }


    public void getVendorDetail(Context context, String vendorId, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("vendor_id", vendorId);
            jsonObject.put("language", session.getLanguage());
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse(context, ConstandValue.SERVER_URL + "vendor/details_api", listerner, jsonObject);

    }

    public void getRatinds(Context context, String vendorId, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("vendor_id", vendorId);
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse1(context, ConstandValue.SERVER_URL + "vendor/ratings", listerner, jsonObject);
    }

    public void updataLanguage(Context context, String language, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("language", language);
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse1(context, ConstandValue.SERVER_URL + "user/update_language", listerner, jsonObject);
    }

    public void getVendorLocation(Context context, String vendorId, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("vendor_id", vendorId);
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse(context, ConstandValue.SERVER_URL + "vendor/get_location", listerner, jsonObject);
    }

    public void logout(Context context, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse1(context, ConstandValue.SERVER_URL + "user/logout", listerner, jsonObject);
    }

    public void getProductList(Context context, String latitude, String longitude, String distance, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("latitude", latitude + "");
            jsonObject.put("longitude", longitude + "");
            jsonObject.put("distance", distance);
            jsonObject.put("language", session.getLanguage());
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse(context, ConstandValue.SERVER_URL + "vendor/product_search", listerner, jsonObject);
    }

    public void getSearchVendorList(Context context, String latitude, String longitude, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("latitude", latitude + "");
            jsonObject.put("longitude", longitude + "");
            jsonObject.put("language", session.getLanguage());
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse1(context, ConstandValue.SERVER_URL + "vendor/search_api", listerner, jsonObject);
    }

    public void addRating(Context context, String vendorId, String rating, String review, VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("vendor_id", vendorId);
            jsonObject.put("star_rating", rating);
            jsonObject.put("review", review);
        } catch (JSONException e) {
            Log.e(TAG + " " + TAG, e.getMessage());
        }
        setResponse(context, ConstandValue.SERVER_URL + "vendor/add_rating", listerner, jsonObject);
    }

    public void getBusinessVendorList(Context context, String latitude, String longitude, String distance, String business_id, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", session.getUserId());
            jsonObject.put("api_key", session.getApiKey());
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("distance", distance);
            jsonObject.put("business_id", business_id);
            jsonObject.put("language", session.getLanguage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setResponse(context, ConstandValue.SERVER_URL + "vendor/business_vendors", listerner, jsonObject);
    }

    private void setResponse(Context context, String url, final VolleyResponseListerner listerner, JSONObject jsonObject) {
        volleyClass.volleyPostData(context, url, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    private void setResponse1(Context context, String url, final VolleyResponseListerner listerner, JSONObject jsonObject) {
        volleyClass.volleyPostDataNoProgression(url, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }


}
