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
import com.sample.honeybuser.EnumClass.FragmentType;
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
    public static boolean isVisibleView = false;
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
    Handler handler = new Handler();
    Runnable runnable;
    FragmentType fragmentType;


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
                flagIsOnTouched = false;
                isMove = false;
                //mapAddMarker.removeAll();
                hideVendorItem();
                handler.removeCallbacks(runnable);
//                CommonMethods.toast(getActivity(), "Loading vendors");
            }

            @Override
            public void onReleased() {
                flagIsOnTouched = true;
                if (isMove) {
                    //   By Raja on today 4.11.2016
                    DashBoardActivity.distanceLatLng = googleMap.getCameraPosition().target;
                    getMarkerMovedAddress(googleMap.getCameraPosition().target);
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getVendorLocation();
                                    Complete.offerDialogInstance().orderCompleted();
                                }
                            });
                        }
                    };
                    handler.postDelayed(runnable, 300);
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

                mapAddMarker = new MapAddMarker(getActivity(), googleMap);

                googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        setVendorDetailsItem(getVendorByMarkerId(marker.getId()));
                        return true;
                    }
                });
                googleMap.setOnCameraMoveListener(new OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        isMove = true;
                        if (mapAddMarker != null)
                            mapAddMarker.moveCircle(googleMap.getCameraPosition().target);
                    }
                });
                if (MyApplication.locationInstance() != null && MyApplication.locationInstance().getLocation() != null) {
                    location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 14);
                    googleMap.animateCamera(yourLocation);
                    flagIsOnTouched = false;
                    googleMap.animateCamera(yourLocation, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            flagIsOnTouched = true;
                        }

                        @Override
                        public void onCancel() {
                            flagIsOnTouched = false;
                            Log.d("MapViewFragment", "onCancel");
                        }
                    });
                    googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                        @Override
                        public void onCameraChange(CameraPosition cameraPosition) {
//                            DashBoardActivity.distanceLatLng = googleMap.getCameraPosition().target;
                            getMarkerMovedAddress(googleMap.getCameraPosition().target);
                        }
                    });
                    googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {
                            location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, MapUtils.calculateZoomLevel(getActivity(), 15));
                            googleMap.animateCamera(yourLocation);
                            //Now
//                            DashBoardActivity.distanceLatLng = googleMap.getCameraPosition().target;
                            Complete.getGetMapList().orderCompleted();
                            //getVendorLocation();
                            Log.d("MapViewFragment", "onMyLocationButtonClick");
                            return false;

                        }
                    });
                }
            }
        });
        ChangeLocationSingleton.getInstance().setChangeLocationListener(new ChangeLocationListener() {
            @Override
            public void locationChanged(LatLng latLng, String distance, String address, String classType) {
                Log.d("MapViewFragment", "OnLineMapFragment " + classType);
                if (latLng != null) {
                    if (googleMap != null) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
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
        Log.d("MapViewFragment", "getVendorLocation");

        if (DashBoardActivity.distanceLatLng != null) {
            final LatLng location1 = DashBoardActivity.distanceLatLng;
//            LatLng location1 = googleMap.getCameraPosition().target;
            if (location1 != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getVendorLocationWebService(location1);
                    }
                });
                ChangeLocationSingleton.getInstance().locationChanges(location1, null, null, "OnLineMapFragment");

            }
        } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                location = new LatLng(MyApplication.locationInstance().getLocation().getLatitude(), MyApplication.locationInstance().getLocation().getLongitude());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getVendorLocationWebService(location);
                    }
                });

                ChangeLocationSingleton.getInstance().locationChanges(location, null, null, "OnLineMapFragment");
                Log.d("OnLineMapFragment", "getVendorLocation +location ");
            }
        }
    }

    private void getVendorLocationWebService(final LatLng loc) {
        Log.d("MapViewFragment", "getVendorLocationWebService");
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
        JSONArray jsonArrayVendor = response.getJSONObject("data").getJSONArray("vendors");
        listVendor.clear();

        for (int i = 0; i < jsonArrayVendor.length(); i++) {
            Vendor vendor = new Gson().fromJson(jsonArrayVendor.getJSONObject(i).toString(), Vendor.class);
            listVendor.add(vendor);
        }
        if (jsonArrayVendor.length() == 0) {
            CommonMethods.toast(getActivity(), response.getJSONObject("data").getString("message"));
        }
        OnLineMapFragment.this.distance = response.getJSONObject("data").getString("distance");
        //By Raja
        Log.d("volleyPostData", response.getJSONObject("data").getString("distance"));

        ChangeLocationSingleton.getInstance().locationChanges(null, response.getJSONObject("data").getString("distance"), null, "OnLineMap");

        if (googleMap != null) {
            mapAddMarker.setGoogleMap(googleMap);
            mapAddMarker.addAllMarkLocationMap(listVendor);
//            mapAddMarker.moveCircle(location);
        }
    }

    private void getMarkerMovedAddress(LatLng target) {
        adres = CommonMethods.getAddressName(getActivity(), target, TAG);
        // today
        ChangeLocationSingleton.getInstance().locationChanges(null, null, adres, "OnLineMapFragment");
    }

    private void setVendorDetailsItem(final Vendor vendor) {
        if (vendor != null) {
            vendorItemLinearLayout.setVisibility(View.VISIBLE);
            //vendorItemBackgroundCardView.setVisibility(View.VISIBLE);
            callPhone = vendor.getPhone_no();
            vendorId = vendor.getVendor_id();
            vendorNameTextView.setText(vendor.getName());
            onlineMapKmTextView.setText(vendor.getDistance());

            if (vendor.getNew_vendor().startsWith("Y")) {
                onlineMapratingImageView.setImageResource(R.drawable.new_icon);
                onlineMapRatingTextView.setVisibility(View.GONE);
                onlineMapRatingCountTextView.setVisibility(View.GONE);
            } else {
                onlineMapratingImageView.setImageResource(R.drawable.star);
                onlineMapRatingTextView.setText(vendor.getStar_rating());
                onlineMapRatingCountTextView.setText("(" + vendor.getRating_count() + " Ratings)");
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
                    CommonWebserviceMethods.getVendorLocation(getActivity(), TAG, vendorId, vendor.getName());
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
                    CommonMethods.commonBundleIntent(getActivity(), VendorDetailActivity.class, vendorId, vendorItemBackgroundCardView);
//                    startActivity(new Intent(getActivity(), VendorDetailActivity.class).putExtra("vendor_id", vendorId).putExtra("notificationType", ""));
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
                        AlertDialogManager.listenerDialogBox(getActivity(), "", "Disable Alert for this vendor", new DialogBoxInterface() {
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
//        getVendorLocation();
/*        if (isVisibleView) {
//            if (FragmentType.OFFLINE==OnLineMapFragment.isVisibleView)
            getVendorLocation();
            Log.d("volleyPostData", "onResume");
            isVisibleView = false;*/
//        }
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

    public static Fragment mapInstance() {
        OnLineMapFragment fragment = new OnLineMapFragment();
        fragment.setFragmentType(FragmentType.OFFLINE);
        return fragment;
    }

    private void setFragmentType(FragmentType offline) {
        this.fragmentType = offline;
    }
}