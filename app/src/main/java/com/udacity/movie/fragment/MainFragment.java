package com.udacity.movie.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.udacity.movie.R;
import com.udacity.movie.activity.DetailActivity;
import com.udacity.movie.adapter.MoviesGridAdapter;
import com.udacity.movie.data.MovieContract.MovieEntry;
import com.udacity.movie.model.MovieInfo;
import com.udacity.movie.sync.MovieSyncAdapter;
import com.udacity.movie.utils.NetWorkUtils;
import com.udacity.movie.utils.Utility;

/**
 */
public class MainFragment extends Fragment implements LoaderCallbacks<Cursor>{

    private final String TAG = this.getClass().getSimpleName();
    private static final int MOVIE_LOADER_ID = 0;

    // 记住滚动位置
    private static final String SELECTED_KEY = "selected_position";
    private int mPosition = GridView.INVALID_POSITION;

    static final int COL_MOVIE_ID = 0+1;
    static final int COL_MOVIE_ORIGINAL_TITLE = 1+1;
    static final int COL_MOVIE_RELEASE_DATE = 2+1;
    static final int COL_MOVIE_POSTER_PATH = 3+1;
    static final int COL_MOVIE_VOTE_AVERAGE = 4+1;
    static final int COL_MOVIE_POPUlARITY = 5+1;
    static final int COL_MOVIE_LENGTH = 6+1;
    static final int COL_MOVIE_OVERVIEW = 7+1;
    static final int COL_MOVIE_FAVORE = 8+1;

    private GridView gridView;
    private View rootView;

    private MoviesGridAdapter moviesAdapter;

