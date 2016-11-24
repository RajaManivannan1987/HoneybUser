package com.sample.honeybuser.Application;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sample.honeybuser.GCMClasses.RegistrationIntentService;
import com.sample.honeybuser.MapIntegration.LocationService;
import com.sample.honeybuser.MapIntegration.MyLocation;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Utility.Fonts.CustomViewWithTypefaceSupport;
import com.sample.honeybuser.Utility.Fonts.FontsOverride;
import com.sample.honeybuser.Utility.Fonts.TextField;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by IM028 on 4/20/16.
 **/
public class MyApplication extends android.support.multidex.MultiDexApplication {
    public static String deviceId = "";
    // by raja
    private static MyLocation location;
    private RequestQueue mRequestQueue;
    private static MyApplication sInstance;

    public static MyLocation locationInstance() {
        return location;
    }

    public static void instanceLocation(Context context) {
        location = new MyLocation(context);
    }

    public MyApplication() {

    }

    public static synchronized MyApplication getInstance() {
        return sInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-ThinItalic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .addCustomViewWithSetTypeface(CustomViewWithTypefaceSupport.class)
                .addCustomStyle(TextField.class, R.attr.textFieldStyle)
                .build()
        );
        instanceLocation(this);

        sInstance = this;
        MultiDex.install(this);

//        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/am_sams_r.otf");
//        FontsOverride.setDefaultFont(this, "SERIF", "fonts/rupee_foradian.ttf");
//        FontsOverride.setDefaultFont(this, "GOTHAMROUNDED", "fonts/gothamroundedbold_21016.ttf");
//        FontsOverride.setDefaultFont(this, "GOTHAMROUNDED", "fonts/gothamroundedbook_21018.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/rupee_foradian.ttf");

        // startService(new Intent(this, LocationService.class));
        startService(new Intent(this, RegistrationIntentService.class));
        ChangeLocationSingleton.getInstance().clearListener();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }
}
