package com.sample.honeybuser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.honeybuser.R;

/**
 * Created by IM0033 on 10/5/2016.
 */

public class NavigationBarAdapter extends BaseAdapter {
    Context context;
    private String[] listName = {"Street", "Alerts", "Search", "Settings"};
    private int[] listImage = {
            R.drawable.street,
            R.drawable.bell,
            R.drawable.search,
            R.drawable.settings1,

    };
    private LayoutInflater inflater;

    public NavigationBarAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return listName.length;
    }

    @Override
    public Object getItem(int i) {
        return listImage[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.navigationbar_list_item, viewGroup, false);
        }
        TextView nameTextView = (TextView) view.findViewById(R.id.navigationTextView);
        ImageView imageView = (ImageView) view.findViewById(R.id.navigationImageView);
        nameTextView.setText(listName[i]);
        imageView.setImageResource(listImage[i]);
        return view;
    }
}
