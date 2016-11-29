package com.udacity.movie.task;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.udacity.movie.BuildConfig;
import com.udacity.movie.data.MovieContract.MovieEntry;
import com.udacity.movie.model.MovieInfo;
import com.udacity.movie.model.MovieResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

public class FetchMovieTask extends AsyncTask<String, Void, MovieResults> {
    private final String TAG = this.getClass().getSimpleName();

    private final Context mContext;
    public FetchMovieTask (Context context) {
        mContext = context;
    }

    @Override
    protected MovieResults doInBackground(String... params) {

        //没有参数,直接跳过
        if (params.length == 0) {
            return null;
        }
        return getMovies(params[0]);
    }

    private MovieResults getMovies (String choice) {
        final String MVDB_BASE_URL = "https://api.themoviedb.org/3/movie/"+choice+"?";
        final String APPID_PARAM = "api_key";

        Uri buildUri = Uri.parse(MVDB_BASE_URL)
                .buildUpon()
                .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
        Log.e(TAG, "URL: "+buildUri.toString());

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJSONStr = null;

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

        try {
            return getMoviesFromJSONStr(moviesJSONStr);
        } catch (JSONException e) {
            Log.e(TAG, "JSON 解析出错"+e.getMessage(), e);
        }

        // 只有解析出错的时候才会走到这里来
        return null;
    }

    // 从jSON字符中解析出我们需要的数据
    private MovieResults getMoviesFromJSONStr (String moviesJSONStr) throws JSONException {
        // 提取所需要的JSON对象的键名
        final String MR_PAGE = "page";
        final String MR_TOTAL_PAGES = "total_pages";
        final String MR_TOTAL_RESULTS = "total_results";
        final String MR_RESULTS = "results";

        final String M_POSTER_PATH = "poster_path";
        final String M_ADULT = "adult";
        final String M_OVERVIEW = "overview";
        final String M_RELEASE_DATE = "release_date";
        final String M_GENRE_IDS = "genre_ids";
        final String M_ID = "id";
        final String M_ORIGINAL_TITLE = "original_title";
        final String M_ORIGINAL_LANGUAGE = "original_language";
        final String M_TITLE = "title";
        final String M_BACKDROP_PATH = "backdrop_path";
        final String M_POPULARITY = "popularity";
        final String M_VOTE_COUNT = "vote_count";
        final String M_VIDEO = "video";
        final String M_VOTE_AVERAGE = "vote_average";

        JSONObject moviesResultJson = new JSONObject(moviesJSONStr);
        JSONArray movieArray = moviesResultJson.getJSONArray(MR_RESULTS);

        MovieResults moviesResult = new MovieResults();
        moviesResult.page = moviesResultJson.getInt(MR_PAGE);
        moviesResult.total_pages = moviesResultJson.getInt(MR_TOTAL_PAGES);
        moviesResult.total_results = moviesResultJson.getInt(MR_TOTAL_RESULTS);
        moviesResult.results = new ArrayList<>();

        // 为数据的数据插入做准备
        Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());
        for (int i = 0 ; i < movieArray.length(); i++) {
            JSONObject movieJson = movieArray.getJSONObject(i);
            JSONArray genre_ids = movieJson.getJSONArray(M_GENRE_IDS);

            int[] ids = new int[genre_ids.length()];
            for (int j = 0; j < genre_ids.length(); j++) {
                ids[j] = genre_ids.getInt(j);
            }

            MovieInfo movie = new MovieInfo(
                    movieJson.getString(M_POSTER_PATH),
                    movieJson.getString(M_ADULT),
                    movieJson.getString(M_OVERVIEW),
                    movieJson.getString(M_RELEASE_DATE),
                    ids,
                    movieJson.getInt(M_ID),
                    movieJson.getString(M_ORIGINAL_TITLE),
                    movieJson.getString(M_ORIGINAL_LANGUAGE),
                    movieJson.getString(M_TITLE),
                    movieJson.getString(M_BACKDROP_PATH),
                    Float.parseFloat(movieJson.getString(M_POPULARITY)),
                    movieJson.getInt(M_VOTE_COUNT),
                    movieJson.getBoolean(M_VIDEO),
                    movieJson.getInt(M_VOTE_AVERAGE));

            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, movie.id);
            movieValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.original_title);
            movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.release_date);
            movieValues.put(MovieEntry.COLUMN_POSTER_PATH, movieJson.getString(M_POSTER_PATH));
            movieValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.vote_average);
            movieValues.put(MovieEntry.COLUMN_POPUlARITY, movie.popularity);
            movieValues.put(MovieEntry.COLUMN_LENGTH, "");
            movieValues.put(MovieEntry.COLUMN_OVERVIEW, movie.overview);
            movieValues.put(MovieEntry.COLUMN_FAVORED, 0);
            Log.d(TAG, movie.original_title);
            cVVector.add(movieValues);

            moviesResult.results.add(movie);
        }

        int inserted = 0;
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            inserted = mContext.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, cvArray);
        }
        Log.d(TAG, "Fetch Movie Task Complete. " + inserted + " Inserted");

        return moviesResult;
    }
}
