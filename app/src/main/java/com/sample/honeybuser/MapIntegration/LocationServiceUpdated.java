package com.sample.honeybuser.MapIntegration;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.sample.honeybuser.InterFaceClass.TimerInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Utility.Fonts.ScheduleThread;
import com.sample.honeybuser.Utility.Fonts.Sharedpreferences.Session;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IM028 on 4/20/16.
 */
public class LocationServiceUpdated extends Service {

    int mStartMode; // indicates how to behave if the service is killed
    IBinder mBinder; // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used
    private String TAG = "LocationService";
    public static final String BROADCAST_ACTION = "Hello World";
    private static int TEN_MINUTES;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    private Session prefManager;
    private GetResponseFromServer webServices;
    private Location location;
    static double lat;
    static double lang;
    Intent intent;
    int counter = 0;
    ScheduleThread scheduleThread;
    TimerInterface timerInterface;
    Timer timer;
    TimerTask timerTask;

    @Override
    public void onCreate() {
        super.onCreate();
        prefManager = new Session(this, TAG);
        webServices = new GetResponseFromServer(this, TAG);
        getPingTime();
        timer = new Timer();
        intent = new Intent(BROADCAST_ACTION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onCreate");
        return startId;
    }


    private void getPingTime() {
        webServices.pingTime(this, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                JSONObject jsonObject = response.getJSONObject("data");
                int second = Integer.parseInt(jsonObject.getString("ping_interval"));
                TEN_MINUTES = 1000 * second;
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Log.d(TAG, location + "");
                        if (location != null) {

//                            By Raja
                          sendLocation();
                        }
                    }
                };
                try {
                    timer.schedule(timerTask, 01, TEN_MINUTES);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }

    @Override
    public boolean stopService(Intent name) {
        Log.d(TAG, "stopService");
        timer.cancel();
        timerTask.cancel();
        stopSelf();
        return super.stopService(name);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e(TAG, "ONSTART");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return mBinder;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TEN_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TEN_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        if (timer != null)
            timer.cancel();
        if (timerTask != null)
            timerTask.cancel();
        stopSelf();
        Log.v(TAG, "Location Stop");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager != null)
            locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(final Location loc) {
            location = loc;
            //Log.d("onLocationChanged", location + "");
            if (isBetterLocation(loc, previousBestLocation)) {
                lat = loc.getLatitude();
                lang = loc.getLongitude();
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                sendBroadcast(intent);
                //String url = ConstantValues.SAVE_LOCATION;

            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderDisabled(String provider) {
            //Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            //Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


    }

    private void sendLocation() {
        webServices.saveLocation(this, lat, lang, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    //Log.d("response", response.toString());
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    public static double getLat() {
        return lat;
    }

    public static double getLang() {
        return lang;
    }

}

