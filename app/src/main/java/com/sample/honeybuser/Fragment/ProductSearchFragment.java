package com.sample.honeybuser.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.sample.honeybuser.Activity.DashBoardActivity;
import com.sample.honeybuser.Adapter.Product05SearchAdapter;
import com.sample.honeybuser.Adapter.Product3KmSearchAdapter;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Models.FiveKmProductSearchModel;
import com.sample.honeybuser.Models.ThreeKmProductSearchModel;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.GridSpacingItemDecoration;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Im033 on 10/18/2016.
 */

public class ProductSearchFragment extends Fragment {
    private String TAG = "ProductSearchFragment";
    private RecyclerView threeKmVendorRecyclerView, fiveKmVendorRecyclerView;
    private ArrayList<ThreeKmProductSearchModel> threeKmVendorList = new ArrayList<>();
    private ArrayList<FiveKmProductSearchModel> fiveKmVendorList = new ArrayList<>();
    private Product3KmSearchAdapter adapter3Km;
    private Product05SearchAdapter adapter05km;
    int spancount = 3;
    int spacing = 30;
    boolean includeEdge = true;
    private Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_search, container, false);
        threeKmVendorRecyclerView = (RecyclerView) view.findViewById(R.id.threeKmVendorRecyclerView);
        fiveKmVendorRecyclerView = (RecyclerView) view.findViewById(R.id.fiveKmVendorRecyclerView);
        threeKmVendorRecyclerView.setHasFixedSize(true);
        fiveKmVendorRecyclerView.setHasFixedSize(true);
        fiveKmVendorRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        threeKmVendorRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        threeKmVendorRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spancount, spacing, includeEdge));
        fiveKmVendorRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spancount, spacing, includeEdge));
        adapter3Km = new Product3KmSearchAdapter(getActivity(), threeKmVendorList);
        adapter05km = new Product05SearchAdapter(getActivity(), fiveKmVendorList);
        fiveKmVendorRecyclerView.setAdapter(adapter05km);
        threeKmVendorRecyclerView.setAdapter(adapter3Km);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        GetResponseFromServer.getWebService(getActivity(), TAG).getProductList(getActivity(), DashBoardActivity.distanceLatLng.latitude, DashBoardActivity.distanceLatLng.longitude, "0.50", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                threeKmVendorList.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                        threeKmVendorList.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), ThreeKmProductSearchModel.class));
                    }
                }
                adapter3Km.notifyDataSetChanged();
                getFiveKmProductList();
            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }

    private void getFiveKmProductList() {
        GetResponseFromServer.getWebService(getActivity(), TAG).getProductList(getActivity(), DashBoardActivity.distanceLatLng.latitude, DashBoardActivity.distanceLatLng.longitude, "3.00", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                fiveKmVendorList.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                        fiveKmVendorList.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), FiveKmProductSearchModel.class));
                    }
                }
                // adapter.notifyDataSetChanged();
                adapter05km.notifyDataSetChanged();
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
