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

import com.udacity.movie.data.MovieContract.MovieEntry;

public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMather = buildUriMather();

    private MovieDbHelper movieDbHelper;
    private SQLiteDatabase db;

    static final int MOVIE = 100;
    static final int MOVIE_FAVORED = 102;

    // movie.favored
    private static final String sFavoredSelection = MovieEntry.TABLE_NAME + "." + MovieEntry.COLUMN_FAVORED + " = " + 1;

    static UriMatcher buildUriMather () {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/" +MOVIE_FAVORED, MOVIE_FAVORED);

        return matcher;
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
            case MOVIE: {
                retCursor = db.query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_FAVORED: {
                retCursor = db.query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        sFavoredSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
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

        final int match = sUriMather.match(uri);

        switch (match) {
            case MOVIE:
            case MOVIE_FAVORED:
                return MovieEntry.CONTENT_TYPE;
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
            case MOVIE: {
                long _id = db.insert(MovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MovieEntry.buildMovieUri(_id);
                } else {
                    throw new UnsupportedOperationException("Insert 错误: " + uri);
                }
                break;
            }
            case MOVIE_FAVORED: {
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
            case MOVIE:
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
            case MOVIE:
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

        final int match = sUriMather.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(
                        MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
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
