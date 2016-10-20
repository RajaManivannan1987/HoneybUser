package com.sample.honeybuser.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.sample.honeybuser.Adapter.SearchViewPagerAdapter;
import com.sample.honeybuser.CommonActionBar.NavigationBarActivity;
import com.sample.honeybuser.EnumClass.FragmentType;
import com.sample.honeybuser.EnumClass.Selected;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;

/**
 * Created by Im033 on 10/16/2016.
 */

public class SearchActivity extends NavigationBarActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout searchTabLayout;
    private ViewPager searchViewPager;
    private TabLayout.Tab productTab, vemdorTab;
    private FragmentType fragmentType = FragmentType.PRODUCT;

    public void setFragmentType(FragmentType fragmentType) {
        this.fragmentType = fragmentType;
    }

    private void Product() {
        setFragmentType(FragmentType.PRODUCT);
    }

    private void Vendor() {
        setFragmentType(FragmentType.VENDOR);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_search);
        setSelected(Selected.SEARCH);
        setTitle("Search");
        searchTabLayout = (TabLayout) findViewById(R.id.searchTabLayout);
        searchViewPager = (ViewPager) findViewById(R.id.searchViewPager);
        productTab = searchTabLayout.newTab().setText(CommonMethods.getTabHeading(SearchActivity.this, FragmentType.PRODUCT, true));
        searchTabLayout.addTab(productTab);
        vemdorTab = searchTabLayout.newTab().setText(CommonMethods.getTabHeading(SearchActivity.this, FragmentType.VENDOR, false));
        searchTabLayout.addTab(vemdorTab);
        searchViewPager.setAdapter(new SearchViewPagerAdapter(getSupportFragmentManager(), searchTabLayout.getTabCount()));
        searchViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(searchTabLayout));
        searchViewPager.setCurrentItem(0);
        searchTabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        searchViewPager.setCurrentItem(tab.getPosition());
        switch (tab.getPosition()) {
            case 0:
                Product();
                productTab.setText(CommonMethods.getTabHeading(SearchActivity.this, FragmentType.PRODUCT, true));
                vemdorTab.setText(CommonMethods.getTabHeading(SearchActivity.this, FragmentType.VENDOR, false));
                break;
            case 1:
                Vendor();
                productTab.setText(CommonMethods.getTabHeading(SearchActivity.this, FragmentType.PRODUCT, false));
                vemdorTab.setText(CommonMethods.getTabHeading(SearchActivity.this, FragmentType.VENDOR, true));
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
