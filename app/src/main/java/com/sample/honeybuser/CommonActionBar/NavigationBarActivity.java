package com.sample.honeybuser.CommonActionBar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.sample.honeybuser.Activity.ChangeLocationActivity;
import com.sample.honeybuser.Activity.DashBoardActivity;
import com.sample.honeybuser.Activity.FollowerActivity;
import com.sample.honeybuser.Activity.SearchActivity;
import com.sample.honeybuser.Activity.SettingsActivity;
import com.sample.honeybuser.Adapter.NavigationBarAdapter;
import com.sample.honeybuser.EnumClass.IntentClasses;
import com.sample.honeybuser.EnumClass.Selected;
import com.sample.honeybuser.InterFaceClass.ChangeLocationListener;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.Sharedpreferences.Session;

/**
 * Created by IM0033 on 10/5/2016.
 */

public class NavigationBarActivity extends AppCompatActivity {
    private String TAG = "NavigationBarActivity";
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private LinearLayout actionBarViewLocationLinearLayout;
    private ImageView menuImageView, searchImageView;
    private NavigationBarAdapter navigationBarAdapter;
    private Toolbar toolbar;
    private View headerSection, footerSection;
    public static TextView distanceTextView;
    //    public static TextView distanceTextView1;
    public TextView locationTextView;
    public TextView titleTextView;
    public TextView UserNameTextView;

    //public static String locationName;
    private Selected selected;
    private ImageView tab1, tab2, tab3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationbar);
        toolbar = (Toolbar) findViewById(R.id.actionBarViewToolBar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.actionBarViewDrawerLayout);
        drawerList = (ListView) findViewById(R.id.actionBarViewListView);
        headerSection = getLayoutInflater().inflate(R.layout.menu_header, null, false);
        footerSection = getLayoutInflater().inflate(R.layout.menu_footer, null, false);
        UserNameTextView = (TextView) headerSection.findViewById(R.id.usernameEditext);
        //distanceTextView = (TextView) findViewById(R.id.actionBarViewDistanceTextView);
        distanceTextView = (TextView) findViewById(R.id.actionBarViewDistanceTextView);
        locationTextView = (TextView) findViewById(R.id.actionBarViewPlaceTextView);
        titleTextView = (TextView) findViewById(R.id.actionBarViewTitleTextView);
        searchImageView = (ImageView) findViewById(R.id.actionBarViewSearchImageView);
        actionBarViewLocationLinearLayout = (LinearLayout) findViewById(R.id.actionBarViewLocationLinearLayout);
        tab1 = (ImageView) findViewById(R.id.tab1_icon);
        tab2 = (ImageView) findViewById(R.id.tab2_icon);
        tab3 = (ImageView) findViewById(R.id.tab3_icon);

        // set navigation list
        menuImageView = (ImageView) findViewById(R.id.actionBarViewMenuImageView);
        navigationBarAdapter = new NavigationBarAdapter(NavigationBarActivity.this);
        drawerList.addHeaderView(headerSection);
        drawerList.addFooterView(footerSection);
        drawerList.setAdapter(navigationBarAdapter);
        drawerList.setAnimation(new AnimationSet(true));
        UserNameTextView.setText(Session.getSession(NavigationBarActivity.this, TAG).getName());

        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Selected.DASHBOARD != selected) {
                    CommonMethods.commonIntent(NavigationBarActivity.this, IntentClasses.DASHBOARD);
                }
            }
        });
        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Selected.FOLLOWER != selected) {
                    CommonMethods.commonIntent(NavigationBarActivity.this, IntentClasses.FOLLOWER);
                }
            }
        });
        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Selected.SEARCH != selected) {
                    CommonMethods.commonIntent(NavigationBarActivity.this, IntentClasses.SEARCH);
                }
            }
        });
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0 && position != (drawerList.getCount() - 1)) {
                    TextView nameTextView = (TextView) view.findViewById(R.id.navigationTextView);
                    switch (nameTextView.getText().toString().toLowerCase().trim()) {
                        case "street":
                            CommonMethods.commonIntent(NavigationBarActivity.this, IntentClasses.DASHBOARD);
                            break;
                        case "alerts":
                            CommonMethods.commonIntent(NavigationBarActivity.this, IntentClasses.FOLLOWER);
                            break;
                        case "search":
                            CommonMethods.commonIntent(NavigationBarActivity.this, IntentClasses.SEARCH);
                            break;
                        case "settings":
                            CommonMethods.commonIntent(NavigationBarActivity.this, IntentClasses.SETTINGS);
                            break;

                        default:
                            break;
                    }
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    }
                }
            }
        });
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(Gravity.LEFT))
                    drawerLayout.openDrawer(Gravity.LEFT);
                else
                    drawerLayout.closeDrawer(Gravity.LEFT);
                navigationBarAdapter.notifyDataSetChanged();

            }
        });
        actionBarViewLocationLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavigationBarActivity.this, ChangeLocationActivity.class));
            }
        });

    }

    public void setSelected(Selected selected) {
        this.selected = selected;
        int textcolor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textcolor = getResources().getColor(R.color.colorPrimary, getTheme());
        } else {
            textcolor = getResources().getColor(R.color.colorPrimary);
        }
        if (selected == Selected.DASHBOARD) {
            ChangeLocationSingleton.getInstance().setChangeLocationListener(new ChangeLocationListener() {
                @Override
                public void locationChanged(LatLng latLng, String distance, String address, String classType) {
                    Log.d("OnLineMapFragment", "NavigationBar " + classType);

//                     By Raja 4.11.16
//                    set distance in dashboard page

//                    if (distance != null && !distance.equalsIgnoreCase("")) {
//                        distanceTextView.setText(" " + distance + " km ");
//                    }
                    if (address != null && !address.equalsIgnoreCase("")) {
                        locationTextView.setText(address);
                    }
                }
            });
        }
        switch (selected) {
            case DASHBOARD:
                tab1.setImageResource(R.drawable.street);
                break;
            case FOLLOWER:
                tab2.setImageResource(R.drawable.bell);
                break;
            case SEARCH:
                tab3.setImageResource(R.drawable.search);
        }
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
        titleTextView.setVisibility(View.VISIBLE);
        searchImageView.setVisibility(View.INVISIBLE);
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setView(int viewLayout) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.actionBarViewFrameLayout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(viewLayout, null, false);
        frameLayout.addView(activityView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
