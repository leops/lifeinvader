package com.lifeinvader.tracker.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.lifeinvader.tracker.R;
import com.lifeinvader.tracker.adapters.GroupsAdapter;
import com.lifeinvader.tracker.models.Group;
import com.lifeinvader.tracker.services.LocationService;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

public class GroupListActivity extends AppCompatActivity {
    public boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    setContentView(R.layout.activity_group_list);

                    RxPermissions.getInstance(GroupListActivity.this)
                            .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean granted) {
                                    if (granted) {
                                        startService(new Intent(GroupListActivity.this, LocationService.class));
                                    }
                                }
                            });

                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    assert toolbar != null;

                    setSupportActionBar(toolbar);
                    toolbar.setTitle(getTitle());

                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.group_list);
                    assert recyclerView != null;

                    recyclerView.setAdapter(new GroupsAdapter(
                            GroupListActivity.this, database.getReference("groups"), Group.class, null, null
                    ));

                    if (findViewById(R.id.group_detail_container) != null) {
                        twoPane = true;
                    }
                } else {
                    startActivity(new Intent(GroupListActivity.this, LoginActivity.class));
                }
            }
        });
    }
}
