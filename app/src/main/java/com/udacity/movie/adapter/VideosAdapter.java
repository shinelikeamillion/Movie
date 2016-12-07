package com.udacity.movie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.movie.R;
import com.udacity.movie.api.FetchVideosResponse.Results;

public class VideosAdapter extends ArrayAdapter<Results> {

    private Context context;
    ViewGroup.LayoutParams layoutParams;

    public VideosAdapter(Context context, Results[] results) {

        super(context, 0, results);
        Log.e("AAAAA", results.length+"");
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Results results = getItem(position);

        ImageView imageView = new ImageView(context);
        convertView = imageView;
        layoutParams = new ViewGroup.LayoutParams(100, 100);
        convertView.setLayoutParams(layoutParams);
        ((ImageView)convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (results.isYouTube()) {
            String thumbnail = String.format(context.getString(R.string.youtube_thumbnail), results.key);
            Log.e("youtube", thumbnail);
            Picasso.with(context)
                    .load(thumbnail)
                    .error(R.mipmap.ic_launcher)
                    .into((ImageView) convertView);
        }

        return convertView;
    }
}
