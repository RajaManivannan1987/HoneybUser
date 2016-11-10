package com.sample.honeybuser.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.sample.honeybuser.Activity.DashBoardActivity;
import com.sample.honeybuser.Activity.SearchActivity;
import com.sample.honeybuser.InterFaceClass.DialogBoxInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Models.ChangeAddress;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ChangeLocationSingleton;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.AlertDialogManager;
import com.sample.honeybuser.Utility.Fonts.CommonUtilityClass.CommonMethods;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IM028 on 7/4/16.
 */
public class AddressRecyclerViewAdapter extends RecyclerView.Adapter<AddressRecyclerViewAdapter.CustomViewHolder> {

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    private List<ChangeAddress> addressList = new ArrayList<ChangeAddress>();
    private Activity activity;
    private int selectedPosition = -1;


    public AddressRecyclerViewAdapter(Activity activity, List<ChangeAddress> addressList) {
        this.addressList = addressList;
        this.activity = activity;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(activity.getLayoutInflater().inflate(R.layout.item_list_address, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.titleTextView.setText(addressList.get(position).getTitle());
        holder.contentTextView.setText(addressList.get(position).getAddress());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialogManager.listenerDialogBox(activity, "Delete!", "Are you sure want to delete this address?", new DialogBoxInterface() {
                    @Override
                    public void yes() {
                        GetResponseFromServer.getWebService(activity, "AddressAdaptr").removeAddress(activity, addressList.get(position).getAddress_id(), new VolleyResponseListerner() {
                            @Override
                            public void onResponse(JSONObject response) throws JSONException {
                                if (response.getString("status").equalsIgnoreCase("1")) {
                                    addressList.remove(position);
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(String message, String title) {

                            }
                        });

                    }

                    @Override
                    public void no() {

                    }
                });

                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashBoardActivity.distanceLatLng = new LatLng(Double.parseDouble(addressList.get(position).getLatitude()), Double.parseDouble(addressList.get(position).getLongitude()));
                Log.d("AddressRecycler", "" + DashBoardActivity.distanceLatLng);
                ChangeLocationSingleton.getInstance().locationChanges(new LatLng(Double.parseDouble(addressList.get(position).getLatitude()), Double.parseDouble(addressList.get(position).getLongitude())), null, null, "AddressRecyclerViewAdapter");

                Complete.offerDialogInstance().orderCompleted();
                Complete.getGetMapList().orderCompleted();
                activity.finish();

                if (position == selectedPosition) {
                    selectedPosition = -1;
                } else {
                    selectedPosition = position;
                }
                notifyDataSetChanged();
            }
        });
        if (position == selectedPosition) {
            holder.linearLayout.setBackgroundResource(R.color.textcolorGray);
        } else {
            holder.linearLayout.setBackgroundResource(android.R.color.transparent);
        }

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public LatLng selectedPosition() {
        if (selectedPosition > -1) {
            return new LatLng(Double.parseDouble(addressList.get(selectedPosition).getLatitude()), Double.parseDouble(addressList.get(selectedPosition).getLongitude()));
        } else {
            return null;
        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, contentTextView;
        public LinearLayout linearLayout;

        public CustomViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.itemListAddressTitleTextView);
            contentTextView = (TextView) itemView.findViewById(R.id.itemListAddressContentTextView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.itemListAddressLinearLayout);

        }
    }
}
