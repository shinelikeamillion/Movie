package com.udacity.movie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.movie.api.FetchReviewResponse.Results;

public class ReviewsAdapter extends ArrayAdapter<Results> {

    private Context context;
    Results[] results;

    public ReviewsAdapter(Context context, Results[] results) {
        super(context, 0, results);
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("AAAAAAAAAAA", getCount()+"");

        Results result = getItem(position);

        TextView textView = new TextView(context);
        textView.setMaxLines(2);
        convertView = textView;

        ((TextView)convertView).setText(result.content);

        return convertView;
    }

    @Override
    public int getCount() {
        return results.length;
    }
}
