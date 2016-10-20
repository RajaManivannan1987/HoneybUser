package com.sample.honeybuser.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sample.honeybuser.Fragment.ProductSearchFragment;
import com.sample.honeybuser.Fragment.VendorSearchFragment;

/**
 * Created by Im033 on 10/18/2016.
 */

public class SearchViewPagerAdapter extends FragmentStatePagerAdapter {
    int pageCount;

    public SearchViewPagerAdapter(FragmentManager fm, int tabcount) {
        super(fm);
        this.pageCount = tabcount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProductSearchFragment();
            case 1:
                return new VendorSearchFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return pageCount;
    }
}
