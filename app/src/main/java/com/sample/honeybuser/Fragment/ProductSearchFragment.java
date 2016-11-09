package com.sample.honeybuser.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sample.honeybuser.Activity.DashBoardActivity;
import com.sample.honeybuser.Adapter.Product05SearchAdapter;
import com.sample.honeybuser.Adapter.Product3KmSearchAdapter;
import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Models.FiveKmProductSearchModel;
import com.sample.honeybuser.Models.ThreeKmProductSearchModel;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
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
    private String address = "";
    private RecyclerView threeKmVendorRecyclerView, fiveKmVendorRecyclerView;

    private ArrayList<FiveKmProductSearchModel> fiveKmVendorList = new ArrayList<>();
    private ArrayList<ThreeKmProductSearchModel> threeKmVendorList = new ArrayList<>();

    private Product05SearchAdapter adapter05km;
    private Product3KmSearchAdapter adapter3Km;

    private EditText productSearch;
    private ImageView closeButton;
    int spancount = 3;
    int spacing = 30;
    private LatLng latLng = null;
    boolean includeEdge = true;
    private Gson gson = new Gson();

    private String lat = "", lang = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_search, container, false);
        productSearch = (EditText) view.findViewById(R.id.productSearch);
        closeButton = (ImageView) view.findViewById(R.id.productCloseButton);

        threeKmVendorRecyclerView = (RecyclerView) view.findViewById(R.id.threeKmVendorRecyclerView);
        fiveKmVendorRecyclerView = (RecyclerView) view.findViewById(R.id.fiveKmVendorRecyclerView);

        threeKmVendorRecyclerView.setHasFixedSize(true);
        fiveKmVendorRecyclerView.setHasFixedSize(true);

        fiveKmVendorRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        threeKmVendorRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        threeKmVendorRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spancount, spacing, includeEdge));
        fiveKmVendorRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spancount, spacing, includeEdge));

        adapter3Km = new Product3KmSearchAdapter(getActivity(), threeKmVendorList);
        adapter05km = new Product05SearchAdapter(getActivity(), fiveKmVendorList);

        fiveKmVendorRecyclerView.setAdapter(adapter05km);
        threeKmVendorRecyclerView.setAdapter(adapter3Km);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productSearch.setText("");
            }
        });
        productSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                charSequence = charSequence.toString().toLowerCase();
                ArrayList<ThreeKmProductSearchModel> threeKmfilterList = new ArrayList<>();
                ArrayList<FiveKmProductSearchModel> fiveKmfilterList = new ArrayList<>();
                for (int j = 0; j < threeKmVendorList.size(); j++) {
                    String threeKmText = threeKmVendorList.get(j).getBusiness_name().toLowerCase();
                    if (threeKmText.contains(charSequence)) {
                        threeKmfilterList.add(threeKmVendorList.get(j));
                    }
                }
                for (int k = 0; k < fiveKmVendorList.size(); k++) {
                    String fiveKmText = fiveKmVendorList.get(k).getBusiness_name().toLowerCase();
                    if (fiveKmText.contains(charSequence)) {
                        fiveKmfilterList.add(fiveKmVendorList.get(k));
                    }
                }
                fiveKmVendorRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                threeKmVendorRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//                threeKmVendorRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spancount, spacing, includeEdge));
//                fiveKmVendorRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spancount, spacing, includeEdge));
                adapter3Km = new Product3KmSearchAdapter(getActivity(), threeKmfilterList);
                adapter05km = new Product05SearchAdapter(getActivity(), fiveKmfilterList);
                fiveKmVendorRecyclerView.setAdapter(adapter05km);
                threeKmVendorRecyclerView.setAdapter(adapter3Km);
                adapter05km.notifyDataSetChanged();
                adapter3Km.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        if (DashBoardActivity.distanceLatLng != null) {
            lat = String.valueOf(DashBoardActivity.distanceLatLng.latitude);
            lang = String.valueOf(DashBoardActivity.distanceLatLng.longitude);
            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
            address = CommonMethods.getAddressName(getActivity(), latLng, TAG);
        } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                lat = String.valueOf(MyApplication.locationInstance().getLocation().getLatitude());
                lang = String.valueOf(MyApplication.locationInstance().getLocation().getLongitude());
                latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
                address = CommonMethods.getAddressName(getActivity(), latLng, TAG);
            }
        }
        getFiveKmProductList();
    }

    private void getThreeKmProducts() {
        GetResponseFromServer.getWebService(getActivity(), TAG).getProductList(getActivity(), lat, lang, "0.50", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                threeKmVendorList.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                        threeKmVendorList.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), ThreeKmProductSearchModel.class));
                    }
                }
                adapter3Km.notifyDataSetChanged();
//                getFiveKmProductList();
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    private void getFiveKmProductList() {
        GetResponseFromServer.getWebService(getActivity(), TAG).getProductList(getActivity(), lat, lang, "3.00", new VolleyResponseListerner() {
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
                ChangeLocationSingleton.getInstance().locationChanges(null, null, address, TAG);
                getThreeKmProducts();

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
