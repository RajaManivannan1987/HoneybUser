package com.sample.honeybuser.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.InterFaceClass.TimerInterface;
import com.sample.honeybuser.MapIntegration.LocationServiceUpdated;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.ScheduleThread;
import com.sample.honeybuser.Utility.Fonts.Sharedpreferences.Session;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;


public class LocationCheckActivity extends AppCompatActivity {
    private String TAG = "LocationCheckActivity";
    private LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private final int MY_LOCATION = 1;
    private ScheduleThread thread;
    private boolean isPassed = false;
    private String notificationType = "", id = "", channelUrl = "";
    private final String redeemOffers = "redeem_offer";
    private final String confirmOrder = "confirm_order";
    private final String cancelOrder = "cancel_order";
    private final String messageToFollowers = "message_to_followers";
    private final String vendorAlert = "vendor_alert";
    private final String message = "message";
    private final String offer = "new_offer";
    private int count = 10, iteration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_check);
        try {
            if (getIntent().getExtras() != null) {
                notificationType = getIntent().getExtras().getString(ConstandValue.notificationType, "");
                id = getIntent().getExtras().getString(ConstandValue.vendorId, "");
                channelUrl = getIntent().getExtras().getString(ConstandValue.channelUrl, "");
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        thread = new ScheduleThread(new TimerInterface() {
            @Override
            public void onRun() {
                Log.d(TAG, "Inside Thread");
                if (gps_enabled && network_enabled && MyApplication.locationInstance() != null && MyApplication.locationInstance().getLocation() != null) {
                    Log.d(TAG, "Inside Thread Success");
                    thread.stop();
                    if (!isPassed) {
                        switch (notificationType) {
                            case "":
                                startActivity(new Intent(LocationCheckActivity.this, DashBoardActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                                break;
                            case redeemOffers:
//                                startActivity(new Intent(LocationCheckActivity.this, OffersActivity.class).putExtra(ConstantValues.selectedTap, 2));
                                break;
                            case confirmOrder:
//                                startActivity(new Intent(LocationCheckActivity.this, OrderDetails1Activity.class).putExtra(ConstantValues.transactionId, id));
                                break;
                            case cancelOrder:
//                                startActivity(new Intent(LocationCheckActivity.this, OrderDetails1Activity.class).putExtra(ConstantValues.transactionId, id));
                                break;
                            case messageToFollowers:
//                                startActivity(new Intent(LocationCheckActivity.this, VendorActivity.class).putExtra(ConstantValues.vendorId, id).putExtra(ConstantValues.isFromNotification, true));
                                break;
                            case vendorAlert:
//                                startActivity(new Intent(LocationCheckActivity.this, VendorActivity.class).putExtra(ConstantValues.vendorId, id).putExtra(ConstantValues.isFromNotification, true));
                                break;
                            case message:
//                                startActivity(new Intent(LocationCheckActivity.this, SendBirdMessagingActivity.class).putExtras(SendBirdMessagingActivity.makeMessagingJoinArgsDefault(LocationCheckActivity.this, channelUrl)));
                                break;
                            case offer:
//                                startActivity(new Intent(LocationCheckActivity.this, OffersActivity.class));
                                break;
                            default:
                                startActivity(new Intent(LocationCheckActivity.this, DashBoardActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                                break;
                        }
                        finish();
                        isPassed = true;
                        stopService(new Intent(LocationCheckActivity.this, LocationServiceUpdated.class));
                        startService(new Intent(LocationCheckActivity.this, LocationServiceUpdated.class));
                    }
                } else {
                    iteration++;
                    Log.d(TAG, "Inside Thread Failed");
                    if (gps_enabled && network_enabled && iteration == count) {
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(LocationCheckActivity.this);
                            dialog.setMessage("Seems like GPS is turned off. Please enable GPS access and then open this App.");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(myIntent);
                                    finish();
                                }
                            });
                            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    finish();
                                }
                            });
                            if (dialog != null) {
                                dialog.show();
                            }
                        } else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(LocationCheckActivity.this);
                            dialog.setMessage("Seems getting your location takes a while.\n" + "Would you like to try later?");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    finish();
                                }
                            });
                            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    iteration = 0;
                                }
                            });
                            if (dialog != null) {
                                dialog.show();
                            }
                        }
                    }
                }
            }
        }, 2000, 0, this);
        thread.start();
    }

    private void enableLocation() {
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        Log.d(TAG, "GPS " + gps_enabled + " Network " + network_enabled);
        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Enable Location Services");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();
                }
            });
            dialog.show();
        } else {
            enableMyLocation();
        }

    }

    private boolean enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationCheckActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION);
        } else {
            MyApplication.instanceLocation(LocationCheckActivity.this);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation();
                } else {
                    thread.stop();
                    CommonMethods.toast(LocationCheckActivity.this, "My Location permission denied");
                    Session.getSession(LocationCheckActivity.this, TAG).clearSession();
                    stopService(new Intent(LocationCheckActivity.this, LocationServiceUpdated.class));
                    startActivity(new Intent(LocationCheckActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                    finish();
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        enableLocation();
    }
}
