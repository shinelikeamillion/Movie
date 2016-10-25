package com.udacity.moviestepone.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.moviestepone.R;
import com.udacity.moviestepone.model.MovieInfo;

/**
 * 电影详情页
 */
public class DetailFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private ImageView mIvPoster;
    private TextView mTvTitle;
    private TextView mTvReleaseDate;
    private TextView mTvPopularity;
    private TextView mTvVoteCount;
    private TextView mTvOverView;

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

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(Intent.EXTRA_RETURN_RESULT)) {
            MovieInfo movieInfo = intent.getParcelableExtra(Intent.EXTRA_RETURN_RESULT);

            Picasso.with(getActivity()).load(movieInfo.poster_path).into(mIvPoster);
            mTvTitle.setText(movieInfo.title);
            mTvReleaseDate.setText(String.format(getResources().getString(R.string.release_date), movieInfo.release_date));
            mTvPopularity.setText(String.format(getResources().getString(R.string.popularity), movieInfo.popularity));
            mTvVoteCount.setText(String.format(getResources().getString(R.string.vote_count), movieInfo.vote_count));
            mTvOverView.setText(String.format(getResources().getString(R.string.overview), movieInfo.overview));
        }

        return rootView;
    }
}
