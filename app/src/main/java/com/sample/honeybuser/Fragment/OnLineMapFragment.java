package com.sample.honeybuser.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.sample.honeybuser.InterFaceClass.DialogBoxInterface;
import com.sample.honeybuser.InterFaceClass.SaveCompletedInterface;
import com.sample.honeybuser.InterFaceClass.TouchInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.MapIntegration.MapAddMarker;
import com.sample.honeybuser.MapIntegration.MapUtils;
import com.sample.honeybuser.MapIntegration.TouchableWrapper;
import com.sample.honeybuser.Models.Vendor;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.AlertDialogManager;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.CommonWebserviceMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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
    private LinearLayout vendorItemLinearLayout;
    private CardView vendorItemBackgroundCardView;
    private RelativeLayout mapRelativeLayout;
    private TextView vendorNameTextView, onlineMapRatingTextView, onlineMapRatingCountTextView, onlineMapKmTextView;
    private CircleImageView vendorLogoCircleImageView;
    private ImageView onlineMapnotifyImage, onlineMapcallImage, onlineMaplocateImage, onlineMapratingImageView;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_mapview, container, false);
        mapFrameLayout = (FrameLayout) view.findViewById(R.id.mapFrameLayout);
        touchFrameLayout = (TouchableWrapper) view.findViewById(R.id.onlineMapFragmentTouchableWrapper);
        mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        vendorItemLinearLayout = (LinearLayout) view.findViewById(R.id.mobileStoreFragmentVendorbackGroundLinearLayout);
        vendorItemBackgroundCardView = (CardView) view.findViewById(R.id.onlineMapCardView);
        mapRelativeLayout = (RelativeLayout) view.findViewById(R.id.mobileStoreFragmentLocationMapRelativeLayout);
        vendorNameTextView = (TextView) view.findViewById(R.id.onlineMapNameTextView);
        vendorLogoCircleImageView = (CircleImageView) view.findViewById(R.id.onlineMap_profile_image);
        onlineMapnotifyImage = (ImageView) view.findViewById(R.id.onlineMapnotifyImage);
        onlineMapcallImage = (ImageView) view.findViewById(R.id.onlineMapcallImage);
        onlineMaplocateImage = (ImageView) view.findViewById(R.id.onlineMaplocateImage);
        onlineMapratingImageView = (ImageView) view.findViewById(R.id.onlineMapratingImageView);
        onlineMapRatingTextView = (TextView) view.findViewById(R.id.onlineMapRatingTextView);
        onlineMapRatingCountTextView = (TextView) view.findViewById(R.id.onlineMapRatingCountTextView);
        onlineMapKmTextView = (TextView) view.findViewById(R.id.onlineMapKmTextView);
        Complete.getGetMapList().setListener(new SaveCompletedInterface() {
            @Override
            public void completed() {
                getVendorLocation();
            }
        });

        touchFrameLayout.setTouchInterface(new TouchInterface() {
            @Override
            public void onPressed() {
                if (timer != null) {
                    timer.cancel();
                }
                flagIsOnTouched = false;
                isMove = false;
                mapAddMarker.removeAll();
                hideVendorItem();
            }

            @Override
            public void onReleased() {
                flagIsOnTouched = true;
                if (isMove) {
                    DashBoardActivity.distanceLatLng = googleMap.getCameraPosition().target;
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getVendorLocation();
                                    getMarkerMovedAddress(googleMap.getCameraPosition().target);
                                    Complete.offerDialogInstance().orderCompleted();
                                }
                            });

                        }
                    };
                    timer = new Timer();
                    timer.schedule(timerTask, 01, 8000);

                    //   By Raja on today 3.11.2016

//
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            getVendorLocation();
//                        }
//                    }, 5);
//

