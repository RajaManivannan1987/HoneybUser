package com.sample.honeybuser.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.sample.honeybuser.Adapter.SearchViewPagerAdapter;
import com.sample.honeybuser.CommonActionBar.NavigationBarActivity;
import com.sample.honeybuser.EnumClass.Selected;
import com.sample.honeybuser.R;

/**
 * Created by Im033 on 10/16/2016.
 */

public class SearchActivity extends NavigationBarActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout searchTabLayout;
    private ViewPager searchViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_search);
        setSelected(Selected.SEARCH);
        setTitle("Search");
        searchTabLayout = (TabLayout) findViewById(R.id.searchTabLayout);
        searchViewPager = (ViewPager) findViewById(R.id.searchViewPager);
        searchTabLayout.addTab(searchTabLayout.newTab().setText("Vendor"));
        searchTabLayout.addTab(searchTabLayout.newTab().setText("Product"));
        searchViewPager.setAdapter(new SearchViewPagerAdapter(getSupportFragmentManager(), searchTabLayout.getTabCount()));
        searchViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(searchTabLayout));
        searchViewPager.setCurrentItem(0);
        searchTabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        searchViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
