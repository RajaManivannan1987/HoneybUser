package com.sample.honeybuser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.honeybuser.Activity.VendorDetailActivity;
import com.sample.honeybuser.InterFaceClass.DialogBoxInterface;
import com.sample.honeybuser.Models.Ratings;
import com.sample.honeybuser.Models.VendorSearchListModel;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.AlertDialogManager;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.CommonWebserviceMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Raja M on 10/19/2016.
 */

public class VendorSearchAdapter extends RecyclerView.Adapter<VendorSearchAdapter.CustomHolder> {
    private String TAG = "VendorSearchAdapter";
    private Activity context;
    private ArrayList<VendorSearchListModel> vendorList;

    public VendorSearchAdapter(Activity context, ArrayList<VendorSearchListModel> vendorList) {
        this.context = context;
        this.vendorList = vendorList;

    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomHolder(LayoutInflater.from(context).inflate(R.layout.vendor_search_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final CustomHolder holder, final int position) {
        holder.vendorSearchNameTextView.setText(vendorList.get(position).getName());
        holder.vendorSearchRatingTextView.setText(vendorList.get(position).getStar_rating() + " (" + vendorList.get(position).getRating_count() + " Ratings " + ") ");
        if (!vendorList.get(position).getPhoto().equalsIgnoreCase("")) {
            Picasso.with(context).load(vendorList.get(position).getPhoto()).into(holder.vendorProfileImageView);
        } else {
            holder.vendorProfileImageView.setImageResource(R.drawable.nouser);
        }
        if (vendorList.get(position).getIs_online().equalsIgnoreCase("Y")) {
            holder.vendorSearchOnlineImageView.setImageResource(R.drawable.on);
            holder.vendorSearchLocateImageView.setVisibility(View.VISIBLE);
        } else {
            holder.vendorSearchOnlineImageView.setImageResource(R.drawable.off);
            holder.vendorSearchLocateImageView.setVisibility(View.INVISIBLE);
        }
        if (vendorList.get(position).getFollow().equalsIgnoreCase("Y")) {
            holder.vendorSearchNotifyImageView.setImageResource(R.drawable.notify);
        } else {
            holder.vendorSearchNotifyImageView.setImageResource(R.drawable.nonotify);
        }

        holder.vendorSearchCallImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.callFunction(context, vendorList.get(position).getPhone_no());
            }
        });
        holder.vendorSearchLocateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonWebserviceMethods.getVendorLocation(context, TAG, vendorList.get(position).getVendor_id(), vendorList.get(position).getName());
            }
        });
        holder.vendorCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VendorDetailActivity.class);
                intent.putExtra("vendor_id", vendorList.get(position).getVendor_id());
                intent.putExtra("notificationType", "");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, holder.vendorCardView, ConstandValue.transitionName);
                context.startActivity(intent, options.toBundle());
//                context.startActivity(new Intent(context, VendorDetailActivity.class).putExtra("vendor_id", vendorList.get(position).getVendor_id()));
            }
        });
        holder.vendorSearchNotifyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vendorList.get(position).getFollow().equalsIgnoreCase("Y")) {
                    AlertDialogManager.listenerDialogBox(context, "", " Disable Alert for this Vendor", new DialogBoxInterface() {
                        @Override
                        public void yes() {
                            CommonWebserviceMethods.removeFollows(context, TAG, vendorList.get(position).getVendor_id(), "4");
                        }

                        @Override
                        public void no() {

                        }
                    });

                } else {
                    CommonWebserviceMethods.setFollows(context, TAG, vendorList.get(position).getVendor_id(), "4");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return vendorList.size();

    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        CardView vendorCardView;
        TextView vendorSearchRatingTextView, vendorSearchNameTextView;
        ImageView vendorProfileImageView, vendorSearchOnlineImageView, vendorSearchNotifyImageView, vendorSearchCallImageView, vendorSearchLocateImageView;

        public CustomHolder(View itemView) {
            super(itemView);
            vendorProfileImageView = (ImageView) itemView.findViewById(R.id.vendorProfileImageView);
            vendorSearchOnlineImageView = (ImageView) itemView.findViewById(R.id.vendorSearchOnlineImageView);
            vendorSearchNotifyImageView = (ImageView) itemView.findViewById(R.id.vendorSearchNotifyImageView);
            vendorSearchCallImageView = (ImageView) itemView.findViewById(R.id.vendorSearchCallImageView);
            vendorSearchLocateImageView = (ImageView) itemView.findViewById(R.id.vendorSearchLocateImageView);
            vendorSearchRatingTextView = (TextView) itemView.findViewById(R.id.vendorSearchRatingTextView);
            vendorSearchNameTextView = (TextView) itemView.findViewById(R.id.vendorSearchNameTextView);
            vendorCardView = (CardView) itemView.findViewById(R.id.vendorCardView);
        }
    }
}
