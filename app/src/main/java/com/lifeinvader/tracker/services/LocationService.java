package com.lifeinvader.tracker.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

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

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);

        LocationRequest request = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_LOW_POWER)
            .setNumUpdates(5)
            .setInterval(100);

        mSubscription = locationProvider.getUpdatedLocation(request)
            .subscribe(new Action1<Location>() {
                @Override
                public void call(Location location) {
                    Log.d("LocationService", location.getLatitude() + ", " + location.getLongitude());
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
