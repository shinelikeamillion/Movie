package com.udacity.movie.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class MovieSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static MovieSyncAdapter syncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("MovieSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (syncAdapter == null) {
                syncAdapter = new MovieSyncAdapter(getApplicationContext(), true);
            }
        }
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
