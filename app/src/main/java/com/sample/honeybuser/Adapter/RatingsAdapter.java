package com.sample.honeybuser.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sample.honeybuser.Models.Ratings;
import com.sample.honeybuser.R;

import java.util.ArrayList;

/**
 * Created by Im033 on 10/14/2016.
 */

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.CustomHolder> {
    private Context context;
    ArrayList<Ratings> list;

    public RatingsAdapter(Context context, ArrayList<Ratings> arrayList) {
        this.context = context;
        this.list = arrayList;
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomHolder(LayoutInflater.from(context).inflate(R.layout.ratings_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomHolder holder, int position) {
        holder.reviewListItemDescriptionTextView.setText(list.get(position).getReview());
        holder.reviewListItemNameTextView.setText(list.get(position).getUsername() + ", ");
        holder.reviewListItemDaysTextView.setText(list.get(position).getDate_time());
        holder.reviewListItemRatingBar.setRating(Float.parseFloat(list.get(position).getStar_rating()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        public TextView reviewListItemDescriptionTextView, reviewListItemNameTextView, reviewListItemDaysTextView;
        public RatingBar reviewListItemRatingBar;

        public CustomHolder(View itemView) {
            super(itemView);
            reviewListItemDescriptionTextView = (TextView) itemView.findViewById(R.id.reviewListItemDescriptionTextView);
            reviewListItemNameTextView = (TextView) itemView.findViewById(R.id.reviewListItemNameTextView);
            reviewListItemDaysTextView = (TextView) itemView.findViewById(R.id.reviewListItemDaysTextView);
            reviewListItemRatingBar = (RatingBar) itemView.findViewById(R.id.reviewListItemRatingBar);
        }
    }
}
