package com.udacity.movie.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.movie.fragment.MainFragment;
import com.udacity.movie.R;

public class MainActivity extends AppCompatActivity {

    private final String TAG= this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }
}
