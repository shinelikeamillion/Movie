package com.udacity.movie.fragment;

import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.udacity.movie.MyApplication;
import com.udacity.movie.R;
import com.udacity.movie.adapter.ReviewsAdapter;
import com.udacity.movie.adapter.VideosAdapter;
import com.udacity.movie.api.FetchMovieByIdResponse;
import com.udacity.movie.api.FetchReviewResponse;
import com.udacity.movie.api.FetchVideosResponse;
import com.udacity.movie.model.MovieInfo;
import com.udacity.movie.net.FetchDetailTask;
import com.udacity.movie.utils.Utility;

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
    private TextView mBtnFavored;

    private RecyclerView mVideoList;
    private RecyclerView mReviewList;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mIvPoster = (ImageView) rootView.findViewById(R.id.iv_movie_poster);
        mTvTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        mTvReleaseDate = (TextView) rootView.findViewById(R.id.tv_movie_release_date);
        mTvPopularity = (TextView) rootView.findViewById(R.id.tv_movie_popularity);
        mTvVoteCount = (TextView) rootView.findViewById(R.id.tv_movie_vote_count);
        mTvOverView = (TextView) rootView.findViewById(R.id.tv_overview);
        mTvRunTime = (TextView) rootView.findViewById(R.id.tv_runtime);
        mBtnFavored = (TextView) rootView.findViewById(R.id.btn_favorite);

        mVideoList = (RecyclerView) rootView.findViewById(R.id.rv_videos);
        mVideoList.setHasFixedSize(true);
        mVideoList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mReviewList = (RecyclerView) rootView.findViewById(R.id.rv_reviews);
        mReviewList.setHasFixedSize(true);
        mReviewList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mBtnFavored.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {

                // TODO: 12/12/16 时间紧张,先存到sharepreference 里面吧
                if (MyApplication.favoredMovieId.contains(mMovieInfo.id+"")) {
                    Utility.getFavoredMoviesPreference(getActivity()).remove(mMovieInfo.id+"");
                    mMovieInfo.favored = 0;
                } else {
                    Utility.putFavoredMoviesPreference(getActivity(), mMovieInfo.id+"");
                    mMovieInfo.favored = 1;
                }
                updateFavoredView();
            }
        });

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(Intent.EXTRA_RETURN_RESULT)) {
            mMovieInfo = intent.getParcelableExtra(Intent.EXTRA_RETURN_RESULT);

            Picasso.with(getActivity()).load(mMovieInfo.poster_path).into(mIvPoster);
            mTvTitle.setText(mMovieInfo.title);
            mTvReleaseDate.setText(String.format(getResources().getString(R.string.release_date), mMovieInfo.release_date));
            mTvPopularity.setText(String.format(getResources().getString(R.string.popularity), mMovieInfo.popularity));
            mTvVoteCount.setText(String.format(getResources().getString(R.string.vote_count), mMovieInfo.vote_count));
            mTvOverView.setText(String.format(getResources().getString(R.string.overview), mMovieInfo.overview));

            updateFavoredView();

            String movieId = String.valueOf(mMovieInfo.id);
            Log.e(TAG, movieId);

            getRuntime(movieId);

            getVideos(movieId+"/videos");

            getReviews(movieId+"/reviews");
        }

        return rootView;
    }


    private void updateFavoredView () {
        if (mMovieInfo.favored == 1) {
            mBtnFavored.setText("FAVORED");
            mBtnFavored.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            mBtnFavored.setText("MARK AS FAVORED");
            mBtnFavored.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
        }
    }

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
