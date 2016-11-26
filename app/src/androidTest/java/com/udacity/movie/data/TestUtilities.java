package com.udacity.movie.data;

import android.content.ContentValues;
import android.test.ActivityTestCase;

import com.udacity.movie.data.MovieContract.MovieEntry;

public class TestUtilities extends ActivityTestCase {

    static ContentValues createMovieValues() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieEntry.COLUMN_MOVIE_ID, 1021);
        movieValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, "kill bill");
        movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, "2010-1-2");
        movieValues.put(MovieEntry.COLUMN_POSTER_PATH, "/jflsd/cjl/dfls");
        movieValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, "6.63");
        movieValues.put(MovieEntry.COLUMN_POPUlARITY, "47.9087");
        movieValues.put(MovieEntry.COLUMN_LENGTH, "120min");
        movieValues.put(MovieEntry.COLUMN_OVERVIEW, "i don't know");
        movieValues.put(MovieEntry.COLUMN_FAVORED, "0");

        return movieValues;
    }


}
