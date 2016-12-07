package com.udacity.movie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.udacity.movie.R;
import com.udacity.movie.adapter.ReviewsAdapter;
import com.udacity.movie.adapter.VideosAdapter;
import com.udacity.movie.api.FetchMovieByIdResponse;
import com.udacity.movie.api.FetchReviewResponse;
import com.udacity.movie.api.FetchVideosResponse;
import com.udacity.movie.model.MovieInfo;
import com.udacity.movie.net.FetchDetailTask;

/**
 * 电影详情页
 */
public class DetailFragment extends Fragment {

    MovieInfo mMovieInfo;

    private final String TAG = this.getClass().getSimpleName();

    private ImageView mIvPoster;
    private TextView mTvTitle;
    private TextView mTvReleaseDate;
    private TextView mTvPopularity;
    private TextView mTvVoteCount;
    private TextView mTvOverView;
    private TextView mTvRunTime;

    private ListView mVideoList;
    private ListView mReviewList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mIvPoster = (ImageView) rootView.findViewById(R.id.iv_movie_poster);
        mTvTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        mTvReleaseDate = (TextView) rootView.findViewById(R.id.tv_movie_release_date);
        mTvPopularity = (TextView) rootView.findViewById(R.id.tv_movie_popularity);
        mTvVoteCount = (TextView) rootView.findViewById(R.id.tv_movie_vote_count);
        mTvOverView = (TextView) rootView.findViewById(R.id.tv_overview);
        mTvRunTime = (TextView) rootView.findViewById(R.id.tv_runtime);

        mVideoList = (ListView) rootView.findViewById(R.id.lv_videos);
        mReviewList = (ListView) rootView.findViewById(R.id.lv_reviews);

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(Intent.EXTRA_RETURN_RESULT)) {
            mMovieInfo = intent.getParcelableExtra(Intent.EXTRA_RETURN_RESULT);

            Picasso.with(getActivity()).load(mMovieInfo.poster_path).into(mIvPoster);
            mTvTitle.setText(mMovieInfo.title);
            mTvReleaseDate.setText(String.format(getResources().getString(R.string.release_date), mMovieInfo.release_date));
            mTvPopularity.setText(String.format(getResources().getString(R.string.popularity), mMovieInfo.popularity));
            mTvVoteCount.setText(String.format(getResources().getString(R.string.vote_count), mMovieInfo.vote_count));
            mTvOverView.setText(String.format(getResources().getString(R.string.overview), mMovieInfo.overview));

            String movieId = String.valueOf(mMovieInfo.id);

            getRuntime(movieId);

            getVideos(movieId+"/videos");

            getReviews(movieId+"/reviews");
        }

        return rootView;
    }

    // FIXME: 12/8/16 : Why there is shown one view
    private void getReviews(String key) {
        new FetchDetailTask() {
            @Override
            public void onSuccess(String content) {
                FetchReviewResponse fetchReviewResponse = new Gson().fromJson(content, FetchReviewResponse.class);
                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity(), fetchReviewResponse.results);
                mReviewList.setAdapter(reviewsAdapter);
            }
        }.execute(key);
    }

    private void getVideos(String key) {
        new FetchDetailTask() {

            @Override
            public void onSuccess(String content) {
                FetchVideosResponse fetchVideosResponse = new Gson().fromJson(content, FetchVideosResponse.class);
                VideosAdapter videosAdapter = new VideosAdapter(getActivity(), fetchVideosResponse.results);
                mVideoList.setAdapter(videosAdapter);
            }
        }.execute(key);
    }

    private void getRuntime(String movieId) {
        new FetchDetailTask() {
            @Override
            public void onSuccess(String content) {

                FetchMovieByIdResponse fetchMovieByIdResponse = new Gson().fromJson(content, FetchMovieByIdResponse.class);
                mTvRunTime.setText("Runtime: "+fetchMovieByIdResponse.runtime+"");
            }
        }.execute(movieId);
    }
}
