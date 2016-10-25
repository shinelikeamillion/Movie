package com.udacity.moviestepone.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ten-thousand-hours on 10/24/16.
 */

public class MovieInfo implements Parcelable{

    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";   // 默认海报前缀
    public String poster_path;                                          // 海报路径
    public String adult;                                                // 是否成人片
    public String overview;                                             // 简介
    public String release_date;                                         // 上映日期
    public int[] genre_ids;                                             // 流派
    public int id;
    public String original_title;                                       // 原始片名
    public String original_language;                                    // 语言
    public String title;                                                // 片名
    public String backdrop_path;
    public float popularity;                                           // 受欢迎度
    public int vote_count;                                              // 评分数量
    public boolean video;
    public int vote_average;                                            // 平均得分

    public MovieInfo (String poster_path,
                      String adult,
                      String overview,
                      String release_date,
                      int[] genre_ids,
                      int id,
                      String original_title,
                      String original_language,
                      String title,
                      String backdrop_path,
                      float popularity,
                      int vote_count,
                      boolean video,
                      int vote_average) {

        this.poster_path = BASE_URL.concat(poster_path);
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
        this.genre_ids = genre_ids;
        this.id = id;
        this.original_title = original_title;
        this.original_language = original_language;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.video = video;
        this.vote_average = vote_average;
    }

    protected MovieInfo(Parcel in) {
        poster_path = in.readString();
        adult = in.readString();
        overview = in.readString();
        release_date = in.readString();
        genre_ids = in.createIntArray();
        id = in.readInt();
        original_title = in.readString();
        original_language = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        popularity = in.readFloat();
        vote_count = in.readInt();
        video = in.readByte() != 0;
        vote_average = in.readInt();
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    @Override
    public String toString() {
        return "MovieInfo {\n"+
                "poster_path = " + poster_path + "\n" +
                "adult = " + adult + "\n" +
                "overview = " + overview + "\n" +
                "release_date = " + release_date + "\n" +
                "genre_ids = " + genre_ids.toString() + "\n" +
                "id = " + id + "\n" +
                "original_title = " + original_title + "\n" +
                "original_language = " + original_language + "\n" +
                "title = " + title + "\n" +
                "backdrop_path = " + backdrop_path + "\n" +
                "popularity = " + popularity + "\n" +
                "vote_count = " + vote_count + "\n" +
                "video = " + video + "\n" +
                "vote_average = " + vote_average + "\n" +
                " }";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(adult);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeIntArray(genre_ids);
        dest.writeInt(id);
        dest.writeString(original_title);
        dest.writeString(original_language);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        dest.writeFloat(popularity);
        dest.writeInt(vote_count);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeInt(vote_average);
    }
}
