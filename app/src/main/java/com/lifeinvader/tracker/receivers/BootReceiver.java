package com.lifeinvader.tracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lifeinvader.tracker.services.LocationService;

/**
 * Reçoit un message au démarrage du téléphone, et lance le service de localisation
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, LocationService.class));;
    }
}
