package com.sample.honeybuser.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sample.honeybuser.Adapter.RatingsAdapter;
import com.sample.honeybuser.CommonActionBar.CommonActionBar;
import com.sample.honeybuser.InterFaceClass.DialogBoxInterface;
import com.sample.honeybuser.InterFaceClass.SaveCompletedInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Models.Ratings;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.AlertDialogManager;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.CommonWebserviceMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Im033 on 10/14/2016.
 */

public class VendorDetailActivity extends CommonActionBar {
    private String TAG = "VendorDetailActivity";
    private ImageView bannerImageView, vendorNotifyImagiview, vendorcallImagiview, vendorlocateImagiview;
    private CircleImageView vendorProfileImageView;
    private RecyclerView vendorReviewRecyclerView;
    private TextView vendorRatingtextView, addReviewTextView, vendorDescriptionTextView1;
    private ArrayList<Ratings> arrayList = new ArrayList<>();
    private RatingsAdapter adapter;
    private Gson gson = new Gson();
    private String phoneNo, follow, vendor_Id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_vendor_detail);
        //setContentView(R.layout.activity_vendor_detail);
        //setActionBar("");
        vendorReviewRecyclerView = (RecyclerView) findViewById(R.id.vendorReviewRecyclerView);
        vendorReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bannerImageView = (ImageView) findViewById(R.id.bannerImageView);
        vendorNotifyImagiview = (ImageView) findViewById(R.id.vendorDetailsNotifyImagiview);
        vendorcallImagiview = (ImageView) findViewById(R.id.vendorDetailscallImagiview);
        vendorlocateImagiview = (ImageView) findViewById(R.id.vendorDetilslocateImagiview);
        vendorProfileImageView = (CircleImageView) findViewById(R.id.vendorProfileImageView);
        addReviewTextView = (TextView) findViewById(R.id.addReviewTextView);
        vendorRatingtextView = (TextView) findViewById(R.id.vendorRatingtextView);
        vendorDescriptionTextView1 = (TextView) findViewById(R.id.vendorDescriptionTextView1);

        adapter = new RatingsAdapter(VendorDetailActivity.this, arrayList);
        vendorReviewRecyclerView.setAdapter(adapter);
        Complete.ratingDialogInstance().setListener(new SaveCompletedInterface() {
            @Override
            public void completed() {
                if (getIntent().getExtras() != null) {
                    String vendorId = getIntent().getExtras().getString("vendor_id");
                    getData(vendorId);
                    //getRatings(vendorId);
                }
            }
        });
        vendorlocateImagiview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonWebserviceMethods.getVendorLocation(VendorDetailActivity.this, TAG, vendor_Id);
            }
        });
        vendorcallImagiview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.callFunction(VendorDetailActivity.this, phoneNo);
            }
        });
        vendorNotifyImagiview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow.equalsIgnoreCase("Y")) {
                    AlertDialogManager.listenerDialogBox(VendorDetailActivity.this, "Remove!", "Remove alert?", new DialogBoxInterface() {
                        @Override
                        public void yes() {
                            CommonWebserviceMethods.removeFollows(VendorDetailActivity.this,TAG,vendor_Id,"2");
//                            GetResponseFromServer.getWebService(VendorDetailActivity.this, TAG).removeFollow(VendorDetailActivity.this, vendor_Id, new VolleyResponseListerner() {
//                                @Override
//                                public void onResponse(JSONObject response) throws JSONException {
//                                    if (response.getString("status").equalsIgnoreCase("1")) {
//                                        Complete.ratingDialogInstance().orderCompleted();
//                                    }
//                                }
//
//                                @Override
//                                public void onError(String message, String title) {
//
//                                }
//                            });
                        }

                        @Override
                        public void no() {

                        }
                    });

                } else {
                    CommonWebserviceMethods.setFollows(VendorDetailActivity.this,TAG,vendor_Id,"2");

//                    GetResponseFromServer.getWebService(VendorDetailActivity.this, TAG).setFollow(VendorDetailActivity.this, vendor_Id, new VolleyResponseListerner() {
//                        @Override
//                        public void onResponse(JSONObject response) throws JSONException {
//                            if (response.getString("status").equalsIgnoreCase("1")) {
//                                Complete.ratingDialogInstance().orderCompleted();
//                            }
//
//                        }
//
//                        @Override
//                        public void onError(String message, String title) {
//
//                        }
//                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null) {
            String vendorId = getIntent().getExtras().getString("vendor_id");
            getData(vendorId);
            getRatings(vendorId);
        }
    }

    private void getRatings(String venderid) {
        GetResponseFromServer.getWebService(VendorDetailActivity.this, TAG).getRatinds(VendorDetailActivity.this, venderid, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                arrayList.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Ratings.class));
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    private void getData(String vendorId) {
        GetResponseFromServer.getWebService(VendorDetailActivity.this, TAG).getVendorDetail(VendorDetailActivity.this, vendorId, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    JSONObject jsonObject = response.getJSONObject("data");
                    phoneNo = jsonObject.getString("phone_no");
                    follow = jsonObject.getString("follow");
                    vendor_Id = jsonObject.getString("vendor_id");

                    if (follow.equalsIgnoreCase("Y")) {
                        vendorNotifyImagiview.setImageResource(R.drawable.notify);
                    } else {
                        vendorNotifyImagiview.setImageResource(R.drawable.nonotify);
                    }
                    if (!jsonObject.getString("photo").equalsIgnoreCase("")) {
                        Picasso.with(VendorDetailActivity.this).load(jsonObject.getString("photo")).into(vendorProfileImageView);
                    } else {
                        vendorProfileImageView.setImageResource(R.drawable.no_image);
                    }
                    if (!jsonObject.getString("photo").equalsIgnoreCase("")) {
                        Picasso.with(VendorDetailActivity.this).load(jsonObject.getString("business_icon")).into(bannerImageView);
                    } else {
                        bannerImageView.setImageResource(R.drawable.no_image);
                    }
                    vendorDescriptionTextView1.setText(jsonObject.getString("description"));
                    setTitle(jsonObject.getString("name"));
                    setNotification(jsonObject.getString("is_online"));

                    vendorRatingtextView.setText(jsonObject.getString("star_rating") + " (" + jsonObject.getString("rating_count") + " Ratings)");
                }

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
