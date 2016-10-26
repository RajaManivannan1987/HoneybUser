package com.sample.honeybuser.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sample.honeybuser.Fragment.OnLineMapFragment;
import com.sample.honeybuser.Fragment.VendorListFragment;

/**
 * Created by IM0033 on 10/6/2016.
 */
public class DashBoardViewPagerAdapter extends FragmentStatePagerAdapter {
    int pageCount;
    String lat, lang;

    public DashBoardViewPagerAdapter(FragmentManager fm, int tabCount, String lat, String lang) {
        super(fm);
        this.pageCount = tabCount;
        this.lat = lat;
        this.lang = lang;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        Bundle bundle = new Bundle();
        bundle.putString("lat", lat);
        bundle.putString("lang", lang);
        switch (position) {
            case 0:
                fragment = new VendorListFragment();
                fragment.setArguments(bundle);
                break;

            case 1:
                fragment = new OnLineMapFragment();
                fragment.setArguments(bundle);
                break;
            default:
                fragment = null;
                break;
        }
        return fragment;
    }
}
