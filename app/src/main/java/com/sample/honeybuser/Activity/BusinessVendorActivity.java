package com.sample.honeybuser.Activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.print.PrintHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sample.honeybuser.Adapter.BusinessVendorAdapter;
import com.sample.honeybuser.Adapter.OnLineVendorListAdapter;
import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.CommonActionBar.CommonActionBar;
import com.sample.honeybuser.InterFaceClass.SaveCompletedInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Models.BusinessVendorModel;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Im033 on 10/20/2016.
 */

public class BusinessVendorActivity extends CommonActionBar {
    private String TAG = "BusinessVendorActivity";
    private RecyclerView businessListRecyclerView;
    private ArrayList<BusinessVendorModel> businessList = new ArrayList<>();
    private BusinessVendorAdapter adapter;
    //    private OnLineVendorListAdapter adapter;
    private String business_id = "", distance = "", businessName = "";
    private Gson gson = new Gson();
    private TextView businessListTextView;
    private String lat = "", lang = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_business_vendor);

        hideNotification();
        businessListTextView = (TextView) findViewById(R.id.noRecordTextView);
        businessListRecyclerView = (RecyclerView) findViewById(R.id.businessListRecyclerView);
        businessListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BusinessVendorAdapter(BusinessVendorActivity.this, businessList);
//        adapter = new OnLineVendorListAdapter(BusinessVendorActivity.this, businessList);
        businessListRecyclerView.setAdapter(adapter);
        Complete.getBusinessList().setListener(new SaveCompletedInterface() {
            @Override
            public void completed() {
                getBusinessList(business_id);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null) {
            business_id = getIntent().getExtras().getString("business_id");
            distance = getIntent().getExtras().getString("distance");
            getBusinessList(business_id);
        }
    }

    private void getBusinessList(String business_id) {
        if (DashBoardActivity.distanceLatLng != null) {
            lat = String.valueOf(DashBoardActivity.distanceLatLng.latitude);
            lang = String.valueOf(DashBoardActivity.distanceLatLng.longitude);
        } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                lat = String.valueOf(MyApplication.locationInstance().getLocation().getLatitude());
                lang = String.valueOf(MyApplication.locationInstance().getLocation().getLongitude());
            }
        }
        GetResponseFromServer.getWebService(BusinessVendorActivity.this, TAG).getBusinessVendorList(BusinessVendorActivity.this, lat, lang, "0.50", business_id, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                businessList.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    businessName = response.getString("business_name");
                    setTitle(businessName);
                    for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                        businessList.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), BusinessVendorModel.class));
                    }
                } else {
                    businessListRecyclerView.setVisibility(View.GONE);
                    businessListTextView.setVisibility(View.VISIBLE);
                    businessListTextView.setText(response.getString("message"));
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
