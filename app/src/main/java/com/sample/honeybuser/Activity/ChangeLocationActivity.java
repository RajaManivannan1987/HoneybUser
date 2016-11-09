package com.sample.honeybuser.Activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sample.honeybuser.Adapter.AddressRecyclerViewAdapter;
import com.sample.honeybuser.Adapter.DistanceSelectRecyclerViewAdapter;
import com.sample.honeybuser.CommonActionBar.CommonActionBar;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Models.ChangeAddress;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.AlertDialogManager;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Im033 on 10/11/2016.
 */

public class ChangeLocationActivity extends CommonActionBar implements PlaceSelectionListener {
    private String TAG = "ChangeLocationActivity";
    private PlaceAutocompleteFragment autocompleteFragment;
    private DistanceSelectRecyclerViewAdapter distanceAdapter;
    private AddressRecyclerViewAdapter addressAdapter;
    private RecyclerView changeDistanceRecyclerView, previousRecyclerView;
    private List<String> distanceList = new ArrayList<String>();
    private List<ChangeAddress> addressList = new ArrayList<ChangeAddress>();
    private String lat, lang;
    private Gson gson = new Gson();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_change_location);
        setTitle("Change Location");
        hideNotification();
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setHint("Enter new address");
        autocompleteFragment.getView().setBackground(getResources().getDrawable(R.drawable.background_edit_text));
//        autocompleteFragment.getView().setPadding(15, 0, 15, 0);
        autocompleteFragment.setOnPlaceSelectedListener(this);

        /*distanceList.add("0.1");
        distanceList.add("0.2");
        distanceList.add("0.3");
        distanceList.add("0.4");
        distanceList.add("0.5");*/

        changeDistanceRecyclerView = (RecyclerView) findViewById(R.id.changeLocationActivityChangeDistanceRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        changeDistanceRecyclerView.setLayoutManager(layoutManager);

        previousRecyclerView = (RecyclerView) findViewById(R.id.changeLocationActivityPreviouslySearchedRecyclerView);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        previousRecyclerView.setLayoutManager(layoutManager1);

        distanceAdapter = new DistanceSelectRecyclerViewAdapter(this, distanceList, "ChangeLocation");
        changeDistanceRecyclerView.setAdapter(distanceAdapter);
        addressAdapter = new AddressRecyclerViewAdapter(this, addressList);
        previousRecyclerView.setAdapter(addressAdapter);
        //KmDistance = distanceAdapter.getDistance();
        getData();
    }

    @Override
    public void onPlaceSelected(Place place) {
        String str = getMarkerMovedAddress(place.getLatLng());
        if (str != null && !str.equals("")) {
            SpannableStringBuilder sb = new SpannableStringBuilder(str);
            sb.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.text_sixe_small)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//resize size
            if (autocompleteFragment != null) {
                autocompleteFragment.setText(sb);
            }
        }
    }

    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    private String getMarkerMovedAddress(LatLng dragPosition) {
        String adres = "";
        String location = "";
//        DashBoardActivity.distanceLatLng = dragPosition;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(dragPosition.latitude, dragPosition.longitude, 1);
            lat = String.valueOf(dragPosition.latitude);
            lang = String.valueOf(dragPosition.longitude);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                if (addresses.get(0).getSubLocality() != null) {
                    location = addresses.get(0).getSubLocality();
                } else if (addresses.get(0).getLocality() != null) {
                    location = addresses.get(0).getLocality();
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(returnedAddress.getAddressLine(i));
                }
                Log.d(TAG, returnedAddress.toString());
                adres = stringBuilder.toString();
                //By raja
                locationPicker(location, adres, lat, lang, dragPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adres;
    }

    private void locationPicker(final String location, String adres, final String lat, final String lang, final LatLng dragPosition) {
        Log.d(TAG, location + " " + adres + " " + lat + " " + lang + " " + dragPosition);

        GetResponseFromServer.getWebService(ChangeLocationActivity.this, TAG).addressSave(ChangeLocationActivity.this, location, adres, lat, lang, "0.50", "Y", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    DashBoardActivity.distanceLatLng = dragPosition;
                    ChangeLocationSingleton.getInstance().locationChanges(dragPosition, "0.50", null, "ChangeLocationActivity");
//                    Complete.offerDialogInstance().orderCompleted();

                    //                     By Raja 4.11.16
//                    set distance in dashboard page
//                    ChangeLocationSingleton.getInstance().locationChanges(dragPosition, distanceAdapter.getDistance(), null, "ChangeLocation");
                    ChangeLocationActivity.this.finish();
                }

            }

            @Override
            public void onError(String message, String title) {
                new AlertDialogManager().showAlertDialog(ChangeLocationActivity.this, title, message, false);
            }
        });

    }

    private void getData() {
        GetResponseFromServer.getWebService(ChangeLocationActivity.this, TAG).addressDistanceList(this, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                addressList.clear();
                distanceList.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    for (int i = 0; i < response.getJSONObject("data").getJSONArray("address").length(); i++) {
                        addressList.add(gson.fromJson(response.getJSONObject("data").getJSONArray("address").getJSONObject(i).toString(), ChangeAddress.class));
                    }
                    for (int i = 0; i < response.getJSONObject("data").getJSONArray("distance").length(); i++) {
                        distanceList.add(response.getJSONObject("data").getJSONArray("distance").getJSONObject(i).getString("range"));
                        if (response.getJSONObject("data").getJSONArray("distance").getJSONObject(i).getString("selected").equalsIgnoreCase("Y")) {
                            distanceAdapter.selectPosition(i);
                            PreferenceManager.getDefaultSharedPreferences(ChangeLocationActivity.this).edit().putString("KmDistance", distanceAdapter.getDistance()).commit();
                        }
                    }
                } else {
                    new AlertDialogManager().showAlertDialog(ChangeLocationActivity.this, "Alert", response.getString("message"), false);
                }
                distanceAdapter.notifyDataSetChanged();
                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
