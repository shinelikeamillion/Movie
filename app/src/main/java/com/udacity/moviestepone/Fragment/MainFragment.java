package com.udacity.moviestepone.Fragment;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.udacity.moviestepone.Adapter.MoviesGridAdapter;
import com.udacity.moviestepone.BuildConfig;
import com.udacity.moviestepone.R;
import com.udacity.moviestepone.model.MovieInfo;
import com.udacity.moviestepone.model.MovieResults;

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

/**
 */
public class MainFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private GridView gridView;

    private MoviesGridAdapter moviesAdapter;

    private final String TOP_RATED = "top_rated";
    private final String POPULAR = "popular";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) view.findViewById(R.id.grid_for_movies);

        moviesAdapter = new MoviesGridAdapter(getActivity(), new ArrayList<MovieInfo>());
        gridView.setAdapter(moviesAdapter);

        new FetchMoviesTask().execute(POPULAR);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_most_popular) {
            new FetchMoviesTask().execute(POPULAR);
        } else if (id == R.id.action_top_rated) {
            new FetchMoviesTask().execute(TOP_RATED);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @String 参数
     */
    public class FetchMoviesTask extends AsyncTask<String, Void, MovieResults> {

        private final String TAG = this.getClass().getSimpleName();

        @Override
        protected MovieResults doInBackground(String... params) {

            //没有参数,直接跳过
            if (params.length == 0) {
                return null;
            }
            return getMovies(params[0]);
        }

        @Override
        protected void onPostExecute(MovieResults movieResults) {
            if (movieResults != null) {
                moviesAdapter.clear();
                if (Build.VERSION.SDK_INT > 11) {
                    moviesAdapter.addAll(movieResults.results);
                } else {
                    for (MovieInfo movieInfo : movieResults.results) {
                        moviesAdapter.add(movieInfo);
                    }
                    moviesAdapter.notifyDataSetChanged();
                }
            }
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
                    // 网络或服务器问题,没有返回
                    return null;
                }

                // 3. 读取响应值
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);

                String line;
                while ((line = reader.readLine()) != null) {


                    Log.i(TAG, line);
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // 返回的数据为空
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
               return getMoviesFromjSONStr(moviesJSONStr);
            } catch (JSONException e) {
                Log.e(TAG, "JSON 解析出错"+e.getMessage(), e);
            }

            // 只有解析出错的时候才会走到这里来
            return null;
        }

        // 从jSON字符中解析出我们需要的数据
        private MovieResults getMoviesFromjSONStr (String moviesjSONStr) throws JSONException {
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

            JSONObject moviesResultJson = new JSONObject(moviesjSONStr);
            JSONArray movieArray = moviesResultJson.getJSONArray(MR_RESULTS);

            MovieResults moviesResult = new MovieResults();
            moviesResult.page = moviesResultJson.getInt(MR_PAGE);
            moviesResult.total_pages = moviesResultJson.getInt(MR_TOTAL_PAGES);
            moviesResult.total_results = moviesResultJson.getInt(MR_TOTAL_RESULTS);
            moviesResult.results = new ArrayList<>();

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
                        movieJson.getString(M_POPULARITY),
                        movieJson.getInt(M_VOTE_COUNT),
                        movieJson.getBoolean(M_VIDEO),
                        movieJson.getInt(M_VOTE_AVERAGE));

                moviesResult.results.add(movie);
//                Log.e(TAG, "电影"+i+" : "+ movie.toString());
            }
            return moviesResult;
        }
    }

}
