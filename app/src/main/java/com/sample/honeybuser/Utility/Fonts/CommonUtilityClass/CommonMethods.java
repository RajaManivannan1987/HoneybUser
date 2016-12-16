package com.sample.honeybuser.Utility.Fonts.CommonUtilityClass;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.sample.honeybuser.Activity.DashBoardActivity;
import com.sample.honeybuser.Activity.FollowerActivity;
import com.sample.honeybuser.Activity.HelperActivity;
import com.sample.honeybuser.Activity.LocationCheckActivity;
import com.sample.honeybuser.Activity.LoginActivity;
import com.sample.honeybuser.Activity.OTPActivity;
import com.sample.honeybuser.Activity.RegistrationActivity;
import com.sample.honeybuser.Activity.SearchActivity;
import com.sample.honeybuser.Activity.SettingsActivity;
import com.sample.honeybuser.Activity.SplashScreen;
import com.sample.honeybuser.Activity.VendorDetailActivity;
import com.sample.honeybuser.Adapter.BusinessVendorAdapter;
import com.sample.honeybuser.Adapter.OnLineVendorListAdapter;
import com.sample.honeybuser.EnumClass.FragmentType;
import com.sample.honeybuser.EnumClass.IntentClasses;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Utility.Fonts.CustomTypefaceSpan;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Created by IM0033 on 8/2/2016.
 */
public class CommonMethods extends AppCompatActivity {

    public static void commonIntent(Activity context, IntentClasses intentValue) {
        Serializable act = null;
        switch (intentValue) {
            case HELPER:
                act = HelperActivity.class;
                break;
            case SPLASH:
                act = SplashScreen.class;
                break;
            case LOGIN:
                act = LoginActivity.class;
                break;
            case REGISTRATION:
                act = RegistrationActivity.class;
                break;
            case OTP:
                act = OTPActivity.class;
                break;
            case FOLLOWER:
                act = FollowerActivity.class;
                break;
            case DASHBOARD:
                act = DashBoardActivity.class;
                break;
            case LOCATIONCHECK:
                act = LocationCheckActivity.class;
                break;
            case SEARCH:
                act = SearchActivity.class;
                break;
            case SETTINGS:
                act = SettingsActivity.class;
                break;
        }
        context.startActivity(new Intent(context, (Class<?>) act).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
    }

    public static void commonBundleIntent(Context context, Class<?> a, String vendorId, CardView holder) {
        Intent intent = new Intent(context, a);
        intent.putExtra(ConstandValue.vendorId, vendorId);
        intent.putExtra("notificationType", "");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder, ConstandValue.transitionName);
        context.startActivity(intent, options.toBundle());
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void locationDirection(Context context, String lat, String lang) {
        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?&daddr=" + lat + "," + lang);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }

    public static void callFunction(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }


    public static boolean checkProvider(Activity context) {
        boolean isGps = false, isNetwork = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGps && !isNetwork) {
            return false;
        } else {
            return true;
        }
    }

    public static void showLocationAlert(final Context context) {
        String message = "To select/set area, enable location services";
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Location!");
        dialog.setMessage(message);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                listener.yes();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
                dialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                listener.no();
                dialogInterface.dismiss();
            }
        });
        dialog.create().show();
    }

    public static boolean checkLocationPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ConstandValue.MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ConstandValue.MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public static CharSequence getTabHeading(Context context, FragmentType fragmentType, boolean isSelected) {
        String text = "";
        Drawable image = null;
        switch (fragmentType) {
            case ONLINE:
                text = "";
                image = (isSelected) ? ContextCompat.getDrawable(context, R.drawable.listtabon) : ContextCompat.getDrawable(context, R.drawable.listtaboff);
                break;
            case OFFLINE:
                text = "";
                image = (isSelected) ? ContextCompat.getDrawable(context, R.drawable.maptabon) : ContextCompat.getDrawable(context, R.drawable.maptaboff);
                break;
            case PRODUCT:
                text = "Products";
                image = (isSelected) ? ContextCompat.getDrawable(context, R.drawable.products1) : ContextCompat.getDrawable(context, R.drawable.productsblk1);
                break;
            case VENDOR:
                text = "Vendors";
                image = (isSelected) ? ContextCompat.getDrawable(context, R.drawable.vendor) : ContextCompat.getDrawable(context, R.drawable.vendorblk);
                break;

        }
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   " + text);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new CustomTypefaceSpan("", Typeface.MONOSPACE), 3, text.length() + 3, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        sb.setSpan(new AbsoluteSizeSpan((int) context.getResources().getDimension(R.dimen.text_sixe_normal)), 3, text.length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//resize size
        return sb;
    }

    public static String getAddressName(Context context, LatLng target, String Tag) {
        String address = "", subAddress = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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
                Log.e(Tag, e.getMessage());
            }
        }
//        ChangeLocationSingleton.getInstance().locationChanges(null, null, address + ", " + subAddress, "VendorListFragment");
        return subAddress + ", " + address;
    }

    public static void checkPermision(Activity activity, String permision, int requestCode) {
        if (ActivityCompat.checkSelfPermission(activity, permision) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permision}, requestCode);
        }
    }
}

