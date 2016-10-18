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
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.sample.honeybuser.Adapter.DashBoardViewPagerAdapter;
import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.CommonActionBar.NavigationBarActivity;
import com.sample.honeybuser.EnumClass.FragmentType;
import com.sample.honeybuser.EnumClass.Selected;
import com.sample.honeybuser.InterFaceClass.ChangeLocationListener;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;

import java.util.List;
import java.util.Locale;

/**
 * Created by IM0033 on 10/5/2016.
 */

public class DashBoardActivity extends NavigationBarActivity implements TabLayout.OnTabSelectedListener {
    private String TAG = "DashBoardActivity";
    private TabLayout tabLayout;
    private ViewPager dashBoardViewPager;
    private FragmentType fragmentType = FragmentType.ONLINE;
    private TabLayout.Tab onlineTab, offlineTab;
    private ImageView mapChangeIcon;
    private LatLng location = new LatLng(0d, 0d);
    String adres = "";
    public static LatLng distanceLatLng;
    public static String locationName;
    private static String lat, lang;


    public void setFragmentType(FragmentType fragmentType) {
        this.fragmentType = fragmentType;
    }

    private void onLine() {
        //getVendorLocation();
        setFragmentType(FragmentType.ONLINE);
    }

    private void offLine() {
        //getVendorLocation();
        setFragmentType(FragmentType.OFFLINE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_dashboard);
        //setSelectTab("dashboard");
        setSelected(Selected.DASHBOARD);
        enableMyLocation();
        tabLayout = (TabLayout) findViewById(R.id.dashboardMapActivityTabLayout);
        //tabLayout.setSelectedTabIndicatorHeight(0);
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
            getVendorLocation(lat, lang);
        } else {
            lat = String.valueOf(MyApplication.locationInstance().getLocation().getLatitude());
            lang = String.valueOf(MyApplication.locationInstance().getLocation().getLongitude());
            dashBoardViewPager.setAdapter(new DashBoardViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), String.valueOf(MyApplication.locationInstance().getLocation().getLatitude()), String.valueOf(MyApplication.locationInstance().getLocation().getLongitude())));
            getVendorLocation(lat, lang);
        }

        dashBoardViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        dashBoardViewPager.setCurrentItem(0);
        tabLayout.setOnTabSelectedListener(this);
        ChangeLocationSingleton.getInstance().setChangeLocationListener(new ChangeLocationListener() {
            @Override
            public void locationChanged(LatLng latLng, String distance, String address) {
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
    }

    private boolean enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(DashBoardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DashBoardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DashBoardActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ConstandValue.MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            //googleMap.setMyLocationEnabled(true);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // getVendorLocation();
    }

    private void getVendorLocation(String lat, String lang) {
        if (getIntent().getExtras() != null) {
            //lat = getIntent().getExtras().getString("lat");
            //lang = getIntent().getExtras().getString("lang");
            LatLng latLng = new LatLng(Double.parseDouble(getIntent().getExtras().getString("lat")), Double.parseDouble(getIntent().getExtras().getString("lang")));
            getMarkerMovedAddress(latLng);
        } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                // getVendorLocationWebService(location);
                getMarkerMovedAddress(location);

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
                onLine();
                onlineTab.setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.ONLINE, true));
                offlineTab.setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.OFFLINE, false));
                mapChangeIcon.setVisibility(View.VISIBLE);
                break;
            case 1:
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
