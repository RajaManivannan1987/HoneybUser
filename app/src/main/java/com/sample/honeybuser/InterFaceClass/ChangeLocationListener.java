package com.sample.honeybuser.InterFaceClass;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by IM028 on 7/2/16.
 */
public interface ChangeLocationListener {
    void locationChanged(LatLng latLng, String distance, String address);
}
