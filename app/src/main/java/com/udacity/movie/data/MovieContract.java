package com.udacity.movie.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 数据库的契约类
 */

public class MovieContract {

    public static final String BASE_SERVER_URL = "https://api.themoviedb.org/3/movie/";

    public static final String TOP_RATED = "top_rated";
    public static final String POPULAR = "popular";

    public static final String CONTENT_AUTHORITY = "com.udacity.movie.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    // 电影表相关内容
    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";

        public static final int COL_MOVIE_ID = 1;
        public static final int COL_MOVIE_ORIGINAL_TITLE = 2;
        public static final int COL_MOVIE_RELEASE_DATE = 3;
        public static final int COL_MOVIE_POSTER_PATH = 4;
        public static final int COL_MOVIE_VOTE_AVERAGE = 5;
        public static final int COL_MOVIE_POPUlARITY = 6;
        public static final int COL_MOVIE_LENGTH = 7;
        public static final int COL_MOVIE_OVERVIEW = 8;
        public static final int COL_MOVIE_FAVORED = 9;

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "movie_original_title"; // 片名
        public static final String COLUMN_RELEASE_DATE = "movie_release_date"; // 上映日期
        public static final String COLUMN_POSTER_PATH = "movie_poster_path"; // 海报
        public static final String COLUMN_VOTE_AVERAGE = "movie_vote_average"; // 平均得分
        public static final String COLUMN_POPUlARITY = "movie_popularity"; // 受欢迎度
        public static final String COLUMN_LENGTH = "movie_length"; // 片长
        public static final String COLUMN_OVERVIEW = "movie_overview"; // 剧情简介
        public static final String COLUMN_FAVORED = "movie_favored"; // 是否收藏
        public static final String COLUMN_TYPE = "movie_fetch_type"; // 拉取数据时的类型

        public static final Uri CONTENT_URI =  BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static Uri buildMovieUri (long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieFavored () {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE)
                    .appendPath("movies_favored").build();
        }

        public static Uri buildMovieIdWithFavored (int movieId, int favored) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_MOVIE)
                    .appendPath(Integer.toString(movieId))
                    .appendPath(Integer.toString(favored))
                    .build();
        }

        public static Integer getFavoredFromUri (Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(2));
        }

        public static int getMovieIdFromUri (Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }

}
