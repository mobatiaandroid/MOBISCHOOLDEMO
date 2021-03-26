package com.mobatia.naisapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mobatia.naisapp.manager.PreferenceManager;

/**
 * Created by mob-dt-and007 on 26/10/16.
 */
public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }

    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        PreferenceManager.setIsFirstLaunch(getApplicationContext(), true);
        //Code here
        stopSelf();
    }
}