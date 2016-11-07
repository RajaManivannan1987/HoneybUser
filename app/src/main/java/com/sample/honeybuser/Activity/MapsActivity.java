package com.sample.honeybuser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sample.honeybuser.CommonActionBar.CommonActionBar;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.CommonWebserviceMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;

public class MapsActivity extends CommonActionBar implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String vendorId;
    private ImageView vendorLocationActivityDirectionImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_maps);
        setTitle(getIntent().getExtras().getString(ConstandValue.vendorName, ""));
        vendorLocationActivityDirectionImageView = (ImageView) findViewById(R.id.vendorLocationActivityDirectionImageView);

        vendorId = getIntent().getExtras().getString(ConstandValue.vendorId, "");

        if (getIntent().getExtras().getString(ConstandValue.IS_ONLINE, "").equalsIgnoreCase("Y")) {
            setNotification("Y");
        } else {
            setNotification("N");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        vendorLocationActivityDirectionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.locationDirection(MapsActivity.this, String.valueOf(getIntent().getExtras().getDouble(ConstandValue.latitude)), String.valueOf(getIntent().getExtras().getDouble(ConstandValue.longitude)));
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        String lat = String.valueOf(getIntent().getExtras().getString(ConstandValue.latitude));
        String lang = String.valueOf(getIntent().getExtras().getString(ConstandValue.longitude));
        Log.d("MapActivity", lat + " " + lang);
        LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
        mMap.addMarker(new MarkerOptions().position(sydney).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                startActivity(new Intent(MapsActivity.this, VendorDetailActivity.class).putExtra("vendor_id", vendorId).putExtra("notificationType", ""));
                MapsActivity.this.finish();
                return true;
            }
        });
    }
}
