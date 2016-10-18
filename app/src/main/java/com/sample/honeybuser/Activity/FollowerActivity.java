package com.sample.honeybuser.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.sample.honeybuser.Adapter.FollowersAdapter;
import com.sample.honeybuser.CommonActionBar.NavigationBarActivity;
import com.sample.honeybuser.EnumClass.Selected;
import com.sample.honeybuser.InterFaceClass.ActionCompleted;
import com.sample.honeybuser.InterFaceClass.SaveCompletedInterface;
import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.Models.FollowerListModel;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.ActionCompletedSingleton;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Im033 on 10/12/2016.
 */

public class FollowerActivity extends NavigationBarActivity {
    private String TAG = "FollowerActivity";
    private RecyclerView followerListRecyclerView;
    private ArrayList<FollowerListModel> followerList = new ArrayList<FollowerListModel>();
    private FollowersAdapter adapter;
    private Gson gson = new Gson();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_follower);
        //setSelectTab("follower");
        setSelected(Selected.FOLLOWER);
        setTitle("Alerts");
        followerListRecyclerView = (RecyclerView) findViewById(R.id.followerListRecyclerView);
        followerListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FollowersAdapter(FollowerActivity.this, followerList);
        followerListRecyclerView.setAdapter(adapter);
        ActionCompletedSingleton.getActionCompletedSingleton().setListener(new ActionCompleted() {
            @Override
            public void actionCompleted() {
                getData();
            }
        });
//        Complete.getInstance().setListener(new SaveCompletedInterface() {
//            @Override
//            public void completed() {
//                getData();
//            }
//        });

    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    private void getData() {
        String lat = "13.034223";
        String lang = "80.2443443";
        String type = "sales";
        GetResponseFromServer.getWebService(FollowerActivity.this, TAG).getFollowerList(FollowerActivity.this, lat, lang, type, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                followerList.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    JSONObject object = response.getJSONObject("data");
                    for (int i = 0; i < object.getJSONArray("vendors").length(); i++) {
                        followerList.add(gson.fromJson(object.getJSONArray("vendors").getJSONObject(i).toString(), FollowerListModel.class));
                    }
                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }
}
