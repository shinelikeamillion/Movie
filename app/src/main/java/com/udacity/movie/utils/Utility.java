package com.udacity.movie.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.udacity.movie.BuildConfig;
import com.udacity.movie.data.MovieContract;
import com.udacity.movie.data.MovieContract.MovieEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
            return MovieContract.TOP_RATED;
        }
            return MovieContract.POPULAR;
    }

    public static String sendGetRequest (String key) {
        final String TAG = "sendGetRequest";
        final String MVDB_BASE_URL = MovieContract.BASE_SERVER_URL+key+"?";
        final String APPID_PARAM = "api_key";

        Uri buildUri = Uri.parse(MVDB_BASE_URL)
                .buildUpon()
                .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
        Log.e(TAG, "URL: "+buildUri.toString());

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String responseJsonStr = null;

        try {
            // 1. 打开连接
            URL url = new URL(buildUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // 2. 获取数据流
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {

                Log.e(TAG, "inputStream null");
                // 没有返回
                return null;
            }

            // 3. 读取响应值
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // 返回的数据为空
                Log.e(TAG, "inputStreamReader null");
                return null;
            }

            responseJsonStr = buffer.toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURL failed", e);
            return null;
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing reader Stream", e);
                }
            }
        }

        return responseJsonStr;
    }
}
