package com.sample.honeybuser.MapIntegration;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by IM028 on 6/16/16.
 */
public class MapUtils {
    public  void animateToMeters(int meters1, Context context, GoogleMap googleMap){
        int MARKER_BOUNDS=0;
        double meters=meters1/1.8;
        int mapHeightInDP = 200;
        Resources r = context.getResources();
        int mapSideInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mapHeightInDP, r.getDisplayMetrics());

        LatLngBounds latLngBounds = calculateBounds(googleMap.getCameraPosition().target, meters);
        if(latLngBounds != null){
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, mapSideInPixels, mapSideInPixels, MARKER_BOUNDS);
            if(googleMap != null)
                googleMap.animateCamera(cameraUpdate);
        }
    }

    private LatLngBounds calculateBounds(LatLng center, double radius) {
        return new LatLngBounds.Builder().
                include(SphericalUtil.computeOffset(center, radius, 0)).
                include(SphericalUtil.computeOffset(center, radius, 90)).
                include(SphericalUtil.computeOffset(center, radius, 180)).
                include(SphericalUtil.computeOffset(center, radius, 270)).build();
    }

    public static int calculateZoomLevel(Context context, int distance) {
        int screenWidth = getScreenResolution(context);
        double equatorLength = 40075004; // in meters
        double widthInPixels = screenWidth;
        double metersPerPixel = equatorLength / 256;
        int zoomLevel = 1;
        while ((metersPerPixel * widthInPixels) > distance) {
            metersPerPixel /= 2;
            ++zoomLevel;
        }
        Log.i("Zoom", "zoom level = "+zoomLevel);
        return zoomLevel;
    }

    private static int getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        return width;
    }

    public static int distanceInMeter(float distanceKm) {
        return (int) distanceKm * 1000;
    }

}
