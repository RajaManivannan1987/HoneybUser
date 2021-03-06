package com.sample.honeybuser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.honeybuser.Activity.DashBoardActivity;
import com.sample.honeybuser.Application.MyApplication;
import com.sample.honeybuser.InterFaceClass.SelectedDistance;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IM028 on 7/2/16.
 */
public class DistanceSelectRecyclerViewAdapter extends RecyclerView.Adapter<DistanceSelectRecyclerViewAdapter.CustomViewHolder> {
    private Activity context;
    private LayoutInflater inflater;
    private List<String> distance = new ArrayList<String>();
    private int selected = -1;
    private SelectedDistance selectedDistance;
    public static String distanc;
    private String type = "";
    private String lat = "", lang = "";


    public DistanceSelectRecyclerViewAdapter(Activity context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        distance.add("0.1");
        distance.add("0.2");
        distance.add("0.3");
        distance.add("0.4");
        distance.add("0.5");
    }

    public DistanceSelectRecyclerViewAdapter(Activity context, List<String> list, String type) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        distance = list;
        this.type = type;
    }

    public void selectPosition(int position) {
        selected = position;
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return new CustomViewHolder(inflater.inflate(R.layout.item_list_distance, parent, false));
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_distance, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.textView.setText(distance.get(position));
        if (position == selected) {
            holder.textView.setTextColor(Color.WHITE);
            holder.textView.setBackgroundResource(R.drawable.background_distance_selected);
        } else {
            holder.textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.textView.setBackgroundResource(R.drawable.background_distance_unselected);
        }
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected != position) {
                    selected = position;
                    callListener(position);
                    if (type.equalsIgnoreCase("ChangeLocation")) {
                        saveAddress();
                    } else if (type.equalsIgnoreCase("Settings")) {
                        updateNotification();
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void updateNotification() {

        GetResponseFromServer.getWebService(context, "DistanceSelectRecyclerViewAdapter").updateNotificationRange(context, distance.get(selected), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    CommonMethods.toast(context, response.getString("message"));
                    //ChangeLocationSingleton.getInstance().locationChanges(null, distance.get(selected), DashBoardActivity.locationName, "DistanceRecyclerView");
                    //PreferenceManager.getDefaultSharedPreferences(context).edit().putString("KmDistance", distance.get(selected)).commit();
                    //context.finish();
                    //context.startActivity(new Intent(context, DashBoardActivity.class).putExtra("lat", String.valueOf(DashBoardActivity.distanceLatLng.latitude)).putExtra("lang", String.valueOf(DashBoardActivity.distanceLatLng.longitude)));
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }

    private void saveAddress() {
        if (DashBoardActivity.distanceLatLng != null) {
            lat = String.valueOf(DashBoardActivity.distanceLatLng.latitude);
            lang = String.valueOf(DashBoardActivity.distanceLatLng.longitude);
        } else {
            if (MyApplication.locationInstance().getLocation() != null) {
                lat = String.valueOf(MyApplication.locationInstance().getLocation().getLatitude());
                lang = String.valueOf(MyApplication.locationInstance().getLocation().getLongitude());
            }
        }
        GetResponseFromServer.getWebService(context, "DistanceSelectRecyclerViewAdapter").addressSave(context, "", "", lat, lang, distance.get(selected), "N", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    //                     By Raja 4.11.16
//                    set distance in dashboard page
                    // ChangeLocationSingleton.getInstance().locationChanges(null, distance.get(selected), DashBoardActivity.locationName, "DistanceRecyclerView");
//                    PreferenceManager.getDefaultSharedPreferences(context).edit().putString("KmDistance", distance.get(selected)).commit();
                    context.finish();
                    //context.startActivity(new Intent(context, DashBoardActivity.class).putExtra("lat", String.valueOf(DashBoardActivity.distanceLatLng.latitude)).putExtra("lang", String.valueOf(DashBoardActivity.distanceLatLng.longitude)));
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    private void callListener(int position) {
        if (selectedDistance != null) {
            selectedDistance.selected(distance.get(position));
        }
    }

    public void setListener(SelectedDistance selectedDistance) {
        this.selectedDistance = selectedDistance;
    }

    public String getDistance() {
        return distance.get(selected);
    }

    @Override
    public int getItemCount() {
        return distance.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.itemListDistanceTextView);
        }
    }
}
