package com.sample.honeybuser.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sample.honeybuser.Activity.DashBoardActivity;
import com.sample.honeybuser.Adapter.VendorSearchAdapter;
import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.InterFaceClass.SaveCompletedInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Models.VendorSearchListModel;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.GridSpacingItemDecoration;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Im033 on 10/18/2016.
 */

public class VendorSearchFragment extends Fragment {
    private String TAG = "VendorSearchFragment";
    private RecyclerView vendorSearchRecyclerView;
    private ArrayList<VendorSearchListModel> vendorList = new ArrayList<>();
    private VendorSearchAdapter adapter;
    int spancount = 2;
    int spacing = 30;
    boolean includeEdge = true;
    private TextView txtView;
    private EditText vendorSearch;
    private ImageView closeButton;
    private Gson gson = new Gson();

    private String lat = "", lang = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendor_search, container, false);
        txtView = (TextView) view.findViewById(R.id.noRecordTextView);
        vendorSearch = (EditText) view.findViewById(R.id.vendorSearch);
        closeButton = (ImageView) view.findViewById(R.id.vendorCloseButton);

        vendorSearchRecyclerView = (RecyclerView) view.findViewById(R.id.vendorSearchRecyclerView);

        vendorSearchRecyclerView.setHasFixedSize(true);

        vendorSearchRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        vendorSearchRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spancount, spacing, includeEdge));

        adapter = new VendorSearchAdapter(getActivity(), vendorList);
        vendorSearchRecyclerView.setAdapter(adapter);

        Complete.getVendorSearch().setListener(new SaveCompletedInterface() {
            @Override
            public void completed() {
                getVendorList();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vendorSearch.setText("");
            }
        });
        vendorSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                charSequence = charSequence.toString().toLowerCase();
                ArrayList<VendorSearchListModel> filterList = new ArrayList<VendorSearchListModel>();
                for (int j = 0; j < vendorList.size(); j++) {
                    String text = vendorList.get(j).getName().toLowerCase();
                    if (text.contains(charSequence)) {
                        filterList.add(vendorList.get(j));
                    }
                }
                vendorSearchRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                //vendorSearchRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spancount, spacing, includeEdge));
                adapter = new VendorSearchAdapter(getActivity(), filterList);
                vendorSearchRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        getVendorList();
        super.onResume();
    }

    private void getVendorList() {
        if (DashBoardActivity.distanceLatLng != null) {
            lat = String.valueOf(DashBoardActivity.distanceLatLng.latitude);
            lang = String.valueOf(DashBoardActivity.distanceLatLng.longitude);
        } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                lat = String.valueOf(MyApplication.locationInstance().getLocation().getLatitude());
                lang = String.valueOf(MyApplication.locationInstance().getLocation().getLongitude());
            }
        }
        GetResponseFromServer.getWebService(getActivity(), TAG).getSearchVendorList(getActivity(), lat, lang, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                vendorList.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    JSONObject jsonObject = response.getJSONObject("data");
                    for (int i = 0; i < jsonObject.getJSONArray("vendors").length(); i++) {
                        vendorList.add(gson.fromJson(jsonObject.getJSONArray("vendors").getJSONObject(i).toString(), VendorSearchListModel.class));
                    }
                } else {
                    vendorSearchRecyclerView.setVisibility(View.GONE);
                    txtView.setVisibility(View.VISIBLE);
                    txtView.setText(response.getString("message"));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }
}
