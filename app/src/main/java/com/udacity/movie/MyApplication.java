package com.udacity.movie;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build.VERSION_CODES;

import com.facebook.stetho.Stetho;

import java.util.HashSet;

public class MyApplication extends Application {

    public static HashSet<String> favoredMovieId;

    @TargetApi(VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
