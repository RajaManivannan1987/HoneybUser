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
import android.util.Log;
import android.view.View;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sample.honeybuser.Adapter.DashBoardViewPagerAdapter;
import com.sample.honeybuser.CommonActionBar.NavigationBarActivity;
import com.sample.honeybuser.EnumClass.FragmentType;
import com.sample.honeybuser.EnumClass.Selected;
import com.sample.honeybuser.InterFaceClass.ChangeLocationListener;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.MapIntegration.MapAddMarker;
import com.sample.honeybuser.Models.Vendor;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.CustomViewPager;
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
    private TabLayout tabLayout;
    private CustomViewPager dashBoardViewPager;
    private FragmentType fragmentType = FragmentType.ONLINE;
    private TabLayout.Tab onlineTab, offlineTab;
    private List<Vendor> listVendor = new ArrayList<Vendor>();
    String adres = "";
    public static LatLng distanceLatLng = null;
    public static String locationName;
    //private String distance = "5.0", previousDistance = "5.0";


    public void setFragmentType(FragmentType fragmentType) {
        this.fragmentType = fragmentType;
    }

    private void onLine() {
        setFragmentType(FragmentType.ONLINE);
        listVendor.clear();
    }

    private void offLine() {
        setFragmentType(FragmentType.OFFLINE);
        listVendor.clear();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_dashboard);
        //setSelectTab("dashboard");
        setSelected(Selected.DASHBOARD);
        enableMyLocation();
        tabLayout = (TabLayout) findViewById(R.id.dashboardMapActivityTabLayout);


        dashBoardViewPager = (CustomViewPager) findViewById(R.id.dashBoardViewPager);
        //mapChangeIcon = (ImageView) findViewById(R.id.mapChangeIcon);
        onlineTab = tabLayout.newTab().setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.ONLINE, true));
        tabLayout.addTab(onlineTab);
        offlineTab = tabLayout.newTab().setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.OFFLINE, false));
        tabLayout.addTab(offlineTab);
        dashBoardViewPager.setSwipeable(false);
        dashBoardViewPager.setAdapter(new DashBoardViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount()));

        dashBoardViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);
//
//        ChangeLocationSingleton.getInstance().setChangeLocationListener(new ChangeLocationListener() {
//            @Override
//            public void locationChanged(LatLng latLng, String distance, String address) {
//                if (distance != null && !distance.equalsIgnoreCase("")) {
//                    //DashBoardActivity.this.distance = distance;
////                    previousDistance = distance;
//                    // distanceTextView.setText(" " + distance + " km ");
//                }
//
//            }
//        });
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
    }


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


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        dashBoardViewPager.setCurrentItem(tab.getPosition());
        switch (tab.getPosition()) {
            case 0:
                onLine();
                onlineTab.setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.ONLINE, true));
                offlineTab.setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.OFFLINE, false));
                break;
            case 1:
                offLine();
                onlineTab.setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.ONLINE, false));
                offlineTab.setText(CommonMethods.getTabHeading(DashBoardActivity.this, FragmentType.OFFLINE, true));

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
