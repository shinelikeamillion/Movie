package com.udacity.movie.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.movie.R;
import com.udacity.movie.fragment.MainFragment;
import com.udacity.movie.sync.MovieSyncAdapter;

public class MainActivity extends AppCompatActivity {

    private final String TAG= this.getClass().getSimpleName();
    private final String MOVIE_FRAGMENT_TAG = "FFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new MainFragment(), MOVIE_FRAGMENT_TAG)
                    .commit();
        }
        MovieSyncAdapter.initializeSyncAdapter(this);
    }

}
