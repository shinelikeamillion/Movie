package com.udacity.movie.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.udacity.movie.MyApplication;
import com.udacity.movie.data.MovieContract.MovieEntry;

public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMather = buildUriMather();

    private MovieDbHelper movieDbHelper;
    private SQLiteDatabase db;

    static final int MOVIES = 100;
    static final int MOVIE_ID = 102;
    static final int MOVIES_FAVORED = 103;

    // movie.type
    private static final String sMovieFetchTypeSelection = MovieEntry.TABLE_NAME + "." + MovieEntry.COLUMN_TYPE
            + " = ?";
    // movie.favored
    private static final String sMovieFavoredSelection = MovieEntry.TABLE_NAME + "." + MovieEntry.COLUMN_MOVIE_ID
            + " IN ";
    // movie.movie_id = ?
    private static final String sMovieIdSelection = MovieEntry.TABLE_NAME + "." + MovieEntry.COLUMN_MOVIE_ID
            + " = ?";

    static UriMatcher buildUriMather () {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#/*", MOVIE_ID);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/movies_favored", MOVIES_FAVORED);

        return matcher;
    }

    private static String getMoviesId () {
        String[] aa = MyApplication.favoredMovieId.toArray(new String[MyApplication.favoredMovieId.size()]);
        return "("+TextUtils.join(",", aa)+")";
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        db = movieDbHelper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMather.match(uri)) {
            case MOVIES: {
                retCursor = db.query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        sMovieFetchTypeSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIES_FAVORED: {
                retCursor = db.query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        sMovieFavoredSelection + getMoviesId(),
                        null,
                        null,
                        null,
                        null
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.e("TTag", uri.toString());
        final int match = sUriMather.match(uri);

        switch (match) {
            case MOVIES:
            case MOVIES_FAVORED:
                return MovieEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("unKnown uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        final int match = sUriMather.match(uri);
        Uri returnUri = null;

        switch (match) {
            case MOVIES: {
                long _id = db.insert(MovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MovieEntry.buildMovieUri(_id);
                } else {
                    throw new UnsupportedOperationException("Insert 错误: " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Insert 错误: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final int match = sUriMather.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount ++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final int match = sUriMather.match(uri);

        int rowsDeleted;

        if (null == selection) selection = "1";
        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(
                        MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Uri 错误: "+uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.e("update", uri.toString()+"\n"+values.toString());
        final int match = sUriMather.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIES:
            case MOVIE_ID:
                rowsUpdated = db.update(
                        MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case MOVIES_FAVORED:
                Integer movieId = MovieEntry.getMovieIdFromUri(uri);
                Integer favored = MovieEntry.getFavoredFromUri(uri);
                values = new ContentValues();
                values.put(MovieEntry.COLUMN_FAVORED, favored);
                rowsUpdated = db.update(
                        MovieEntry.TABLE_NAME,
                        values,
                        sMovieIdSelection,
                        new String[] {Integer.toString(movieId)}
                );
                break;
            default:
                throw new UnsupportedOperationException("Uri 错误: "+uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    // 为了测试方便
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        movieDbHelper.close();
        super.shutdown();
    }
}
