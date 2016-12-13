package com.udacity.movie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.udacity.movie.data.MovieContract.MovieEntry;

/**
 * 管理本地数据库
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    final String TAG = this.getClass().getSimpleName();

    /** 改变数据结构的时候需要增加版本
     */
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movie.db";

    final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE "
            + MovieEntry.TABLE_NAME
            + " ("
            + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
            + MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, "
            + MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
            + MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
            + MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "
            + MovieEntry.COLUMN_POPUlARITY + " REAL NOT NULL, "
            + MovieEntry.COLUMN_LENGTH + " INTEGER, "
            + MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
            + MovieEntry.COLUMN_FAVORED + " INTEGER DEFAULT 0, "
            + MovieEntry.COLUMN_TYPE + " TEXT NOT NULL, "
            + "UNIQUE ("+ MovieEntry.COLUMN_MOVIE_ID +") ON CONFLICT REPLACE"
            + " );";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        Log.i(TAG, "创建数据库...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 因为只是缓存网络数据,下面这行代码会删除已存在的数据库版本, 如果不想删除,注释掉紧跟着的这一行。
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
        Log.i(TAG, "更新数据库...");
    }
}
