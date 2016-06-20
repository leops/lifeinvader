package com.lifeinvader.tracker.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lifeinvader.tracker.R;
import com.lifeinvader.tracker.models.User;

import java.util.ArrayList;

public class UsersAdapter extends FirebaseRecyclerAdapter<UsersAdapter.ViewHolder, String> {
    private final Firebase ref;

    public UsersAdapter(Firebase ref, Query query, Class<String> itemClass,
                        @Nullable ArrayList<String> items, @Nullable ArrayList<String> keys) {
        super(query, itemClass, items, keys);
        this.ref = ref;
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.user_list_content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UsersAdapter.ViewHolder holder, int position) {
        holder.setUserId(this.ref, getItem(position));
    }

    @Override
    protected void itemAdded(String item, String key, int position) {
        Log.d("UsersAdapter", "Added an item.");
    }

    @Override
    protected void itemChanged(String oldItem, String newItem, String key, int position) {
        Log.d("UsersAdapter", "Changed an item.");
    }

    @Override
    protected void itemRemoved(String item, String key, int position) {
        Log.d("UsersAdapter", "Removed an item from the adapter.");
    }

    @Override
    protected void itemMoved(String item, String key, int oldPosition, int newPosition) {
        Log.d("UsersAdapter", "Moved an item.");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private Firebase ref;
        private ValueEventListener listener;

        public final View mView;
        public final TextView usernameView;

        public ViewHolder(View view) {
            super(view);
            ref = null;

            mView = view;
            usernameView = (TextView) view.findViewById(R.id.username);
        }

        public void setUserId(Firebase ref, String userId) {
            if(this.ref != null && this.listener != null) {
                this.ref.removeEventListener(this.listener);
            }

            this.ref = ref.child("users").child(userId);
            this.listener = this.ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    usernameView.setText(user.firstName + " " + user.lastName);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    //
                }
            });
        }
    }
}
