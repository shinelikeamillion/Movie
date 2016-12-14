package com.udacity.movie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.movie.R;
import com.udacity.movie.fragment.DetailFragment;
import com.udacity.movie.fragment.MainFragment;
import com.udacity.movie.fragment.MainFragment.MovieItemClickCallback;
import com.udacity.movie.model.MovieInfo;
import com.udacity.movie.sync.MovieSyncAdapter;

public class MainActivity extends AppCompatActivity implements MovieItemClickCallback{

    private final String TAG= this.getClass().getSimpleName();
    private final String MOVIE_FRAGMENT_TAG = "FFTAG";
    private boolean mIsBigScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null != findViewById(R.id.movie_detail_container)) {
            mIsBigScreen = true;

            if (null == savedInstanceState) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.movie_detail_container, new DetailFragment(), MOVIE_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mIsBigScreen = false;
            getSupportActionBar().setElevation(0f);
        }

        if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new MainFragment(), MOVIE_FRAGMENT_TAG)
                    .commit();
        }

        MovieSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public void onItemSelected(MovieInfo movieInfo) {

        if (mIsBigScreen) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, movieInfo);

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(args);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.movie_detail_container, detailFragment, DetailFragment.DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {

            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, movieInfo);
            startActivity(intent);
        }
    }
}
