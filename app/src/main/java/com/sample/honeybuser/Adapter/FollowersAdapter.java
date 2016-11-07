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
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Models.FollowerListModel;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ActionCompletedSingleton;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.AlertDialogManager;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.CommonWebserviceMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Im033 on 10/12/2016.
 */

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.CustomHolder> {
    private String TAG = "FollowersAdapter";
    ArrayList<FollowerListModel> list;
    Activity context;

    public FollowersAdapter(Activity context, ArrayList<FollowerListModel> arrayList) {
        this.context = context;
        this.list = arrayList;
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomHolder(LayoutInflater.from(context).inflate(R.layout.follower_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder( final CustomHolder holder, final int position) {
        if (!list.get(position).getLogo().equalsIgnoreCase("")) {
            Picasso.with(context).load(list.get(position).getLogo()).into(holder.follower_profile_image);
        } else {
            holder.follower_profile_image.setImageResource(R.drawable.no_image);
        }
        if (list.get(position).getIs_online().equalsIgnoreCase("Y")) {
            holder.followerOnlineImageView.setImageResource(R.drawable.on);
            holder.followerLocateImageview.setVisibility(View.VISIBLE);
        } else {
            holder.followerOnlineImageView.setImageResource(R.drawable.off);
            holder.followerLocateImageview.setVisibility(View.INVISIBLE);
        }
        if (list.get(position).getNotification_status().startsWith("Y")) {
            holder.followerNotificationStatusImageview.setImageResource(R.drawable.bellon);
        } else {
            holder.followerNotificationStatusImageview.setImageResource(R.drawable.belloff);
        }
        holder.followerratingImageView.setImageResource(R.drawable.star);
        holder.followerRatingTextView.setText(list.get(position).getStar_rating());
        holder.followerRatingCountTextView.setText("(" + list.get(position).getRating_count() + " Ratings)");
        holder.followerNameTextView.setText(list.get(position).getName());
        holder.followerKmTextView.setText(list.get(position).getDistance() + " km away");
        holder.followerCallImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.callFunction(context, list.get(position).getPhone_no());
            }
        });
        holder.followerLocateImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonWebserviceMethods.getVendorLocation(context, TAG, list.get(position).getVendor_id());
                //CommonMethods.locationDirection(context, list.get(position).getLatitude(), list.get(position).getLongitude());
            }
        });
        holder.followerCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VendorDetailActivity.class);
                intent.putExtra("vendor_id", list.get(position).getVendor_id());
//                intent.putExtra("distance", "3.00");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.followerCv, ConstandValue.transitionName);
                context.startActivity(intent, options.toBundle());
//                context.startActivity(new Intent(context, VendorDetailActivity.class).putExtra("vendor_id", list.get(position).getVendor_id()));
            }
        });
        holder.onOffFollowerStatusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getFollow().equalsIgnoreCase("Y")) {
                    AlertDialogManager.listenerDialogBox(context, "Remove!", "Remove Follow?", new DialogBoxInterface() {
                        @Override
                        public void yes() {
                            CommonWebserviceMethods.removeFollows(context, TAG, list.get(position).getVendor_id(), "3");
                        }

                        @Override
                        public void no() {

                        }
                    });

                }
            }
        });
        holder.followerNotificationStatusImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getNotification_status().equalsIgnoreCase("Y")) {
                    AlertDialogManager.listenerDialogBox(context, "Disable!", "Disable Alert for this vendor?", new DialogBoxInterface() {
                        @Override
                        public void yes() {
                            CommonWebserviceMethods.removeNtification(context, TAG, list.get(position).getVendor_id());
                        }

                        @Override
                        public void no() {

                        }
                    });
                } else {
                    CommonWebserviceMethods.setNtification(context, TAG, list.get(position).getVendor_id());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        public ImageView onOffFollowerStatusImageView, followerratingImageView, followerOnlineImageView, followerCallImageview, followerLocateImageview, followerNotificationStatusImageview;
        public CircleImageView follower_profile_image;
        private CardView followerCv;
        public TextView followerRatingCountTextView, followerRatingTextView, followerKmTextView, followerNameTextView;

        public CustomHolder(View itemView) {
            super(itemView);
            followerRatingCountTextView = (TextView) itemView.findViewById(R.id.followerRatingCountTextView);
            followerRatingTextView = (TextView) itemView.findViewById(R.id.followerRatingTextView);
            followerKmTextView = (TextView) itemView.findViewById(R.id.followerKmTextView);
            followerNameTextView = (TextView) itemView.findViewById(R.id.followerNameTextView);
            follower_profile_image = (CircleImageView) itemView.findViewById(R.id.follower_profile_image);
            followerratingImageView = (ImageView) itemView.findViewById(R.id.followerratingImageView);
            followerOnlineImageView = (ImageView) itemView.findViewById(R.id.followerOnlineImageView);
            followerLocateImageview = (ImageView) itemView.findViewById(R.id.followerLocateImageview);
            followerCallImageview = (ImageView) itemView.findViewById(R.id.followerCallImageview);
            followerNotificationStatusImageview = (ImageView) itemView.findViewById(R.id.followerNotificationStatusImageview);
            followerCv = (CardView) itemView.findViewById(R.id.followerCv);
            onOffFollowerStatusImageView = (ImageView) itemView.findViewById(R.id.onOffFollowerStatusImageView);
        }
    }
}
