package com.udacity.movie.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;

import com.udacity.movie.data.MovieContract.MovieEntry;
import com.udacity.movie.data.TestUtilities.TestContentObserver;

public class TestProvider extends AndroidTestCase {
    private static final String TAG = TestProvider.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
//        deleteAllRecordsFromDB();
        deleteAllRecordsFromProvider();
    }

    // 如果 contentProvider 的删除方法测试通过,此方法废弃
    public void deleteAllRecordsFromDB() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(MovieEntry.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteAllRecordsFromProvider () {
        mContext.getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                null, // null for all
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("错误: 数据没有删完: ", 0, cursor.getCount());
        cursor.close();
    }

    // 测试Provider有没有正确的注册
    public  void testProviderRegistry () {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());

        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("错误: MovieProvider 授权: " +providerInfo.authority + " 应该是: " +
                    MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (NameNotFoundException e) {
            assertEquals("Provider 没有正确的注册: " + mContext.getPackageName(), false);
        }
    }

    public void testGetType () {
        // content://com.udacity.movie.app/movie
        String type = mContext.getContentResolver().getType(MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.udacity.movie.app/movie
        assertEquals("错误: MovieEntry CONTENT_UTI 应该返回 MovieEntry.CONTENT_TYPE",
                MovieEntry.CONTENT_TYPE, type);

        // content://com.udacity.movie.app/movie/movies_favored
        String favoredType = mContext.getContentResolver().getType(MovieEntry.buildMovieFavored());
        // vnd.android.cursor.dir/com.udacity.movie.app/movie/movies_favored
        assertEquals("错误: MovieEntry.CONTENT_TYPE BUT "+ favoredType,
                MovieEntry.CONTENT_TYPE, favoredType);

        int id = 1021;
        int favored = 1;
        String IdWithFavoredType = mContext.getContentResolver().getType(MovieEntry.buildMovieIdWithFavored(id, favored));
        assertEquals("错误: MovieEntry CONTENT_URI 应该返回 MovieEntry.CONTENT_TYPE",
                MovieEntry.CONTENT_ITEM_TYPE, IdWithFavoredType);
    }

    public void testBasicMovieQuery () {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues testValues = TestUtilities.createMovieValues();
        long movieRowId = db.insert(MovieEntry.TABLE_NAME, null, testValues);

        assertTrue("无法插入数据到数据库: ", movieRowId != -1);

        db.close();

        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null, // null for all
                null, // col where
                null, // values where
                null // sort order
        );

        TestUtilities.validateCursor("测试基本的查询: ",cursor, testValues);

        if ( Build.VERSION.SDK_INT >= 19 ) {
            assertEquals("错误: NotificationUri 可能没有设置",
                    cursor.getNotificationUri(), MovieEntry.CONTENT_URI);
        }
    }

    public void testInsertReadProvider () {
        ContentValues testValues = TestUtilities.createMovieValues();

        // 为我们的插入注册一个观察者,这回用content resolver
        TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, tco);
        Uri movieUri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, testValues);

        assertTrue(movieUri != null);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long movieRowId = ContentUris.parseId(movieUri);

        // 验证我们拿到的返回数据
        assertTrue(movieRowId != -1);

        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testInsertProvider. 错误: 验证数据的插入。", cursor, testValues);

    }

    public void testUpdateFavored () {
        ContentValues testValues = TestUtilities.createMovieValues();

        Uri movieUri = mContext.getContentResolver()
                .insert(MovieEntry.CONTENT_URI, testValues);
        assertTrue(movieUri != null);
        long movieRowId = ContentUris.parseId(movieUri);
        assertTrue(movieRowId != -1);

        testValues.put(MovieEntry._ID, movieRowId);
        testValues.put(MovieEntry.COLUMN_FAVORED, 1);

        Cursor movieCursor = mContext.getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);

        TestContentObserver tco = TestUtilities.getTestContentObserver();
        movieCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MovieEntry.buildMovieIdWithFavored((int) testValues.get(MovieEntry.COLUMN_MOVIE_ID), 1),
                testValues,
                MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[] {(int) testValues.get(MovieEntry.COLUMN_MOVIE_ID) + ""}
        );
        assertEquals(count, 1);

        tco.waitForNotificationOrFail();
        movieCursor.unregisterContentObserver(tco);
        movieCursor.close();

        Cursor cursor = mContext.getContentResolver()
                .query(
                        MovieEntry.CONTENT_URI,
                        null,
                        MovieEntry._ID + " = " + movieRowId,
                        null,
                        null);

        TestUtilities.validateCursor("updateMovie", cursor, testValues);
        cursor.close();
    }

    public void testBulkInsert () {
        ContentValues[] testBulkValues = TestUtilities.createBulkInsertMovieValue();
        TestContentObserver movieOvserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, movieOvserver);

        int inserCount = mContext.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, testBulkValues);

        movieOvserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(movieOvserver);

        assertEquals(inserCount, TestUtilities.BULK_INSERT_RECORDS_TO_INSERT);

        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                MovieEntry.COLUMN_POPUlARITY + " ASC"
        );

        assertEquals(cursor.getCount(), TestUtilities.BULK_INSERT_RECORDS_TO_INSERT);

        cursor.moveToFirst();
        for (int i = 0 ; i < TestUtilities.BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("testBulkInsert."+i, cursor, testBulkValues[i]);
        }
    }
}
