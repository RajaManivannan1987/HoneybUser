package com.sample.honeybuser.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.sample.honeybuser.Activity.DashBoardActivity;
import com.sample.honeybuser.Activity.VendorDetailActivity;
import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.InterFaceClass.ChangeLocationListener;
import com.sample.honeybuser.InterFaceClass.TouchInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.MapIntegration.MapAddMarker;
import com.sample.honeybuser.MapIntegration.MapUtils;
import com.sample.honeybuser.MapIntegration.TouchableWrapper;
import com.sample.honeybuser.Models.Vendor;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import static com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

/**
 * Created by IM0033 on 10/6/2016.
 */
public class OnLineMapFragment extends Fragment {
    private String TAG = "OnLineMapFragment";
    private FrameLayout mapFrameLayout;
    private TouchableWrapper touchFrameLayout;
    private SupportMapFragment mapView;
    private GoogleMap googleMap;
    private MapAddMarker mapAddMarker;
    private List<Vendor> listVendor = new ArrayList<Vendor>();
    private boolean isMove = false;
    private String distance = "5.0", previousDistance = "5.0", vendorId, callPhone;
    private boolean flagIsOnTouched = true;
    private LatLng location;
    private String adres = "";
    private LinearLayout vendorItemLinearLayout, vendorItemBackgroundLinearLayout;
    private RelativeLayout mapRelativeLayout;
    private TextView vendorNameTextView;
    private CircleImageView vendorLogoCircleImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_mapview, container, false);
        mapFrameLayout = (FrameLayout) view.findViewById(R.id.mapFrameLayout);
        touchFrameLayout = (TouchableWrapper) view.findViewById(R.id.onlineMapFragmentTouchableWrapper);
        mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        vendorItemLinearLayout = (LinearLayout) view.findViewById(R.id.mobileStoreFragmentVendorbackGroundLinearLayout);
        vendorItemBackgroundLinearLayout = (LinearLayout) view.findViewById(R.id.mobileStoreFragmentVendorLinearLayout);
        mapRelativeLayout = (RelativeLayout) view.findViewById(R.id.mobileStoreFragmentLocationMapRelativeLayout);
        vendorNameTextView = (TextView) view.findViewById(R.id.mobileStoreFragmentVendorNameShopTextView);
        vendorLogoCircleImageView = (CircleImageView) view.findViewById(R.id.mobileStoreFragmentVendorCircleImageView);


        touchFrameLayout.setTouchInterface(new TouchInterface() {
            @Override
            public void onPressed() {
                flagIsOnTouched = false;
                isMove = false;
                hideVendorItem();
            }

            @Override
            public void onReleased() {
                flagIsOnTouched = true;
                if (isMove) {
                    getVendorLocation();
//                    getMarkerMovedAddress(googleMap.getCameraPosition().target);
                }
                isMove = false;
            }

            @Override
            public void onMove() {

            }
        });
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                setMapView(googleMap);
                enableMyLocation();
                Log.d("volleyPostData", "onMapReady");
                mapAddMarker = new MapAddMarker(getActivity(), googleMap);

                googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        setVendorDetailsItem(getVendorByMarkerId(marker.getId()));
                        Log.d("volleyPostData", "setOnMarkerClickListener");
                        return true;
                    }
                });
                googleMap.setOnCameraMoveListener(new OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        isMove = true;
                        Log.d("volleyPostData", "onCameraMove");
                    }
                });
                if (MyApplication.locationInstance() != null && MyApplication.locationInstance().getLocation() != null) {
                    Log.d("volleyPostData", "MylocationInstance");
                    location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 10);
                    googleMap.animateCamera(yourLocation);
                    flagIsOnTouched = false;
                    googleMap.animateCamera(yourLocation, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            flagIsOnTouched = true;
                            Log.d("volleyPostData", "onFinish");
                        }

                        @Override
                        public void onCancel() {
                            flagIsOnTouched = true;
                            Log.d("volleyPostData", "onCancel");
                        }
                    });
                    googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                        @Override
                        public void onCameraChange(CameraPosition cameraPosition) {
                            if (mapAddMarker != null)
                                mapAddMarker.moveCircle(cameraPosition.target);
                            if (flagIsOnTouched) {
                                getVendorLocation();
                                getMarkerMovedAddress(cameraPosition.target);
                                Log.d("volleyPostData", "onCameraChange");
                            }
                        }
                    });
                    googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {
                            location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, MapUtils.calculateZoomLevel(getActivity(), 15));
                            googleMap.animateCamera(yourLocation);
                            Log.d("volleyPostData", "onMyLocationButtonClick");
                            return false;

                        }
                    });
                }
            }
        });
        ChangeLocationSingleton.getInstance().setChangeLocationListener(new ChangeLocationListener() {
            @Override
            public void locationChanged(LatLng latLng, String distance, String address) {
                Log.d("volleyPostData", "locationChanged");
                if (latLng != null) {
                    if (googleMap != null) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        DashBoardActivity.distanceLatLng = latLng;
                    }
                }

                if (distance != null && !distance.equalsIgnoreCase("")) {
                    OnLineMapFragment.this.distance = distance;
                    if (mapAddMarker != null) {
                        mapAddMarker.radiCircle(Float.parseFloat(distance) * 1000);
                    }
                    if (!previousDistance.equalsIgnoreCase(distance)) {
                        if (mapAddMarker != null) {
                            mapAddMarker.changeZoomLevel(Float.parseFloat(distance) * 1000);
                        }
                    }
                    previousDistance = distance;
                }

            }
        });

        return view;
    }

    private void hideVendorItem() {
        vendorItemLinearLayout.setVisibility(View.GONE);
        vendorItemBackgroundLinearLayout.setVisibility(View.GONE);
    }

    private void getVendorLocation() {
        //if (fragmentType != FragmentType.ONLINE) {
        if (DashBoardActivity.distanceLatLng != null) {
            LatLng location1 = DashBoardActivity.distanceLatLng;
            if (location1 != null) {
                Log.d("volleyPostData", "getVendorLocation +location1 ");
                getVendorLocationWebService(location1);
            }
        } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                getVendorLocationWebService(location);
                Log.d("volleyPostData", "getVendorLocation +location ");
            }
        }
       /* if (googleMap.getCameraPosition() != null) {
            LatLng location1 = googleMap.getCameraPosition().target;
            if (location1 != null) {
                getVendorLocationWebService(location1);
            }
        } else {
            // } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                getVendorLocationWebService(location);
            }
        }*/
    }

    private void getVendorLocationWebService(LatLng location) {
        //if (fragmentType == FragmentType.OFFLINE) {
        if (isMove) {
            Log.d("volleyPostData", "getVendorLocationWebService + isMove");
            DashBoardActivity.distanceLatLng = new LatLng(location.latitude, location.longitude);
            Complete.offerDialogInstance().orderCompleted();
        }

        GetResponseFromServer.getWebService(getActivity(), TAG).getOnlineVendor(getActivity(), String.valueOf(location.latitude), String.valueOf(location.longitude), "online", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                urlResponse(response);
                Log.d("volleyPostData", "urlResponse ");
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
    // }//

    private void urlResponse(JSONObject response) throws JSONException {
        JSONArray jsonArrayVendor = response.getJSONObject("data").getJSONArray("online");
        listVendor.clear();
        for (int i = 0; i < jsonArrayVendor.length(); i++) {
            Vendor vendor = new Gson().fromJson(jsonArrayVendor.getJSONObject(i).toString(), Vendor.class);
            listVendor.add(vendor);
        }
//By Raja
        Log.d("volleyPostData", "urlResponse + ChangeLocationSingleton");
        ChangeLocationSingleton.getInstance().locationChanges(null, response.getJSONObject("data").getString("distance"), null);

        if (googleMap != null) {
            mapAddMarker.setGoogleMap(googleMap);
            mapAddMarker.addAllMarkLocationMap(listVendor);
//                            mapAddMarker.moveCircle(location);
        }
    }

    private void getMarkerMovedAddress(LatLng target) {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        if (geocoder.isPresent()) {
            try {
                DashBoardActivity.distanceLatLng = target;
                List<Address> addresses = geocoder.getFromLocation(target.latitude, target.longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    if (addresses.get(0).getSubLocality() != null && !addresses.get(0).getSubLocality().equalsIgnoreCase(""))
                        adres = addresses.get(0).getSubLocality();
                    else if (addresses.get(0).getLocality() != null && !addresses.get(0).getLocality().equalsIgnoreCase("")) {
                        adres = addresses.get(0).getLocality();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        Log.d("volleyPostData", "getMarkerMovedAddress +ChangeLocationSingleton");
        ChangeLocationSingleton.getInstance().locationChanges(null, null, adres);
        //Complete.offerDialogInstance().orderCompleted();
        Log.d(TAG, adres);
    }

    private void setVendorDetailsItem(Vendor vendor) {
        if (vendor != null) {
            vendorItemLinearLayout.setVisibility(View.VISIBLE);
            vendorItemBackgroundLinearLayout.setVisibility(View.VISIBLE);
            if (vendor.getLogo() != null)
                Picasso.with(getActivity()).load(vendor.getLogo()).into(vendorLogoCircleImageView);
            vendorNameTextView.setText(vendor.getName());
            /*if (vendor.getStar_rating() != null)
                vendorRatingBar.setRating(Float.parseFloat(vendor.getStar_rating()));
            else
                vendorRatingBar.setRating(0);
*/
            callPhone = vendor.getPhone_no();
            vendorId = vendor.getVendor_id();
            vendorItemBackgroundLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), VendorDetailActivity.class).putExtra("vendor_id", vendorId));
                }
            });
        }

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

    private void setMapView(GoogleMap googleMap) {
        this.googleMap = googleMap;
//        Log.d("volleyPostData", "setMapView");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstandValue.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                } else {
                    CommonMethods.toast(getActivity(), "My Location permission denied");
                }
                break;
        }
    }

    private boolean enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ConstandValue.MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            googleMap.setMyLocationEnabled(true);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        //getVendorLocation();
        super.onResume();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getVendorLocation();
        }
        if (isVisibleToUser && DashBoardActivity.distanceLatLng == null) {
            getMarkerMovedAddress(googleMap.getCameraPosition().target);
        } else if (getActivity() != null) {
            getMarkerMovedAddress(new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude()));
        }
    }
}
