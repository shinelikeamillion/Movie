package com.udacity.movie.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.ActivityTestCase;

import com.udacity.movie.data.MovieContract.MovieEntry;
import com.udacity.movie.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

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

    public static  final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertMovieValue () {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, 10+i);
            movieValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, "kill bill"+i);
            movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, "2010-1-2"+i);
            movieValues.put(MovieEntry.COLUMN_POSTER_PATH, "/jflsd/cjl/dfls");
            movieValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, "6.63");
            movieValues.put(MovieEntry.COLUMN_POPUlARITY, "47.9087");
            movieValues.put(MovieEntry.COLUMN_LENGTH, "120min");
            movieValues.put(MovieEntry.COLUMN_OVERVIEW, "i don't know"+i);
            movieValues.put(MovieEntry.COLUMN_FAVORED, "0");
            returnContentValues[i] = movieValues;
        }
        return returnContentValues;
    }

    static  void validateCursor (String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("返回的游标为空: "+error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord (String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("列 '" + columnName + "' 没有找到. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Values '" + entry.getValue().toString()
                    + "' 与期望值不匹配' "+ expectedValue + " '."
                    + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;

        static TestContentObserver getTestContentObserver () {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        boolean mContentChanged;
        public TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail () {
            new PollingCheck(5000) {

                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver () {
        return TestContentObserver.getTestContentObserver();
    }
}
