package com.lifeinvader.tracker.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Le service de localisation est lancé lors du premier lancement de l'application ou au démarrage
 * de l'appareil. Il suit la position de manière passive et met a jour la base de données.
 */
public class LocationService extends Service {
    private Subscription mSubscription;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LocationService", "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d("LocationService", "onCreate");

        super.onCreate();

        FirebaseApp app = FirebaseApp.initializeApp(this, FirebaseOptions.fromResource(this));
        final FirebaseDatabase database = FirebaseDatabase.getInstance(app);
        FirebaseAuth auth = FirebaseAuth.getInstance(app);

        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    final DatabaseReference userRef = database.getReference("users").child(user.getUid());

                    ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(LocationService.this);

                    LocationRequest request = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                        .setNumUpdates(5)
                        .setInterval(100);

                    mSubscription = locationProvider.getUpdatedLocation(request)
                        .subscribe(new Action1<Location>() {
                            @Override
                            public void call(Location location) {
                                Map<String, Object> payload = new HashMap<>();
                                payload.put("latitude", location.getLatitude());
                                payload.put("longitude", location.getLongitude());

                                userRef.updateChildren(payload);
                            }
                        });
                } else {
                    stopSelf();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.d("LocationService", "onDestroy");
        super.onDestroy();
        mSubscription.unsubscribe();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
