package com.sample.honeybuser.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sample.honeybuser.Activity.DashBoardActivity;
import com.sample.honeybuser.Adapter.OffLineVendorListAdapter;
import com.sample.honeybuser.Adapter.OnLineVendorListAdapter;
import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.CommonActionBar.NavigationBarActivity;
import com.sample.honeybuser.EnumClass.FragmentType;
import com.sample.honeybuser.InterFaceClass.ChangeLocationListener;
import com.sample.honeybuser.InterFaceClass.SaveCompletedInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Models.OffLineVendorListModel;
import com.sample.honeybuser.Models.OnLineVendorListModel;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IM0033 on 10/6/2016.
 */
public class VendorListFragment extends Fragment {
    private String TAG = "VendorListFragment";
    private RecyclerView onLineRecyclerView;
    private OnLineVendorListAdapter onLineAdapter;
    private Gson gson = new Gson();
    private List<OnLineVendorListModel> onLineList = new ArrayList<OnLineVendorListModel>();
    //    private List<OffLineVendorListModel> offLineList = new ArrayList<OffLineVendorListModel>();
//    private OffLineVendorListAdapter offLineAdapter;
    private String lat, lang, distance, address, subAddress;
    public static LatLng latLngValue = null;
    private TextView onLineToastText;
//    private FragmentType fragmentType;
//    TimerTask timerTask;
//    Timer timer = new Timer();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendor_list, container, false);
        onLineToastText = (TextView) view.findViewById(R.id.onLineToastText);
//        offLineToastText = (TextView) view.findViewById(R.id.offLineToastText);

        onLineRecyclerView = (RecyclerView) view.findViewById(R.id.onLineVendorList);
//        offLineVendorRecyclerView = (RecyclerView) view.findViewById(R.id.offLineVendorRecyclerView);

        onLineRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        offLineVendorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        onLineAdapter = new OnLineVendorListAdapter(getActivity(), onLineList, "1");
//        offLineAdapter = new OffLineVendorListAdapter(getActivity(), offLineList, "0");

        onLineRecyclerView.setAdapter(onLineAdapter);
//        offLineVendorRecyclerView.setAdapter(offLineAdapter);

        Complete.offerDialogInstance().setListener(new SaveCompletedInterface() {
            @Override
            public void completed() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getData();
//                        Complete.getGetMapList().orderCompleted();
                    }
                });

            }
        });

        return view;
    }

    private void getMarkerMovedAddress(LatLng target) {
        address = CommonMethods.getAddressName(getActivity(), target, TAG);
        /*Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        if (geocoder.isPresent()) {
            try {
                //DashBoardActivity.distanceLatLng = target;
                List<Address> addresses = geocoder.getFromLocation(target.latitude, target.longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    if (addresses.get(0).getSubLocality() != null && !addresses.get(0).getSubLocality().equalsIgnoreCase("")) {
                        address = addresses.get(0).getSubLocality();
                        subAddress = addresses.get(0).getThoroughfare();

                    } else {
                        if (addresses.get(0).getLocality() != null && !addresses.get(0).getLocality().equalsIgnoreCase("")) {
                            address = addresses.get(0).getLocality();
                            subAddress = addresses.get(0).getThoroughfare();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }*/

    }

    @Override
    public void onResume() {
//        getData();
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
       /* Log.e(TAG, "OnPause");
        if (timer != null)
            timer.cancel();
        if (timerTask != null)
            timerTask.cancel();*/
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            TimerTask timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    getData();
//                    // Complete.offerDialogInstance().orderCompleted();
//                    Log.e(TAG, "Timer start");
//                }
//            };
//            timer = new Timer();
//            timer.schedule(timerTask, 10, 10000);
//
//        }
    }

    private void getData() {
        if (DashBoardActivity.distanceLatLng != null) {
            lat = String.valueOf(DashBoardActivity.distanceLatLng.latitude);
            lang = String.valueOf(DashBoardActivity.distanceLatLng.longitude);
            latLngValue = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
            getMarkerMovedAddress(latLngValue);
        } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                lat = String.valueOf(MyApplication.locationInstance().getLocation().getLatitude());
                lang = String.valueOf(MyApplication.locationInstance().getLocation().getLongitude());
                latLngValue = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
                getMarkerMovedAddress(latLngValue);
            }
        }
        GetResponseFromServer.getWebService(getActivity(), TAG).getOnlineVendor(getActivity(), lat, lang, "", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                onLineList.clear();
//                offLineList.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    JSONObject jsonObject = response.getJSONObject("data");

                    //                     By Raja 4.11.16
//                    set distance in dashboard page
//                    distance = jsonObject.getString("distance");
//                    NavigationBarActivity.distanceTextView.setText(" " + distance + " km ");

                    for (int i = 0; i < jsonObject.getJSONArray("vendors").length(); i++) {
                        onLineList.add(gson.fromJson(jsonObject.getJSONArray("vendors").getJSONObject(i).toString(), OnLineVendorListModel.class));
                    }

                   if (jsonObject.getJSONArray("vendors").length() == 0) {
                        onLineRecyclerView.setVisibility(View.GONE);
                        onLineToastText.setVisibility(View.VISIBLE);
                    } else {
                        onLineRecyclerView.setVisibility(View.VISIBLE);
                        onLineToastText.setVisibility(View.GONE);
                    }


                }
                onLineAdapter.notifyDataSetChanged();
//                offLineAdapter.notifyDataSetChanged();
                ChangeLocationSingleton.getInstance().locationChanges(null, null, address, TAG);
            }

            @Override
            public void onError(String message, String title) {

            }
        });
        //CommonMethods.showLocationAlert(getActivity());
    }


}
