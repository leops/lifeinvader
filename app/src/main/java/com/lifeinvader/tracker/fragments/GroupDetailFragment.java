package com.lifeinvader.tracker.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifeinvader.tracker.R;
import com.lifeinvader.tracker.adapters.UsersAdapter;
import com.lifeinvader.tracker.models.Group;

public class GroupDetailFragment extends Fragment {
    public static final String ARG_ITEM = "group";

    private RecyclerView listView;
    private FirebaseDatabase database;
    private DatabaseReference groupRef;

    public GroupDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();

        if (getArguments().containsKey(ARG_ITEM)) {
            Activity activity = this.getActivity();
            final CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            String key = getArguments().getString(ARG_ITEM);
            if(key != null) {
                groupRef = database.getReference("groups").child(key);
                groupRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Group item = dataSnapshot.getValue(Group.class);

                        if (appBarLayout != null) {
                            appBarLayout.setTitle(item.name);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //
                    }
                });
            } else {
                Log.w("GroupDetailFragment", "WTF: " + getArguments().keySet().size());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_detail, container, false);
        listView = (RecyclerView) rootView.findViewById(R.id.group_users);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(groupRef != null) {
            listView.setAdapter(new UsersAdapter(
                    database, groupRef.child("users"), String.class, null, null
            ));
        }
    }
}
