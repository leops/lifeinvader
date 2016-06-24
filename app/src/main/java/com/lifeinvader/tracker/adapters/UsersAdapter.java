package com.lifeinvader.tracker.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifeinvader.tracker.R;
import com.lifeinvader.tracker.models.User;

import java.util.ArrayList;

public class UsersAdapter extends FirebaseRecyclerAdapter<UsersAdapter.ViewHolder, String> {
    private final FirebaseDatabase database;

    public UsersAdapter(FirebaseDatabase database, DatabaseReference query, Class<String> itemClass,
                        @Nullable ArrayList<String> items, @Nullable ArrayList<String> keys) {
        super(query, itemClass, items, keys);
        this.database = database;
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.user_list_content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UsersAdapter.ViewHolder holder, int position) {
        holder.setUserId(database, getItem(position));
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

        private DatabaseReference ref;
        private ValueEventListener listener;

        public final View mView;
        public final TextView usernameView;

        public ViewHolder(View view) {
            super(view);
            ref = null;

            mView = view;
            usernameView = (TextView) view.findViewById(R.id.username);
        }

        public void setUserId(FirebaseDatabase ref, String userId) {
            if(this.ref != null && this.listener != null) {
                this.ref.removeEventListener(this.listener);
            }

            this.ref = ref.getReference("users").child(userId);
            this.listener = this.ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    usernameView.setText(user.firstName + " " + user.lastName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //
                }
            });
        }
    }
}
