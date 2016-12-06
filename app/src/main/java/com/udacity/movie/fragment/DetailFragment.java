package com.udacity.movie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.udacity.movie.R;
import com.udacity.movie.api.FetchMovieByIdResponse;
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

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(Intent.EXTRA_RETURN_RESULT)) {
            mMovieInfo = intent.getParcelableExtra(Intent.EXTRA_RETURN_RESULT);

            Picasso.with(getActivity()).load(mMovieInfo.poster_path).into(mIvPoster);
            mTvTitle.setText(mMovieInfo.title);
            mTvReleaseDate.setText(String.format(getResources().getString(R.string.release_date), mMovieInfo.release_date));
            mTvPopularity.setText(String.format(getResources().getString(R.string.popularity), mMovieInfo.popularity));
            mTvVoteCount.setText(String.format(getResources().getString(R.string.vote_count), mMovieInfo.vote_count));
            mTvOverView.setText(String.format(getResources().getString(R.string.overview), mMovieInfo.overview));

            FetchDetailTask fetchDetailTask = new FetchDetailTask() {
                @Override
                public void onSuccess(String content, String task) {

                    FetchMovieByIdResponse fetchMovieByIdResponse = new Gson().fromJson(content, FetchMovieByIdResponse.class);
                    mTvRunTime.setText("Runtime: "+fetchMovieByIdResponse.runtime+"");
                }
            };
            fetchDetailTask.execute(mMovieInfo.id+"");
        }

        return rootView;
    }
}
