package com.sample.honeybuser.Singleton;


import com.google.android.gms.maps.model.LatLng;
import com.sample.honeybuser.InterFaceClass.ChangeLocationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IM028 on 7/2/16.
 */
public class ChangeLocationSingleton {
    private List<ChangeLocationListener> changeLocationListeners = new ArrayList<ChangeLocationListener>();

    private static ChangeLocationSingleton changeLocationSingleton = new ChangeLocationSingleton();

    public static ChangeLocationSingleton getInstance() {
        return changeLocationSingleton;
    }

    public void clearListener() {
        changeLocationListeners.clear();
    }

    public void setChangeLocationListener(ChangeLocationListener changeLocationListener) {
        this.changeLocationListeners.add(changeLocationListener);
    }

    public void locationChanges(LatLng latLng, String distance, String address) {
        for (ChangeLocationListener c : changeLocationListeners) {
            c.locationChanged(latLng, distance, address);
        }
    }

}
