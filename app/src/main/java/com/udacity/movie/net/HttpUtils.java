package com.udacity.movie.net;

import android.net.Uri;
import android.util.Log;

import com.udacity.movie.BuildConfig;
import com.udacity.movie.data.MovieContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {
    private static final int CONNECTION_TIMEOUT = 10 * 1000;
    private static final String TAG = HttpUtils.class.getSimpleName();
    private static final String APPID_PARAM = "api_key";

    public static String doHttpGet (String key) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJSONStr = null;

        try {
            // 1. 打开连接
            URL url = new URL(getUri(key).toString());
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

            moviesJSONStr = buffer.toString();
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
        return moviesJSONStr;
    }

    public static Uri getUri(String key) {
        return Uri.parse(MovieContract.BASE_SERVER_URL+key)
                    .buildUpon()
                    .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();
    }
}
