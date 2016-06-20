package com.lifeinvader.tracker.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.firebase.client.Firebase;
import com.lifeinvader.tracker.services.LocationService;
import com.lifeinvader.tracker.R;
import com.lifeinvader.tracker.adapters.GroupsAdapter;
import com.lifeinvader.tracker.models.Group;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

public class GroupListActivity extends AppCompatActivity {
    public boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_list);

        RxPermissions.getInstance(this)
            .request(Manifest.permission.ACCESS_COARSE_LOCATION)
            .subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean granted) {
                    if(granted) {
                        startService(new Intent(GroupListActivity.this, LocationService.class));
                    }
                }
            });

        Firebase.setAndroidContext(this);
        Firebase firebaseRef = new Firebase("https://project-4640347631861502445.firebaseio.com/");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.group_list);
        assert recyclerView != null;

        recyclerView.setAdapter(new GroupsAdapter(
            this, firebaseRef.child("groups"), Group.class, null, null
        ));

        if (findViewById(R.id.group_detail_container) != null) {
            twoPane = true;
        }
    }
}
