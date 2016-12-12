package com.udacity.movie.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.movie.R;
import com.udacity.movie.api.FetchVideosResponse.Results;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private Context context;
    Results[] results;

    public VideosAdapter(Context context, Results[] results) {
        this.results = results;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_video, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Results result = results[position];
        if (null != result && result.isYouTube()) {
            String thumbnail = String.format(context.getString(R.string.youtube_thumbnail), result.key);
            Log.e("youtube", thumbnail);
            Picasso.with(context)
                    .load(thumbnail)
                    .placeholder(new ColorDrawable(context.getResources().getColor(R.color.colorAccent)))
                    .error(new ColorDrawable(context.getResources().getColor(R.color.colorPrimary)))
                    .into(holder.imageView);

            holder.rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "cliked", Toast.LENGTH_SHORT).show();
                    context.startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(result.BASE_YOUTUBE_URL+result.key))
                    );
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return results.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        private View rootView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.iv_thumbnail);
            this.rootView = view;
        }
    }
}
