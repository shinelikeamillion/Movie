package com.udacity.movie.data;

import android.provider.BaseColumns;

/**
 * 数据库的契约类
 */

public class MovieContract {

    // 电影表相关内容
    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "movie_original_title"; // 片名
        public static final String COLUMN_RELEASE_DATE = "movie_release_date"; // 上映日期
        public static final String COLUMN_POSTER_PATH = "movie_poster_path"; // 海报
        public static final String COLUMN_VOTE_AVERAGE = "movie_vote_average"; // 平均得分
        public static final String COLUMN_POPUlARITY = "movie_popularity"; // 受欢迎度
        public static final String COLUMN_LENGTH = "movie_length"; // 片长
        public static final String COLUMN_OVERVIEW = "movie_overview"; // 剧情简介
        public static final String COLUMN_FAVORED = "movie_favored"; // 是否收藏
    }
}
