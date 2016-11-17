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
import com.sample.honeybuser.Models.FiveKmProductSearchModel;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Raja M on 10/19/2016.
 */

public class Product05SearchAdapter extends RecyclerView.Adapter<Product05SearchAdapter.CustomHolder> {
    private Context context;
    //private ArrayList<ThreeKmProductSearchModel> threeKmList;
    private ArrayList<FiveKmProductSearchModel> fiveKmList;
    private String listType;

    /* public Product05SearchAdapter(Context context, ArrayList<ThreeKmProductSearchModel> threeKmList) {
         this.context = context;
         this.threeKmList = threeKmList;

     }
 */
    public Product05SearchAdapter(Context context, ArrayList<FiveKmProductSearchModel> fiveKmList) {
        this.context = context;
        this.fiveKmList = fiveKmList;
        // this.listType = type;

    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomHolder(LayoutInflater.from(context).inflate(R.layout.product_search_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final CustomHolder holder, final int position) {
        holder.productNameText.setText(fiveKmList.get(position).getBusiness_name());

        if (fiveKmList.get(position).getCount().equalsIgnoreCase("1")) {
            holder.productSearchCountTextView.setText(fiveKmList.get(position).getCount() + " vendor");
        } else {
            holder.productSearchCountTextView.setText(fiveKmList.get(position).getCount() + " vendors");
        }
        if (!fiveKmList.get(position).getIcon().equalsIgnoreCase("")) {
            Picasso.with(context).load(fiveKmList.get(position).getIcon()).into(holder.vendorSearchImageView);
        } else {
            holder.vendorSearchImageView.setImageResource(R.drawable.no_image);
        }
        holder.productCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessVendorActivity.class);
                intent.putExtra("business_id", fiveKmList.get(position).getBusiness_id());
                intent.putExtra("distance", "3.0");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.productCardView, ConstandValue.transitionName);
                context.startActivity(intent, options.toBundle());

//      context.startActivity(new Intent(context, BusinessVendorActivity.class).putExtra("business_id", fiveKmList.get(position).getBusiness_id()).putExtra("distance", "0.50"));
            }
        });

    }

    @Override
    public int getItemCount() {

        return fiveKmList.size();

    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        ImageView vendorSearchImageView;
        CardView productCardView;
        TextView productNameText, productSearchCountTextView;

        public CustomHolder(View itemView) {
            super(itemView);
            vendorSearchImageView = (ImageView) itemView.findViewById(R.id.vendorSearchImageView);
            productCardView = (CardView) itemView.findViewById(R.id.productCardView);
            productNameText = (TextView) itemView.findViewById(R.id.productSearchNameTextView);
            productSearchCountTextView = (TextView) itemView.findViewById(R.id.productSearchCountTextView);
        }
    }
}
