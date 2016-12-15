package com.udacity.movie.fragment;

import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    public static String DETAIL_URI = "URI";
    public static String DETAIL_FRAGMENT_TAG = "DFT";

    MovieInfo mMovieInfo;

    private final String TAG = this.getClass().getSimpleName();

    private ImageView mIvPoster;
    private TextView mTvTitle;
    private TextView mTvReleaseDate;
    private TextView mTvPopularity;
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
                    MyApplication.favoredMovieId.remove(mMovieInfo.id+"");
                    mMovieInfo.favored = MovieInfo.NOT_FAVORED;
                } else {
                    MyApplication.favoredMovieId.add(mMovieInfo.id+"");
                    mMovieInfo.favored = MovieInfo.FAVORED;
                }
                Utility.putFavoredMoviesPreference(getActivity(), MyApplication.favoredMovieId);
                updateFavoredView();
            }
        });

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(Intent.EXTRA_RETURN_RESULT)) {
            mMovieInfo = intent.getParcelableExtra(Intent.EXTRA_RETURN_RESULT);
            initData();
        }
        Bundle arguments = getArguments();
        if (null != arguments) {
            mMovieInfo = arguments.getParcelable(DETAIL_URI);
            initData();
        }

        return rootView;
    }

    private void initData () {

        Picasso.with(getActivity()).load(mMovieInfo.poster_path).into(mIvPoster);
        mTvTitle.setText(mMovieInfo.title);
        mTvReleaseDate.setText(String.format(getResources().getString(R.string.release_date), mMovieInfo.release_date));
        mTvPopularity.setText(String.format(getResources().getString(R.string.popularity), mMovieInfo.popularity));
        mTvOverView.setText(String.format(getResources().getString(R.string.overview), mMovieInfo.overview));

        updateFavoredView();

        String movieId = String.valueOf(mMovieInfo.id);

        getRuntime(movieId);

        getVideos(movieId+"/videos");

        getReviews(movieId+"/reviews");
    }

    private void updateFavoredView () {
        if (mMovieInfo.favored == MovieInfo.FAVORED) {
            mBtnFavored.setText(getResources().getString(R.string.favored));
            mBtnFavored.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            mBtnFavored.setText(getResources().getString(R.string.mark_as_favored));
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
