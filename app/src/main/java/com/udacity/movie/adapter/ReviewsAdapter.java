package com.udacity.movie.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.udacity.movie.R;
import com.udacity.movie.adapter.ReviewsAdapter.ViewHolder;
import com.udacity.movie.api.FetchReviewResponse.Results;

public class ReviewsAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    Results[] results;

    public ReviewsAdapter(Context context, Results[] results ) {
        this.context = context;
        this.results = results;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_for_review, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Results result = results[position];
        holder.review.setText(String.format(context.getResources().getString(R.string.review_author), result.content));
        holder.author.setText(String.format(context.getResources().getString(R.string.review_author), result.author));

        holder.rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage(String.format(context.getResources().getString(R.string.review_author), result.content))
                        .create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView review;
        private AppCompatTextView author;
        private View rootView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.review = (AppCompatTextView) itemView.findViewById(R.id.tv_review_content);
            this.author = (AppCompatTextView) itemView.findViewById(R.id.tv_review_author);
            this.rootView = itemView;
        }
    }
}
