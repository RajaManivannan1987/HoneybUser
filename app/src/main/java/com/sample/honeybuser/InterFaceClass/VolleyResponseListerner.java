package com.sample.honeybuser.InterFaceClass;


import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyResponseListerner {

    void onResponse(JSONObject response) throws JSONException;

    void onError(String message, String title);
}
