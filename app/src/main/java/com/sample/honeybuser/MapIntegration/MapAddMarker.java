package com.sample.honeybuser.MapIntegration;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sample.honeybuser.InterFaceClass.VolleyResponseImageListener;
import com.sample.honeybuser.Models.Vendor;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.WebServices.VolleyClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IM028 on 5/13/16.
 */
public class MapAddMarker {
    private String TAG = "MapAddMarker";
    private Activity activity;
    private GoogleMap googleMap;
    private List<Marker> markerList = new ArrayList<Marker>();
    private Circle circle;
//    private VolleyClass volleyClass;
    private MapUtils mapUtils;
    private Bitmap locateBitMap;
    private int size;

    public MapAddMarker(Activity activity, GoogleMap googleMap) {
        this.activity = activity;
        this.googleMap = googleMap;
        mapUtils = new MapUtils();
//        volleyClass = new VolleyClass(activity, TAG);
        circle = createCircle(googleMap.getCameraPosition().target);
        size = (int) activity.getResources().getDimension(R.dimen.vendor_map_icon);
//        locateBitMap = getResizedBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.map_locatepointer), size, size);
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void removeAll() {
        for (Marker marker : markerList) {
            marker.remove();
        }
        markerList.clear();
    }

    public void addAllMarkLocationMap(List<Vendor> listVendor) {
        removeAll();

        for (int i = 0; i < listVendor.size(); i++) {
            addMapMark(listVendor.get(i));
        }
    }

    public void moveCircle(LatLng latLng) {
        circle.setCenter(latLng);
    }

    public void radiCircle(double size) {
        circle.setRadius(size);
    }

    public void changeZoomLevel(double size) {
        mapUtils.animateToMeters((int) size, activity, googleMap);
    }

    private Circle createCircle(LatLng latLng) {
        changeZoomLevel(500);
        return googleMap.addCircle(new CircleOptions()
                .center(googleMap.getCameraPosition().target)
                .radius(500)
                .fillColor(activity.getResources().getColor(R.color.map_circle))
                .strokeWidth(0));

    }

    private void addMapMark(final Vendor vendor) {
        new VolleyClass(activity,TAG).ImageDownload(vendor.getIcon(), new VolleyResponseImageListener() {
            @Override
            public void onResponse(Bitmap response) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(Double.parseDouble(vendor.getLatitude()), Double.parseDouble(vendor.getLongitude())));
                markerOptions.title(vendor.getName() + " " + vendor.getDistance());
                markerOptions.draggable(false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getResizedBitmap(response,size,size)));
                Marker marker = googleMap.addMarker(markerOptions);
                markerList.add(marker);
                vendor.setMarkerId(marker.getId());
                if (!response.isRecycled()){
                    response.recycle();
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }
}
