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
import android.widget.Toast;

import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM028 on 4/20/16.
 */
public class LocationService extends Service {
    private String TAG = "LocationService";
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 1;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        intent = new Intent(BROADCAST_ACTION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e(TAG, "ONSTART");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
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
        Log.v("STOP_SERVICE", "DONE");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
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
            Log.i(TAG, "Location changed");
            if (isBetterLocation(loc, previousBestLocation)) {
                loc.getLatitude();
                loc.getLongitude();
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                sendBroadcast(intent);
                String url = ConstandValue.SERVER_URL + "user/save_address";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id", "1");
                    jsonObject.put("device_id", MyApplication.deviceId);
                    jsonObject.put("latitude", loc.getLatitude() + "");
                    jsonObject.put("longitude", loc.getLongitude() + "");
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
                // By raja
               /* new VolleyClass(LocationService.this, TAG).volleyPostDataNoProgression(url, jsonObject, new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {

                    }

                    @Override
                    public void onError(String message, String title) {

                    }
                });*/
            /*final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            String Text = "";
            try {
                List<ChangeAddress> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                Text = "My current location is: "+addresses.get(0).getAddressLine(0);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Text = "My current location is: " +"Latitude = " + loc.getLatitude() + ", Longitude = " + loc.getLongitude();
            }
            */
                //Toast.makeText( getApplicationContext(), "Location polled to server", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


    }

}

