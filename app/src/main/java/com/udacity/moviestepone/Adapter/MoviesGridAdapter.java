package com.udacity.moviestepone.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.moviestepone.R;
import com.udacity.moviestepone.model.MovieInfo;

import java.util.ArrayList;

/**
 * 主页电影信息适配器
 */

public class MoviesGridAdapter extends ArrayAdapter<MovieInfo>{

    private final String TAG = this.getClass().getSimpleName();
    ViewGroup.LayoutParams layoutParams;
    private Context context;

    public MoviesGridAdapter(Context context, ArrayList<MovieInfo> movieInfos) {
        super(context, 0, movieInfos);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieInfo movieInfo = getItem(position);

        if (convertView == null) {
            ImageView imageView = new ImageView(context);
            convertView = imageView;
            layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) context.getResources().getDimension(R.dimen.poster_height));
            convertView.setLayoutParams(layoutParams);
            ((ImageView)convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        Picasso.with(context)
                .load(movieInfo.poster_path)
                .into((ImageView) convertView);

        return convertView;
    }
}
