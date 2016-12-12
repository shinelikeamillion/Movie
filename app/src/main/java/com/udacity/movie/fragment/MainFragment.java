package com.udacity.movie.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import com.udacity.movie.MyApplication;
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
    private Uri uri;

    private GridView gridView;
    private View rootView;

    private MoviesGridAdapter moviesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = VERSION_CODES.HONEYCOMB)
    @Override
    public void onResume() {
        super.onResume();

        MyApplication.favoredMovieId = Utility.getFavoredMoviesPreference(getActivity());
        getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.grid_for_movies);

        moviesAdapter = new MoviesGridAdapter(getActivity(), null, 0);
        gridView.setAdapter(moviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                MovieInfo movieInfo = null;
                if (cursor != null) {
                    movieInfo = MovieInfo.getMovieInfo(cursor);
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
        uri = MovieEntry.CONTENT_URI;
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
                uri = MovieEntry.CONTENT_URI;
                Utility.putSortOrderPreference(getActivity(), MovieEntry.COLUMN_POPUlARITY);
                onSortOrderChanged();
                break;
            case R.id.action_top_rated:
                uri = MovieEntry.CONTENT_URI;
                Utility.putSortOrderPreference(getActivity(), MovieEntry.COLUMN_VOTE_AVERAGE);
                onSortOrderChanged();
                break;
            case R.id.action_favored:
                uri = MovieEntry.buildMovieFavored();
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                break;

        }
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
        return new CursorLoader(
                getActivity(),
                uri,
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
        Log.e(TAG, "onLoaderReset");
        moviesAdapter.swapCursor(null);
    }

}
