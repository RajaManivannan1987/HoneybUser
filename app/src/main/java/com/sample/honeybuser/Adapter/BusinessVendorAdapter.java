package com.sample.honeybuser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.honeybuser.Activity.BusinessVendorActivity;
import com.sample.honeybuser.Activity.VendorDetailActivity;
import com.sample.honeybuser.InterFaceClass.DialogBoxInterface;
import com.sample.honeybuser.Models.BusinessVendorModel;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.AlertDialogManager;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.CommonWebserviceMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Im033 on 10/20/2016.
 */

public class BusinessVendorAdapter extends RecyclerView.Adapter<BusinessVendorAdapter.CustomHolder> {
    private String TAG = "BusinessVendorAdapter";
    private ArrayList<BusinessVendorModel> list;
    Activity activity;

    public BusinessVendorAdapter(Activity context, ArrayList<BusinessVendorModel> businessList) {
        this.activity = context;
        this.list = businessList;
    }

    @Override
    public BusinessVendorAdapter.CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomHolder(LayoutInflater.from(activity).inflate(R.layout.business_vendor_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final BusinessVendorAdapter.CustomHolder holder, final int position) {
        if (!list.get(position).getBusiness_icon().equalsIgnoreCase("")) {
            Picasso.with(activity).load(list.get(position).getBusiness_icon()).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.no_image);
        }
        if (list.get(position).getNew_vendor().startsWith("N")) {
            holder.ratingImageView.setImageResource(R.drawable.star);
            holder.onLineRatingTextView.setText(list.get(position).getStar_rating());
            holder.onLineRatingCountTextView.setText("(" + list.get(position).getRating_count() + " Ratings)");
        } else {
            holder.ratingImageView.setImageResource(R.drawable.new_icon);
            holder.onLineRatingTextView.setVisibility(View.GONE);
            holder.onLineRatingCountTextView.setVisibility(View.GONE);
        }
        if (list.get(position).getIs_online().equalsIgnoreCase("N")) {
            holder.isOnlineImageView.setImageResource(R.drawable.off);
            holder.locateImage.setVisibility(View.INVISIBLE);
        } else {
            holder.isOnlineImageView.setImageResource(R.drawable.on);
            holder.locateImage.setVisibility(View.VISIBLE);
        }
        if (list.get(position).getFollow().startsWith("Y")) {
            holder.notifyImage.setImageResource(R.drawable.notify);
        } else {
            holder.notifyImage.setImageResource(R.drawable.nonotify);
        }
        holder.vendorName.setText(list.get(position).getName());
        holder.distance.setText(list.get(position).getDistance());
        holder.callImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.callFunction(activity, list.get(position).getPhone_no());
            }
        });
        holder.locateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonWebserviceMethods.getVendorLocation(activity, TAG, list.get(position).getVendor_id(), list.get(position).getName());
            }
        });
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, VendorDetailActivity.class);
                intent.putExtra("vendor_id", list.get(position).getVendor_id());
                intent.putExtra("notificationType", "");
                ActivityOptionsCompat options1 = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.cv, ConstandValue.transitionName);
                activity.startActivity(intent, options1.toBundle());
//                activity.startActivity(new Intent(activity, VendorDetailActivity.class).putExtra("vendor_id", list.get(position).getVendor_id()));
            }
        });
        holder.notifyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getFollow().equalsIgnoreCase("N")) {
                    CommonWebserviceMethods.setFollows(activity, TAG, list.get(position).getVendor_id(), "5");
                } else {
                    AlertDialogManager.listenerDialogBox(activity, "Remove!", "Remove alert?", new DialogBoxInterface() {
                        @Override
                        public void yes() {
                            CommonWebserviceMethods.removeFollows(activity, TAG, list.get(position).getVendor_id(), "5");
                        }

                        @Override
                        public void no() {

                        }
                    });

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public TextView vendorName, distance, onLineRatingTextView, onLineRatingCountTextView;
        public ImageView ratingImageView, notifyImage, callImage, locateImage, isOnlineImageView;
        public CardView cv;

        public CustomHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.b_vender_profile_image);
            vendorName = (TextView) itemView.findViewById(R.id.b_onlineVendorNameTextView);
            distance = (TextView) itemView.findViewById(R.id.b_onlineVendorKmTextView);
            ratingImageView = (ImageView) itemView.findViewById(R.id.b_ratingImageView);
            onLineRatingTextView = (TextView) itemView.findViewById(R.id.b_onLineRatingTextView);
            onLineRatingCountTextView = (TextView) itemView.findViewById(R.id.b_onLineRatingCountTextView);
            isOnlineImageView = (ImageView) itemView.findViewById(R.id.b_businessOnlineImageView);
            notifyImage = (ImageView) itemView.findViewById(R.id.b_notifyImage);
            callImage = (ImageView) itemView.findViewById(R.id.b_callImage);
            locateImage = (ImageView) itemView.findViewById(R.id.b_locateImage);
            cv = (CardView) itemView.findViewById(R.id.b_cv);
        }
    }
}