//                    Complete.offerDialogInstance().orderCompleted();
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
                Log.d("OnLineMapFragment", "onMapReady");
                mapAddMarker = new MapAddMarker(getActivity(), googleMap);

                googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        setVendorDetailsItem(getVendorByMarkerId(marker.getId()));
                        Log.d("OnLineMapFragment", "setOnMarkerClickListener");
                        return true;
                    }
                });
                googleMap.setOnCameraMoveListener(new OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        isMove = true;
                    }
                });
                if (MyApplication.locationInstance() != null && MyApplication.locationInstance().getLocation() != null) {
                    Log.d("OnLineMapFragment", "MylocationInstance");
                    location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 14);
                    googleMap.animateCamera(yourLocation);
                    flagIsOnTouched = false;
                    googleMap.animateCamera(yourLocation, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            flagIsOnTouched = true;
                            Log.d("OnLineMapFragment", "onFinish");
                        }

                        @Override
                        public void onCancel() {
                            flagIsOnTouched = false;
                            Log.d("OnLineMapFragment", "onCancel");
                        }
                    });
                    googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                        @Override
                        public void onCameraChange(CameraPosition cameraPosition) {
                            if (mapAddMarker != null)
                                mapAddMarker.moveCircle(cameraPosition.target);
                            if (flagIsOnTouched) {
                                // getVendorLocation();
                                // getMarkerMovedAddress(cameraPosition.target);
                                Log.d("OnLineMapFragment", "onCameraChange");
                            }
                        }
                    });
                    googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {
                            location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, MapUtils.calculateZoomLevel(getActivity(), 15));
                            googleMap.animateCamera(yourLocation);
                            //getVendorLocation();
                            Log.d("OnLineMapFragment", "onMyLocationButtonClick");
                            return false;

                        }
                    });
                }
            }
        });
        ChangeLocationSingleton.getInstance().setChangeLocationListener(new ChangeLocationListener() {
            @Override
            public void locationChanged(LatLng latLng, String distance, String address, String classType) {
                Log.d("OnLineMapFragment", classType);
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
        //mapAddMarker.removeAll();
        vendorItemLinearLayout.setVisibility(View.GONE);
        //vendorItemBackgroundCardView.setVisibility(View.GONE);
    }

    private void getVendorLocation() {
        Log.d("OnLineMapFragment", "getVendorLocation");

        if (DashBoardActivity.distanceLatLng != null) {
            LatLng location1 = DashBoardActivity.distanceLatLng;
            if (location1 != null) {
                Log.d("OnLineMapFragment", "getVendorLocation +location1 ");
                getVendorLocationWebService(location1);
                ChangeLocationSingleton.getInstance().locationChanges(location1, null, null, "OnLineMapFragment");

            }
        } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                getVendorLocationWebService(location);
                ChangeLocationSingleton.getInstance().locationChanges(location, null, null, "OnLineMapFragment");
                Log.d("OnLineMapFragment", "getVendorLocation +location ");
            }
        }
    }

    private void getVendorLocationWebService(final LatLng loc) {
        GetResponseFromServer.getWebService(getActivity(), TAG).getOnlineVendor(getActivity(), String.valueOf(loc.latitude), String.valueOf(loc.longitude), "online", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                urlResponse(response);
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }


    private void urlResponse(JSONObject response) throws JSONException {
        Log.d(TAG, response.toString());
        JSONArray jsonArrayVendor = response.getJSONObject("data").getJSONArray("online");
        listVendor.clear();
        for (int i = 0; i < jsonArrayVendor.length(); i++) {
            Vendor vendor = new Gson().fromJson(jsonArrayVendor.getJSONObject(i).toString(), Vendor.class);
            listVendor.add(vendor);
        }
        OnLineMapFragment.this.distance = response.getJSONObject("data").getString("distance");
        //By Raja
        ChangeLocationSingleton.getInstance().locationChanges(null, response.getJSONObject("data").getString("distance"), null, "OnLineMap");

        if (googleMap != null) {
            mapAddMarker.setGoogleMap(googleMap);
            mapAddMarker.addAllMarkLocationMap(listVendor);
//            mapAddMarker.moveCircle(location);
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
        Log.d("OnLineMapFragment", "getMarkerMovedAddress +ChangeLocationSingleton");
        ChangeLocationSingleton.getInstance().locationChanges(null, null, adres, "OnLineMapFragment");
        Log.d(TAG, adres);
    }

    private void setVendorDetailsItem(final Vendor vendor) {
        if (vendor != null) {
            vendorItemLinearLayout.setVisibility(View.VISIBLE);
            //vendorItemBackgroundCardView.setVisibility(View.VISIBLE);
            callPhone = vendor.getPhone_no();
            vendorId = vendor.getVendor_id();
            vendorNameTextView.setText(vendor.getName());
            onlineMapKmTextView.setText(vendor.getDistance() + " Km away");

            if (vendor.getNew_vendor().startsWith("Y")) {
                onlineMapratingImageView.setImageResource(R.drawable.new_icon);
                onlineMapRatingTextView.setVisibility(View.GONE);
                onlineMapRatingCountTextView.setVisibility(View.GONE);
            } else {
                onlineMapratingImageView.setImageResource(R.drawable.star);
                onlineMapRatingTextView.setText(vendor.getStar_rating());
                onlineMapRatingCountTextView.setText(vendor.getRating_count() + " Ratings)");
            }
            if (vendor.getFollow().equalsIgnoreCase("Y")) {
                onlineMapnotifyImage.setImageResource(R.drawable.notify);
            } else {
                onlineMapnotifyImage.setImageResource(R.drawable.nonotify);
            }
            if (vendor.getLogo() != null) {
                Picasso.with(getActivity()).load(vendor.getLogo()).into(vendorLogoCircleImageView);
            } else {
                vendorLogoCircleImageView.setImageResource(R.drawable.no_image);
            }

            onlineMaplocateImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonWebserviceMethods.getVendorLocation(getActivity(), TAG, vendorId);
                }
            });
            onlineMapcallImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonMethods.callFunction(getActivity(), callPhone);
                }
            });
            vendorItemBackgroundCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), VendorDetailActivity.class).putExtra("vendor_id", vendorId));
                    vendorItemLinearLayout.setVisibility(View.GONE);
                }
            });
            onlineMapnotifyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (vendor.getFollow().equalsIgnoreCase("N")) {
                        CommonWebserviceMethods.setFollows(getActivity(), TAG, vendor.getVendor_id(), "6");
                        vendorItemLinearLayout.setVisibility(View.GONE);
                    } else {
                        AlertDialogManager.listenerDialogBox(getActivity(), "Remove!", "Remove alert?", new DialogBoxInterface() {
                            @Override
                            public void yes() {
                                CommonWebserviceMethods.removeFollows(getActivity(), TAG, vendor.getVendor_id(), "6");
                                vendorItemLinearLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void no() {

                            }
                        });

                    }

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getVendorLocation();
        }
//        if (isVisibleToUser && DashBoardActivity.distanceLatLng == null) {
//            getMarkerMovedAddress(googleMap.getCameraPosition().target);
//        } else if (getActivity() != null) {
//            getMarkerMovedAddress(new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude()));
//        }
    }
}