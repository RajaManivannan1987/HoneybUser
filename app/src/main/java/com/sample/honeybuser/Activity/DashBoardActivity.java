package com.sample.honeybuser.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.sample.honeybuser.Adapter.DashBoardViewPagerAdapter;
import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.CommonActionBar.NavigationBarActivity;
import com.sample.honeybuser.EnumClass.FragmentType;
import com.sample.honeybuser.EnumClass.Selected;
import com.sample.honeybuser.InterFaceClass.ChangeLocationListener;
import com.sample.honeybuser.InterFaceClass.TouchInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.MapIntegration.MapAddMarker;
import com.sample.honeybuser.MapIntegration.MapUtils;
import com.sample.honeybuser.MapIntegration.TouchableWrapper;
import com.sample.honeybuser.Models.Vendor;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by IM0033 on 10/5/2016.
 */

public class DashBoardActivity extends NavigationBarActivity implements TabLayout.OnTabSelectedListener {
    private String TAG = "DashBoardActivity";
    private GoogleMap googleMap;
    private MapAddMarker mapAddMarker;

    private TabLayout tabLayout;
    private ViewPager dashBoardViewPager;
    private FragmentType fragmentType = FragmentType.ONLINE;
    private TabLayout.Tab onlineTab, offlineTab;
    private ImageView mapChangeIcon;
    private List<Vendor> listVendor = new ArrayList<Vendor>();
    private LatLng location = new LatLng(0d, 0d);
    String adres = "";
    public static LatLng distanceLatLng;
    public static String locationName;
    private static String lat, lang;
    private boolean mapboolean = false;
    private boolean flagIsOnTouched = true;
    private boolean isMove = false;
    private FrameLayout mapFrameLayout;
    private TouchableWrapper touchFrameLayout;
    private SupportMapFragment mapView;


    public void setFragmentType(FragmentType fragmentType) {
        this.fragmentType = fragmentType;
    }

    private void onLine() {
        //getVendorLocation();
        setFragmentType(FragmentType.ONLINE);
        mapAddMarker.removeAll();
        listVendor.clear();
    }

