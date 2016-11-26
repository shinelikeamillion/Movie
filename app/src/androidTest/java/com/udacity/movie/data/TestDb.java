package com.udacity.movie.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.udacity.movie.data.MovieContract.MovieEntry;

import java.util.HashSet;

/**
 * 数据库操作类的测试
 */

public class TestDb extends AndroidTestCase {
    final String TAG = this.getClass().getSimpleName();

    void deleteTheDatabase () {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteTheDatabase();
    }

    public void testCreateDb () throws Throwable {

        deleteTheDatabase();

        final HashSet<String> tableNames = new HashSet<>();
        tableNames.add(MovieEntry.TABLE_NAME);

        SQLiteDatabase database = new MovieDbHelper(mContext).getWritableDatabase();
        assertTrue(database.isOpen());

        // 是否成功的创建了我们想要的数据库
        Cursor cursor = database.rawQuery("SELECT NAME FROM SQLITE_MASTER WHERE TYPE = 'table'", null);
        assertTrue("Error: 数据库没有创建成功。", cursor.moveToFirst());

        // 测试我们所创建的数据库
        do {
            tableNames.remove(cursor.getString(0));
            Log.i(TAG, cursor.getString(0));
        } while ( cursor.moveToNext());
        assertTrue("Error: 标创建不完整",
                tableNames.isEmpty());

        // 验证是否创建了正确的列名
        cursor = database.rawQuery("PRAGMA TABLE_INFO (" + MovieEntry.TABLE_NAME +")", null);

        assertTrue("Error: 表的信息无法查询", cursor.moveToFirst());

        final HashSet<String> columns = new HashSet<>();
        columns.add(MovieEntry._ID);
        columns.add(MovieEntry.COLUMN_MOVIE_ID);
        columns.add(MovieEntry.COLUMN_ORIGINAL_TITLE);
        columns.add(MovieEntry.COLUMN_RELEASE_DATE);
        columns.add(MovieEntry.COLUMN_POSTER_PATH);
        columns.add(MovieEntry.COLUMN_VOTE_AVERAGE);
        columns.add(MovieEntry.COLUMN_POPUlARITY);
        columns.add(MovieEntry.COLUMN_LENGTH);
        columns.add(MovieEntry.COLUMN_OVERVIEW);
        columns.add(MovieEntry.COLUMN_FAVORED);

        int columnNameIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnNameIndex);
            Log.i(TAG, cursor.getString(columnNameIndex));
            columns.remove(columnName);
        } while (cursor.moveToNext());

        assertTrue("Error: 列名有误", columns.isEmpty());
        database.close();
    }
}
