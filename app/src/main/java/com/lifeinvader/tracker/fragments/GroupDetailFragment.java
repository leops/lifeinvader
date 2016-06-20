package com.lifeinvader.tracker.fragments;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lifeinvader.tracker.R;
import com.lifeinvader.tracker.adapters.UsersAdapter;
import com.lifeinvader.tracker.models.Group;

public class GroupDetailFragment extends Fragment {
    public static final String ARG_ITEM = "group";

    private RecyclerView listView;
    private Firebase firebaseRef;
    private Firebase itemRef;

    public GroupDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseRef = new Firebase("https://project-4640347631861502445.firebaseio.com/");

        if (getArguments().containsKey(ARG_ITEM)) {
            Activity activity = this.getActivity();
            final CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            String key = getArguments().getString(ARG_ITEM);
            if(key != null) {
                itemRef = firebaseRef.child("groups").child(key);
                itemRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Group item = dataSnapshot.getValue(Group.class);

                        if (appBarLayout != null) {
                            appBarLayout.setTitle(item.name);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
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

        if(itemRef != null) {
            listView.setAdapter(new UsersAdapter(
                firebaseRef, itemRef.child("users"), String.class, null, null
            ));
        }
    }
}
