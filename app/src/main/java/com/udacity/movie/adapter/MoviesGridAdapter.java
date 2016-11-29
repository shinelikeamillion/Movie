package com.udacity.movie.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.movie.R;
import com.udacity.movie.data.MovieContract.MovieEntry;

/**
 * 主页电影信息适配器
 */

public class MoviesGridAdapter extends CursorAdapter {

    private final String TAG = this.getClass().getSimpleName();
    ViewGroup.LayoutParams layoutParams;

    private static class ViewHolder {
        private final ImageView imgPoster;

        ViewHolder (ImageView view) {
            imgPoster = view;
        }
    }

    public MoviesGridAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.e(TAG, "newView");


        ImageView imageView = new ImageView(context);
        layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) context.getResources().getDimension(R.dimen.poster_height));
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ViewHolder viewHolder = new ViewHolder(imageView);
        imageView.setTag(viewHolder);

        return imageView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185"+cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH)))
                .into(viewHolder.imgPoster);
    }
}
