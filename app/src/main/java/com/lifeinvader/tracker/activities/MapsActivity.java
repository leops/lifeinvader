package com.lifeinvader.tracker.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifeinvader.tracker.R;
import com.lifeinvader.tracker.fragments.GroupDetailFragment;
import com.lifeinvader.tracker.models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Affiche la position des membres d'un groupe sur une carte Google Maps
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Map<String, Marker> mapMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment =
            (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        mapMarkers = new HashMap<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        String groupKey = getIntent().getStringExtra(GroupDetailFragment.ARG_ITEM);
        database.getReference("groups").child(groupKey).child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String k) {
                final String userKey = dataSnapshot.getValue(String.class);
                database.getReference("users").child(userKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        LatLng location = new LatLng(user.latitude, user.longitude);

                        if(mapMarkers.containsKey(userKey)) {
                            Marker marker = mapMarkers.get(userKey);
                            if(marker != null) {
                                marker.setPosition(location);
                            }
                        } else {
                            mapMarkers.put(userKey, map.addMarker(
                                new MarkerOptions()
                                    .position(location)
                                    .title(user.firstName + " " + user.lastName)
                            ));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String k) {
                //
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getValue(String.class);
                if(mapMarkers.containsKey(key)) {
                    Marker marker = mapMarkers.get(key);
                    if(marker != null) {
                        mapMarkers.put(key, null);
                        marker.remove();
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String k) {
                //
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });
    }
}
