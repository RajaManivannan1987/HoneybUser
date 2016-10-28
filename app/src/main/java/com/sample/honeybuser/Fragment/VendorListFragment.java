package com.sample.honeybuser.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sample.honeybuser.Activity.DashBoardActivity;
import com.sample.honeybuser.Adapter.OffLineVendorListAdapter;
import com.sample.honeybuser.Adapter.OnLineVendorListAdapter;
import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.InterFaceClass.SaveCompletedInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Models.OffLineVendorListModel;
import com.sample.honeybuser.Models.OnLineVendorListModel;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IM0033 on 10/6/2016.
 */
public class VendorListFragment extends Fragment {
    private String TAG = "VendorListFragment";
    private RecyclerView onLineRecyclerView, offLineVendorRecyclerView;
    private OnLineVendorListAdapter onLineAdapter;
    private OffLineVendorListAdapter offLineAdapter;
    private Gson gson = new Gson();
    private List<OnLineVendorListModel> onLineList = new ArrayList<OnLineVendorListModel>();
    private List<OffLineVendorListModel> offLineList = new ArrayList<OffLineVendorListModel>();
    private String lat, lang, distance;
    private TextView offLineToastText, onLineToastText;

    // private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendor_list, container, false);
       /* bundle = this.getArguments();
        if (bundle != null) {
            lat = bundle.getString("lat");
            lang = bundle.getString("lang");
        }*/
        onLineToastText = (TextView) view.findViewById(R.id.onLineToastText);
        offLineToastText = (TextView) view.findViewById(R.id.offLineToastText);

        onLineRecyclerView = (RecyclerView) view.findViewById(R.id.onLineVendorList);
        offLineVendorRecyclerView = (RecyclerView) view.findViewById(R.id.offLineVendorRecyclerView);

        onLineRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        offLineVendorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        onLineAdapter = new OnLineVendorListAdapter(getActivity(), onLineList, "1");
        offLineAdapter = new OffLineVendorListAdapter(getActivity(), offLineList, "0");

        onLineRecyclerView.setAdapter(onLineAdapter);
        offLineVendorRecyclerView.setAdapter(offLineAdapter);
        Complete.offerDialogInstance().setListener(new SaveCompletedInterface() {
            @Override
            public void completed() {
                getData();
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
        } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                lat = String.valueOf(MyApplication.locationInstance().getLocation().getLatitude());
                lang = String.valueOf(MyApplication.locationInstance().getLocation().getLongitude());
            }
        }
        GetResponseFromServer.getWebService(getActivity(), TAG).getOnlineVendor(getActivity(), lat, lang, "", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                onLineList.clear();
                offLineList.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    JSONObject jsonObject = response.getJSONObject("data");
                    distance = jsonObject.getString("distance");
                    for (int i = 0; i < jsonObject.getJSONArray("online").length(); i++) {
                        onLineList.add(gson.fromJson(jsonObject.getJSONArray("online").getJSONObject(i).toString(), OnLineVendorListModel.class));
                    }
                    for (int i = 0; i < jsonObject.getJSONArray("offline").length(); i++) {
                        offLineList.add(gson.fromJson(jsonObject.getJSONArray("offline").getJSONObject(i).toString(), OffLineVendorListModel.class));
                    }
                    if (!jsonObject.getString("online_available").equalsIgnoreCase("Y")) {
                        onLineRecyclerView.setVisibility(View.GONE);
                        onLineToastText.setVisibility(View.VISIBLE);
                    } else {
                        onLineRecyclerView.setVisibility(View.VISIBLE);
                        onLineToastText.setVisibility(View.GONE);
                    }
                    if (!jsonObject.getString("offline_available").equalsIgnoreCase("Y")) {
                        offLineVendorRecyclerView.setVisibility(View.GONE);
                        offLineToastText.setVisibility(View.VISIBLE);
                    } else {
                        offLineVendorRecyclerView.setVisibility(View.VISIBLE);
                        offLineToastText.setVisibility(View.GONE);
                    }

                } else {
                    //onLineRecyclerView.setVisibility(View.GONE);
                    //txtView.setVisibility(View.VISIBLE);
                    //txtView.setText(response.getString("message"));
                }
                onLineAdapter.notifyDataSetChanged();
                offLineAdapter.notifyDataSetChanged();
                ChangeLocationSingleton.getInstance().locationChanges(null, distance, null);
            }

            @Override
            public void onError(String message, String title) {

            }
        });
        //CommonMethods.showLocationAlert(getActivity());
    }


}
