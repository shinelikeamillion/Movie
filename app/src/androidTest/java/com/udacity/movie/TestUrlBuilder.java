package com.udacity.movie;

import com.udacity.movie.net.HttpUtils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TestUrlBuilder {

    @Test
    public void testUrlBuilder () {
        String TopRatedMovieUri = "https://api.themoviedb.org/3/movie/top_rated?api_key=f71e8d0f0******60e71ff999628d";
        String FindMovieByIdUri = "https://api.themoviedb.org/3/movie/1234?api_key=f71e8d0f0e96bb******ff999628d";
        String FindVideosUri = "https://api.themoviedb.org/3/movie/1234/videos?api_key=f71e8d0f0e9******e71ff999628d";
        String FindMoviesReviewsUri= "https://api.themoviedb.org/3/movie/1234/reviews?api_key=f71******bb1ef460e71ff999628d";
        assertEquals(TopRatedMovieUri, HttpUtils.getUri("top_rated").toString());
        assertEquals(FindMovieByIdUri, HttpUtils.getUri("1234").toString());
        assertEquals(FindVideosUri, HttpUtils.getUri("1234/videos").toString());
        assertEquals(FindMoviesReviewsUri, HttpUtils.getUri("1234/reviews").toString());
    }
}
