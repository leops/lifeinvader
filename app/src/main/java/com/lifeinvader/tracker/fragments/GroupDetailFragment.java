package com.lifeinvader.tracker.fragments;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lifeinvader.tracker.R;
import com.lifeinvader.tracker.models.Group;

public class GroupDetailFragment extends Fragment {
    public static final String ARG_ITEM = "group";

    private TextView detailView;

    public GroupDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase firebaseRef = new Firebase("https://project-4640347631861502445.firebaseio.com/");

        if (getArguments().containsKey(ARG_ITEM)) {
            Activity activity = this.getActivity();
            final CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            String key = getArguments().getString(ARG_ITEM);
            Firebase itemRef = firebaseRef.child("groups").child(key);
            itemRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Group item = dataSnapshot.getValue(Group.class);

                    if (appBarLayout != null) {
                        appBarLayout.setTitle(item.name);
                    }

                    if (detailView != null) {
                        detailView.setText(item.name);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    //
                }
            });


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_detail, container, false);
        detailView = (TextView) rootView.findViewById(R.id.group_detail);
        return rootView;
    }
}
