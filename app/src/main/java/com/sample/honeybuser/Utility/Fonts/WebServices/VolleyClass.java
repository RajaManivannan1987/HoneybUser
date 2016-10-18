package com.sample.honeybuser.Utility.Fonts.WebServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.InterFaceClass.VolleyResponseImageListener;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;

import org.json.JSONException;
import org.json.JSONObject;

public class VolleyClass {
    private Context act;
    private String TAG = "";
    String networkErrorMessage = "Network error – please try again.";
    String poorNetwork = "Your data connection is too slow – please try again when you have a better network connection";
    String timeout = "Response timed out – please try again.";
    String authorizationFailed = "Authorization failed – please try again.";
    String serverNotResponding = "Server not responding – please try again.";
    String parseError = "Data not received from server – please try again.";

    String networkErrorTitle = "Network error";
    String poorNetworkTitle = "Poor Network Connection";
    String timeoutTitle = "Network Error";
    String authorizationFailedTitle = "Network Error";
    String serverNotRespondingTitle = "Server Error";
    String parseErrorTitle = "Network Error";
    //RequestQueue queue;

    public VolleyClass(Context context, String TAG) {
       this.act = context;
        //if (queue == null) {
         //   queue = Volley.newRequestQueue(context);
       // }
        this.TAG = TAG + " WebService";
    }

    public void volleyPostData(Context context,final String url, JSONObject jsonObject, final VolleyResponseListerner listener) {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        Log.d(TAG, "volleyPostData request url - " + url);
        Log.d(TAG, "volleyPostData request data - " + jsonObject.toString());
        if (isOnline()) {
            try {
                pDialog.show();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "volleyPostData response - " + response.toString());
                            try {
                                listener.onResponse(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                pDialog.dismiss();
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        pDialog.dismiss();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                    if (error instanceof TimeoutError) {
                        listener.onError(timeout, timeoutTitle);
                    } else if (error instanceof NoConnectionError) {
                        listener.onError(poorNetwork, poorNetworkTitle);
                    } else if (error instanceof AuthFailureError) {
                        listener.onError(authorizationFailed, authorizationFailedTitle);
                    } else if (error instanceof ServerError) {
                        listener.onError(serverNotResponding, serverNotRespondingTitle);
                    } else if (error instanceof NetworkError) {
                        listener.onError(networkErrorMessage, networkErrorTitle);
                    } else if (error instanceof ParseError) {
                        listener.onError(parseError, parseErrorTitle);
                    }
                }
            });
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.getInstance().addToRequestQueue(jsonObjReq);
            // queue.add(jsonObjReq);
        } else {
            Log.d(TAG, "volleyPostData response - No Internet");
            listener.onError(networkErrorMessage, networkErrorMessage);
        }
    }

    public void volleyPostDataProgressionTitle(final String url, JSONObject jsonObject, String progression, final VolleyResponseListerner listener) {
        final ProgressDialog pDialog = new ProgressDialog(act);
        pDialog.setMessage(progression);
        pDialog.setCancelable(false);
        Log.d(TAG, "volleyPostDataProgressionTitle request url - " + url);
        Log.d(TAG, "volleyPostDataProgressionTitle request data - " + jsonObject.toString());
        if (isOnline()) {
            try {
                pDialog.show();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "volleyPostDataProgressionTitle response - " + response.toString());
                            try {
                                listener.onResponse(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                pDialog.dismiss();
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        pDialog.dismiss();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                    if (error instanceof TimeoutError) {
                        listener.onError(timeout, timeoutTitle);
                    } else if (error instanceof NoConnectionError) {
                        listener.onError(poorNetwork, poorNetworkTitle);
                    } else if (error instanceof AuthFailureError) {
                        listener.onError(authorizationFailed, authorizationFailedTitle);
                    } else if (error instanceof ServerError) {
                        listener.onError(serverNotResponding, serverNotRespondingTitle);
                    } else if (error instanceof NetworkError) {
                        listener.onError(networkErrorMessage, networkErrorTitle);
                    } else if (error instanceof ParseError) {
                        listener.onError(parseError, parseErrorTitle);
                    }
                }
            });
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.getInstance().addToRequestQueue(jsonObjReq);
//            queue.add(jsonObjReq);
        } else {
            Log.d(TAG, "volleyPostDataProgressionTitle response - No Internet");
            listener.onError(networkErrorMessage, networkErrorMessage);
        }
    }

    public void volleyPostDataNoProgression(final String url, JSONObject jsonObject, final VolleyResponseListerner listener) {


        Log.d(TAG, "volleyPostDataNoProgression request url - " + url);
        Log.d(TAG, "volleyPostDataNoProgression request data - " + jsonObject.toString());
        if (isOnline()) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "volleyPostDataNoProgression response - " + response.toString());
                            try {
                                listener.onResponse(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError) {
                                listener.onError(timeout, timeoutTitle);
                            } else if (error instanceof NoConnectionError) {
                                listener.onError(poorNetwork, poorNetworkTitle);
                            } else if (error instanceof AuthFailureError) {
                                listener.onError(authorizationFailed, authorizationFailedTitle);
                            } else if (error instanceof ServerError) {
                                listener.onError(serverNotResponding, serverNotRespondingTitle);
                            } else if (error instanceof NetworkError) {
                                listener.onError(networkErrorMessage, networkErrorTitle);
                            } else if (error instanceof ParseError) {
                                listener.onError(parseError, parseErrorTitle);
                            }
                        }
                    });
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.getInstance().addToRequestQueue(jsonObjReq);
//            queue.add(jsonObjReq);
        } else {
            Log.d(TAG, "volleyPostDataNoProgression response - No Internet");
            listener.onError(networkErrorMessage, networkErrorMessage);
        }
    }

    public void ImageDownload(String url, final VolleyResponseImageListener listener) {

        if (isOnline()) {
            ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    listener.onResponse(response);
                    if (!response.isRecycled()) {
                        response.recycle();
                    }
                }
            },
                    0,
                    0,
                    null,
                    null,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError) {
                                listener.onError(timeout, timeoutTitle);
                            } else if (error instanceof NoConnectionError) {
                                listener.onError(poorNetwork, poorNetworkTitle);
                            } else if (error instanceof AuthFailureError) {
                                listener.onError(authorizationFailed, authorizationFailedTitle);
                            } else if (error instanceof ServerError) {
                                listener.onError(serverNotResponding, serverNotRespondingTitle);
                            } else if (error instanceof NetworkError) {
                                listener.onError(networkErrorMessage, networkErrorTitle);
                            } else if (error instanceof ParseError) {
                                listener.onError(parseError, parseErrorTitle);
                            }
                        }
                    }) {
                @Override
                public Priority getPriority() {
                    return Priority.IMMEDIATE;
                }
            };
            int MY_SOCKET_TIMEOUT_MS = 30000;
            imageRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.getInstance().addToRequestQueue(imageRequest);
           // queue.add(imageRequest);
        } else {
            listener.onError(networkErrorMessage, networkErrorMessage);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
