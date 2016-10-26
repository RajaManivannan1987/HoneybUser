package com.sample.honeybuser.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.sample.honeybuser.Adapter.OnLineVendorListAdapter;
import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.InterFaceClass.SaveCompletedInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.MapIntegration.MapAddMarker;
import com.sample.honeybuser.MapIntegration.MapUtils;
import com.sample.honeybuser.MapIntegration.TouchableWrapper;
import com.sample.honeybuser.Models.OnLineVendorListModel;
import com.sample.honeybuser.Models.Vendor;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private boolean flagIsOnTouched = true;
    private LatLng location = new LatLng(0d, 0d);
    private String adres = "";

    private List<OnLineVendorListModel> list = new ArrayList<OnLineVendorListModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_mapview, container, false);
        mapFrameLayout = (FrameLayout) view.findViewById(R.id.mapFrameLayout);
        touchFrameLayout = (TouchableWrapper) view.findViewById(R.id.onlineMapFragmentTouchableWrapper);
        mapView = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapFragment);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                setMapView(googleMap);
                enableMyLocation();

                mapAddMarker = new MapAddMarker(getActivity(), googleMap);

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        setVendorDetailsItem(getVendorByMarkerId(marker.getId()));
                        return true;
                    }
                });
                googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        isMove = true;
                    }
                });
                if (MyApplication.locationInstance() != null && MyApplication.locationInstance().getLocation() != null) {
                    location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 15);
                    flagIsOnTouched = false;
                    googleMap.animateCamera(yourLocation, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            flagIsOnTouched = true;
                        }

                        @Override
                        public void onCancel() {
                            flagIsOnTouched = true;
                        }
                    });
                    googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                        @Override
                        public void onCameraChange(CameraPosition cameraPosition) {
                            if (mapAddMarker != null) {
                                mapAddMarker.moveCircle(cameraPosition.target);
                                if (flagIsOnTouched) {
                                    getVendorLocation();
                                    getMarkerMovedAddress(cameraPosition.target);
                                }
                            }
                        }
                    });
                    googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {
                            location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, MapUtils.calculateZoomLevel(getActivity(), 15));
                            googleMap.animateCamera(yourLocation);
                            return false;

                        }
                    });
                }
            }
        });

        return view;
    }

    private void getVendorLocation() {

    }

    private void getMarkerMovedAddress(LatLng target) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        if (geocoder.isPresent()) {
            try {
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
        ChangeLocationSingleton.getInstance().locationChanges(null, null, adres);
    }

    private void setVendorDetailsItem(Vendor vendorByMarkerId) {

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
        super.onResume();

    }


}