    private void offLine() {
        //getVendorLocation();
        setFragmentType(FragmentType.OFFLINE);
        mapAddMarker.removeAll();
        listVendor.clear();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_dashboard);
        //setSelectTab("dashboard");
        setSelected(Selected.DASHBOARD);
        //enableMyLocation();
        tabLayout = (TabLayout) findViewById(R.id.dashboardMapActivityTabLayout);
        //tabLayout.setSelectedTabIndicatorHeight(0);
        mapFrameLayout = (FrameLayout) findViewById(R.id.mobileStoreFragmentLocationMapFrameLayout);
        touchFrameLayout = (TouchableWrapper) findViewById(R.id.mobileStoreFragmentTouchableWrapper);
        mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mobileStoreFragmentMapFragment));

        dashBoardViewPager = (ViewPager) findViewById(R.id.dashBoardViewPager);
        mapChangeIcon = (ImageView) findViewById(R.id.mapChangeIcon);
        onlineTab = tabLayout.newTab().setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.ONLINE, true));
        tabLayout.addTab(onlineTab);
        offlineTab = tabLayout.newTab().setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.OFFLINE, false));
        tabLayout.addTab(offlineTab);
        if (getIntent().getExtras() != null) {
            lat = getIntent().getExtras().getString("lat");
            lang = getIntent().getExtras().getString("lang");
            dashBoardViewPager.setAdapter(new DashBoardViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), getIntent().getExtras().getString("lat"), lang = getIntent().getExtras().getString("lang")));
            getVendorLocation();
        } else {
            lat = String.valueOf(MyApplication.locationInstance().getLocation().getLatitude());
            lang = String.valueOf(MyApplication.locationInstance().getLocation().getLongitude());
            dashBoardViewPager.setAdapter(new DashBoardViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), String.valueOf(MyApplication.locationInstance().getLocation().getLatitude()), String.valueOf(MyApplication.locationInstance().getLocation().getLongitude())));
            getVendorLocation();
        }

        dashBoardViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        dashBoardViewPager.setCurrentItem(0);
        tabLayout.setOnTabSelectedListener(this);
        ChangeLocationSingleton.getInstance().setChangeLocationListener(new ChangeLocationListener() {
            @Override
            public void locationChanged(LatLng latLng, String distance, String address) {
                if (latLng != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }

                if (distance != null && !distance.equalsIgnoreCase("")) {
                    distanceTextView.setText(" " + distance + " km ");
                } else {
                    distanceTextView.setText(" " + distance + " km ");
                }
                if (address != null && !address.equalsIgnoreCase("")) {
                    //locationName = address;
                    locationTextView.setText(address);
                }
            }
        });
        touchFrameLayout.setTouchInterface(new TouchInterface() {
            @Override
            public void onPressed() {
                Log.d(TAG, "onPressing");
                flagIsOnTouched = false;
                isMove = false;
            }

            @Override
            public void onReleased() {
                Log.d(TAG, "onRelease");
                flagIsOnTouched = true;
                if (isMove) {
                    getVendorLocation();
                }
                isMove = false;
            }

            @Override
            public void onMove() {
                Log.d(TAG, "onMove");
                //  isMove = true;
            }
        });
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                setGoogleMap(googleMap);
                enableMyLocation();
                mapAddMarker = new MapAddMarker(DashBoardActivity.this, googleMap);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        setVendorDetailsItem(getVendorByMarkerId(marker.getId()));
                        Log.d(TAG, marker.getId());
                        return true;
                    }
                });
                googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        isMove = true;
                    }
                });
                if (MyApplication.locationInstance() != null && MyApplication.locationInstance().getLocation() != null) {
                    location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLatitude());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 10);
                    flagIsOnTouched = false;
                    googleMap.animateCamera(yourLocation, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            flagIsOnTouched = true;
                        }

                        @Override
                        public void onCancel() {
                            flagIsOnTouched = false;
                        }
                    });
                }
                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        if (mapAddMarker != null) {
                            mapAddMarker.moveCircle(cameraPosition.target);
                            if (flagIsOnTouched) {
                                getVendorLocation();
                                getMarkerMovedAddress(cameraPosition.target);
                            }
                        }
                    }
                });
                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, MapUtils.calculateZoomLevel(DashBoardActivity.this, 15));
                        googleMap.animateCamera(yourLocation);
                        return false;

                    }
                });
            }
        });
        mapChangeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMapView();
            }
        });
    }

    private void setVendorDetailsItem(Vendor vendorByMarkerId) {

    }

    private Vendor getVendorByMarkerId(String markerId) {
        Vendor vendor = null;
        for (Vendor v : listVendor) {
            if (markerId.equalsIgnoreCase(v.getMarkerId())) {
                return v;
            }
        }
        return vendor;
    }

    private void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void checkMapView() {
        if (!mapboolean) {
            mapChangeIcon.setImageResource(R.drawable.map);
            dashBoardViewPager.setVisibility(View.VISIBLE);
            mapFrameLayout.setVisibility(View.GONE);
            mapboolean = true;
        } else {
            mapChangeIcon.setImageResource(R.drawable.list);
            dashBoardViewPager.setVisibility(View.GONE);
            mapFrameLayout.setVisibility(View.VISIBLE);
            getVendorLocation();
            mapboolean = false;
        }
    }

    private boolean enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(DashBoardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DashBoardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DashBoardActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ConstandValue.MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            googleMap.setMyLocationEnabled(true);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVendorLocation();
    }

    private void getVendorLocation() {
        if (dashBoardViewPager.getVisibility() == View.GONE && mapFrameLayout.getVisibility() == View.VISIBLE) {
            if (googleMap != null && googleMap.getCameraPosition() != null) {
                LatLng location1 = googleMap.getCameraPosition().target;
                if (location1 != null) {
                    getVendorLocationWebService(location1);
                    getMarkerMovedAddress(location1);
                }
            }
        } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                getVendorLocationWebService(location);
                getMarkerMovedAddress(location);

            }
        }
        /*if (getIntent().getExtras() != null) {
            //lat = getIntent().getExtras().getString("lat");
            //lang = getIntent().getExtras().getString("lang");
            LatLng latLng = new LatLng(Double.parseDouble(getIntent().getExtras().getString("lat")), Double.parseDouble(getIntent().getExtras().getString("lang")));
            getVendorLocationWebService(location);
            getMarkerMovedAddress(latLng);
        } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                getVendorLocationWebService(location);
                getMarkerMovedAddress(location);

            }
        }*/

    }

    private void getVendorLocationWebService(LatLng location) {
        //if (fragmentType == FragmentType.ONLINE || fragmentType == FragmentType.OFFLINE) {
        GetResponseFromServer.getWebService(DashBoardActivity.this, TAG).getOnlineVendor(DashBoardActivity.this, String.valueOf(location.latitude), String.valueOf(location.longitude), "1", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                urlResponse(response);
            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }

    private void urlResponse(JSONObject response) throws JSONException{
        JSONArray jsonArrayVendor = response.getJSONObject("data").getJSONArray("vendors");
        listVendor.clear();
        for (int i = 0; i < jsonArrayVendor.length(); i++) {
            Vendor vendor = new Gson().fromJson(jsonArrayVendor.getJSONObject(i).toString(), Vendor.class);
            listVendor.add(vendor);
        }
        if (dashBoardViewPager.getVisibility() == View.GONE) {
            ChangeLocationSingleton.getInstance().locationChanges(null, response.getJSONObject("data").getString("distance"), null);

        } else {
//            getMarkerMovedAddress(new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLatitude()));
        }

        //Map Integration Starts
        if (googleMap != null) {
            mapAddMarker.setGoogleMap(googleMap);
            mapAddMarker.addAllMarkLocationMap(listVendor);
//                            mapAddMarker.moveCircle(location);
        }
        //Map Integration Ends

        //List Integration Starts
        //By Raja
        //adapter.notifyDataSetChanged();
    }
    // }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case ConstandValue.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                } else {
                    CommonMethods.toast(DashBoardActivity.this, "My Location permission denied");
                }
                break;
        }

    }

    private void getMarkerMovedAddress(LatLng location) {
        distanceLatLng = location;
        Geocoder geocoder = new Geocoder(DashBoardActivity.this, Locale.getDefault());
        if (geocoder.isPresent()) {
            try {
                List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    if (addresses.get(0).getSubLocality() != null && !addresses.get(0).getSubLocality().equalsIgnoreCase(""))
                        adres = addresses.get(0).getSubLocality();
                    else if (addresses.get(0).getLocality() != null && !addresses.get(0).getLocality().equalsIgnoreCase("")) {
                        adres = addresses.get(0).getLocality();
                    }
                }
                locationName = adres;
                // By Raja

                /*OfferLatLang setLatLang=new OfferLatLang();
                setLatLang.setLat(String.valueOf(dragPosition.latitude));
                setLatLang.setLang(String.valueOf(dragPosition.longitude));*/
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {

        }
        String dis = PreferenceManager.getDefaultSharedPreferences(DashBoardActivity.this).getString("KmDistance", "0.3");
        ChangeLocationSingleton.getInstance().locationChanges(null, dis, adres);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        dashBoardViewPager.setCurrentItem(tab.getPosition());
        switch (tab.getPosition()) {
            case 0:
                mapboolean = false;
                checkMapView();
                onLine();
                onlineTab.setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.ONLINE, true));
                offlineTab.setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.OFFLINE, false));
                mapChangeIcon.setVisibility(View.VISIBLE);
                break;
            case 1:
                mapboolean = false;
                checkMapView();
                offLine();
                onlineTab.setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.ONLINE, false));
                offlineTab.setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.OFFLINE, true));
                mapChangeIcon.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
