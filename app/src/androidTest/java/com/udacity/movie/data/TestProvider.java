package com.udacity.movie.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.udacity.movie.data.MovieContract.MovieEntry;

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

        // content://com.udacity.movie.app/movie/favored
        type = mContext.getContentResolver().getType(MovieEntry.buildMovieFavored());
        // vnd.android.cursor.dir/com.udacity.movie.app/movie/favored
        assertEquals("错误: MovieEntry CONTENT_UTI with favored 应该返回 MovieEntry.CONTENT_TYPE",
                MovieEntry.CONTENT_TYPE, type);
    }

    public void testInsertReadProvider () {
        ContentValues testValues = TestUtilities.createMovieValues();


    }
}
