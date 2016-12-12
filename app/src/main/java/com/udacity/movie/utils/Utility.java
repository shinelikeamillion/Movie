package com.udacity.movie.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION_CODES;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;

import com.udacity.movie.MyApplication;
import com.udacity.movie.data.MovieContract;
import com.udacity.movie.data.MovieContract.MovieEntry;

import java.util.HashSet;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Utility {

    public static String SORT_ORDER = "sort_order";
    public static String FAVORED_MOVIES = "favored_movies";

    public static String getPreferredSortOrder (Context context) {
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        return preferences.getString(SORT_ORDER, MovieEntry.COLUMN_POPUlARITY);
    }

    public static void putSortOrderPreference (Context context, String sortOrder) {
        getDefaultSharedPreferences(context)
                .edit()
                .putString(SORT_ORDER, sortOrder)
                .apply();
    }

    @RequiresApi(api = VERSION_CODES.HONEYCOMB)
    public static HashSet<String> getFavoredMoviesPreference (Context context) {
        return (HashSet<String>) PreferenceManager
                .getDefaultSharedPreferences(context)
                .getStringSet(FAVORED_MOVIES, new HashSet<String>());
    }

    @RequiresApi(api = VERSION_CODES.HONEYCOMB)
    public static void putFavoredMoviesPreference (Context context, String movieId) {
        MyApplication.favoredMovieId.add(movieId);
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(FAVORED_MOVIES, MyApplication.favoredMovieId)
                .apply();
    }

    public static String getOrderUrlKey (Context context) {
        if (getPreferredSortOrder(context).equals(MovieEntry.COLUMN_VOTE_AVERAGE)) {
            return MovieContract.TOP_RATED;
        }
            return MovieContract.POPULAR;
    }
}
