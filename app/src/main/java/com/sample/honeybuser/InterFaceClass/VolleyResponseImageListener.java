package com.sample.honeybuser.InterFaceClass;

import android.graphics.Bitmap;

/**
 * Created by IM028 on 5/4/16.
 */
public interface VolleyResponseImageListener {
    void onResponse(Bitmap response);

    void onError(String message, String title);
}