    private String sortOrder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.grid_for_movies);

        moviesAdapter = new MoviesGridAdapter(getActivity(), null, 0);
        gridView.setAdapter(moviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                MovieInfo movieInfo = null;
                if (cursor != null) {
                    movieInfo = new MovieInfo(
                            cursor.getString(COL_MOVIE_POSTER_PATH),
                            "",
                            cursor.getString(COL_MOVIE_OVERVIEW),
                            cursor.getString(COL_MOVIE_RELEASE_DATE),
                            null,
                            cursor.getInt(COL_MOVIE_ID),
                            cursor.getString(COL_MOVIE_ORIGINAL_TITLE),
                            "",
                            cursor.getString(COL_MOVIE_ORIGINAL_TITLE),
                            "",
                            cursor.getFloat(COL_MOVIE_POPUlARITY),
                            0,
                            false,
                            cursor.getInt(COL_MOVIE_VOTE_AVERAGE)
                    );
                }
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, movieInfo);
                startActivity(intent);

                mPosition = position;
            }
        });

        if (!NetWorkUtils.isNetWorkAvailable(getActivity())) {
            Snackbar.make(rootView, getString(R.string.network_ont_connected), Snackbar.LENGTH_LONG).show();
        }

        if (null != savedInstanceState &&
                savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                Utility.putSortOrderPreference(getActivity(), MovieEntry.COLUMN_POPUlARITY);
                break;
            case R.id.action_top_rated:
                Utility.putSortOrderPreference(getActivity(), MovieEntry.COLUMN_VOTE_AVERAGE);
                break;
        }
        onSortOrderChanged();
        return super.onOptionsItemSelected(item);
    }

    private void onSortOrderChanged () {
        updateMovie();
        getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    private void updateMovie ( ) {
        MovieSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e(TAG, "onCreateLoader");
        Uri movieUri = MovieEntry.CONTENT_URI;
        return new CursorLoader(
                getActivity(),
                movieUri,
                null,
                null,
                null,
                Utility.getPreferredSortOrder(getActivity()) + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        moviesAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            gridView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesAdapter.swapCursor(null);
    }

    /**
     * @String 参数
     */
//    public class FetchMoviesTask extends AsyncTask<String, Void, MovieResults> {
//
//        private final String TAG = this.getClass().getSimpleName();
//
//        @Override
//        protected MovieResults doInBackground(String... params) {
//
//            //没有参数,直接跳过
//            if (params.length == 0) {
//                return null;
//            }
//            return getMovies(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(MovieResults movieResults) {
//            if (movieResults != null) {
//                moviesAdapter.clear();
//                if (Build.VERSION.SDK_INT > 11) {
//                    moviesAdapter.addAll(movieResults.results);
//                } else {
//                    for (MovieInfo movieInfo : movieResults.results) {
//                        moviesAdapter.add(movieInfo);
//                    }
//                    moviesAdapter.notifyDataSetChanged();
//                }
//            }
//        }
//
//        private MovieResults getMovies (String choice) {
//            final String MVDB_BASE_URL = "https://api.themoviedb.org/3/movie/"+choice+"?";
//            final String APPID_PARAM = "api_key";
//
//            Uri buildUri = Uri.parse(MVDB_BASE_URL)
//                    .buildUpon()
//                    .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
//                    .build();
//            Log.e(TAG, "URL: "+buildUri.toString());
//
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//
//            String moviesJSONStr = null;
//
//            try {
//                // 1. 打开连接
//                URL url = new URL(buildUri.toString());
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                // 2. 获取数据流
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) {
//
//                    Log.e(TAG, "inputStream null");
//                    // 没有返回
//                    return null;
//                }
//
//                // 3. 读取响应值
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                reader = new BufferedReader(inputStreamReader);
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//
//                    Log.i(TAG, line);
//                    buffer.append(line + "\n");
//                }
//
//                if (buffer.length() == 0) {
//                    // 返回的数据为空
//                    Log.e(TAG, "inputStreamReader null");
//                    return null;
//                }
//
//                moviesJSONStr = buffer.toString();
//            } catch (MalformedURLException e) {
//                Log.e(TAG, "MalformedURL failed", e);
//                return null;
//            } catch (IOException e) {
//                Log.e(TAG, "IOException", e);
//                Snackbar snackbar = null;
//                if (!NetWorkUtils.isOnline()) {
//                    snackbar = make(rootView, getString(R.string.internet_not_connected), Snackbar.LENGTH_INDEFINITE);
//                    snackbar.setAction(getString(R.string.try_again), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            new FetchMoviesTask().execute(TOP_RATED);
//                        }
//                    });
//                    snackbar.show();
//                } else {
//                    if (snackbar != null) {
//                        snackbar.dismiss();
//                    }
//                }
//                return null;
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        Log.e(TAG, "Error closing reader Stream", e);
//                    }
//                }
//            }
//
//            try {
//               return getMoviesFromjSONStr(moviesJSONStr);
//            } catch (JSONException e) {
//                Log.e(TAG, "JSON 解析出错"+e.getMessage(), e);
//            }
//
//            // 只有解析出错的时候才会走到这里来
//            return null;
//        }
//
//        // 从jSON字符中解析出我们需要的数据
//        private MovieResults getMoviesFromjSONStr (String moviesjSONStr) throws JSONException {
//            // 提取所需要的JSON对象的键名
//            final String MR_PAGE = "page";
//            final String MR_TOTAL_PAGES = "total_pages";
//            final String MR_TOTAL_RESULTS = "total_results";
//            final String MR_RESULTS = "results";
//
//            final String M_POSTER_PATH = "poster_path";
//            final String M_ADULT = "adult";
//            final String M_OVERVIEW = "overview";
//            final String M_RELEASE_DATE = "release_date";
//            final String M_GENRE_IDS = "genre_ids";
//            final String M_ID = "id";
//            final String M_ORIGINAL_TITLE = "original_title";
//            final String M_ORIGINAL_LANGUAGE = "original_language";
//            final String M_TITLE = "title";
//            final String M_BACKDROP_PATH = "backdrop_path";
//            final String M_POPULARITY = "popularity";
//            final String M_VOTE_COUNT = "vote_count";
//            final String M_VIDEO = "video";
//            final String M_VOTE_AVERAGE = "vote_average";
//
//            JSONObject moviesResultJson = new JSONObject(moviesjSONStr);
//            JSONArray movieArray = moviesResultJson.getJSONArray(MR_RESULTS);
//
//            MovieResults moviesResult = new MovieResults();
//            moviesResult.page = moviesResultJson.getInt(MR_PAGE);
//            moviesResult.total_pages = moviesResultJson.getInt(MR_TOTAL_PAGES);
//            moviesResult.total_results = moviesResultJson.getInt(MR_TOTAL_RESULTS);
//            moviesResult.results = new ArrayList<>();
//
//            for (int i = 0 ; i < movieArray.length(); i++) {
//                JSONObject movieJson = movieArray.getJSONObject(i);
//                JSONArray genre_ids = movieJson.getJSONArray(M_GENRE_IDS);
//
//                int[] ids = new int[genre_ids.length()];
//                for (int j = 0; j < genre_ids.length(); j++) {
//                    ids[j] = genre_ids.getInt(j);
//                }
//
//                MovieInfo movie = new MovieInfo(
//                        movieJson.getString(M_POSTER_PATH),
//                        movieJson.getString(M_ADULT),
//                        movieJson.getString(M_OVERVIEW),
//                        movieJson.getString(M_RELEASE_DATE),
//                        ids,
//                        movieJson.getInt(M_ID),
//                        movieJson.getString(M_ORIGINAL_TITLE),
//                        movieJson.getString(M_ORIGINAL_LANGUAGE),
//                        movieJson.getString(M_TITLE),
//                        movieJson.getString(M_BACKDROP_PATH),
//                        Float.parseFloat(movieJson.getString(M_POPULARITY)),
//                        movieJson.getInt(M_VOTE_COUNT),
//                        movieJson.getBoolean(M_VIDEO),
//                        movieJson.getInt(M_VOTE_AVERAGE));
//
//                moviesResult.results.add(movie);
////                Log.e(TAG, "电影"+i+" : "+ movie.toString());
//            }
//            return moviesResult;
//        }
//    }

}
