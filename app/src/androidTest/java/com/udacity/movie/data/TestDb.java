package com.udacity.movie.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

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

        SQLiteDatabase database = new MovieDbHelper(mContext).getWritableDatabase();
        assertTrue(database.isOpen());

        // 是否成功的创建了我们想要的数据库
        Cursor cursor = database.rawQuery("SELECT NAME FROM SQLITE_MASTER WHERE TYPE = 'table'", null);
        assertTrue("Error: 数据库没有创建成功。", cursor.moveToFirst());
    }
}
