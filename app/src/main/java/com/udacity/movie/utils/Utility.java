package com.udacity.movie.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.udacity.movie.data.MovieContract.MovieEntry;

public class Utility {

    public static String SORT_ORDER = "sort_order";

    public static String getPreferredSortOrder (Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SORT_ORDER, MovieEntry.COLUMN_POPUlARITY);
    }

    public static void putSortOrderPreference (Context context, String sortOrder) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(SORT_ORDER, sortOrder)
                .apply();
    }

    public static String getOrderUrlKey (Context context) {
        if (getPreferredSortOrder(context).equals(MovieEntry.COLUMN_VOTE_AVERAGE)) {
            return "top_rated";
        }
            return "popular";
    }
}
