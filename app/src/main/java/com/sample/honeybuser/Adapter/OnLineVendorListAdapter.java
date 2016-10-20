package com.sample.honeybuser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.honeybuser.Activity.VendorDetailActivity;
import com.sample.honeybuser.InterFaceClass.DialogBoxInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Models.OnLineVendorListModel;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ActionCompletedSingleton;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.AlertDialogManager;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.CommonWebserviceMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Im033 on 10/7/2016.
 */

public class OnLineVendorListAdapter extends RecyclerView.Adapter<OnLineVendorListAdapter.CustomHolder> {
    private String TAG = "OnLineVendorListAdapter";
    private Activity activity;
    private List<OnLineVendorListModel> list;
    private LayoutInflater inflater;
    private String type;

    public OnLineVendorListAdapter(Activity context, List<OnLineVendorListModel> arraylist, String type) {
        this.list = arraylist;
        this.activity = context;
        this.type = type;
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomHolder(LayoutInflater.from(activity).inflate(R.layout.online_vendor_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomHolder holder, final int position) {
        if (!list.get(position).getLogo().equalsIgnoreCase("")) {
            Picasso.with(activity).load(list.get(position).getLogo()).into(holder.imageView);
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
        if (type.equalsIgnoreCase("0")) {
            holder.locateImage.setVisibility(View.INVISIBLE);
        }
        if (list.get(position).getFollow().startsWith("Y")) {
            holder.notifyImage.setImageResource(R.drawable.notify);
        } else {
            holder.notifyImage.setImageResource(R.drawable.nonotify);
        }
        holder.vendorName.setText(list.get(position).getName());
        holder.distance.setText(list.get(position).getDistance() + " Km away");
        holder.callImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.callFunction(activity, list.get(position).getPhone_no());
            }
        });
        holder.locateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonWebserviceMethods.getVendorLocation(activity,TAG,list.get(position).getVendor_id());
                //CommonMethods.locationDirection(activity, list.get(position).getLatitude(), list.get(position).getLongitude());
            }
        });
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, VendorDetailActivity.class).putExtra("vendor_id", list.get(position).getVendor_id()));
            }
        });
        holder.notifyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getFollow().equalsIgnoreCase("N")) {
                    CommonWebserviceMethods.setFollows(activity,TAG,list.get(position).getVendor_id(),type);
                    /*GetResponseFromServer.getWebService(activity, TAG).setFollow(activity, list.get(position).getVendor_id(), new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                if (type.equalsIgnoreCase("0")) {
                                    Complete.getInstance().orderCompleted();
                                } else {
                                    Complete.offerDialogInstance().orderCompleted();
                                    // ActionCompletedSingleton.actionCompletedSingleton().ActionCompleted();

                                }

                            }

                        }

                        @Override
                        public void onError(String message, String title) {

                        }
                    });*/
                } else {
                    AlertDialogManager.listenerDialogBox(activity, "Remove!", "Remove alert?", new DialogBoxInterface() {
                        @Override
                        public void yes() {
                            CommonWebserviceMethods.removeFollows(activity,TAG,list.get(position).getVendor_id(),type);
                            /*GetResponseFromServer.getWebService(activity, TAG).removeFollow(activity, list.get(position).getVendor_id(), new VolleyResponseListerner() {
                                @Override
                                public void onResponse(JSONObject response) throws JSONException {
                                    if (response.getString("status").equalsIgnoreCase("1")) {
                                        if (type.equalsIgnoreCase("0")) {
                                            Complete.getInstance().orderCompleted();
                                        } else {
                                            //ActionCompletedSingleton.actionCompletedSingleton().ActionCompleted();
                                            Complete.offerDialogInstance().orderCompleted();
                                        }
                                    }
                                }

                                @Override
                                public void onError(String message, String title) {

                                }
                            });*/
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
        public ImageView ratingImageView, notifyImage, callImage, locateImage;
        public CardView cv;

        public CustomHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.vender_profile_image);
            vendorName = (TextView) itemView.findViewById(R.id.onlineVendorNameTextView);
            distance = (TextView) itemView.findViewById(R.id.onlineVendorKmTextView);
            ratingImageView = (ImageView) itemView.findViewById(R.id.ratingImageView);
            onLineRatingTextView = (TextView) itemView.findViewById(R.id.onLineRatingTextView);
            onLineRatingCountTextView = (TextView) itemView.findViewById(R.id.onLineRatingCountTextView);
            notifyImage = (ImageView) itemView.findViewById(R.id.notifyImage);
            callImage = (ImageView) itemView.findViewById(R.id.callImage);
            locateImage = (ImageView) itemView.findViewById(R.id.locateImage);
            cv = (CardView) itemView.findViewById(R.id.cv);

        }
    }
}
