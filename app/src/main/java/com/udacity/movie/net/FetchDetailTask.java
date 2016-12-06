package com.udacity.movie.net;

import android.os.AsyncTask;
import android.util.Log;

public abstract class FetchDetailTask extends AsyncTask<String, Void, String> {

    String task;
    public void FetchMovieTask (String task) {
        this.task = task;
    }

    public static final String TAG = FetchDetailTask.class.getSimpleName();
    public abstract void onSuccess (String content, String task);

    @Override
    protected String doInBackground(String... params) {
        return HttpUtils.doHttpGet(params[0]);
    }

    @Override
    protected void onPostExecute(String responseStr) {
        Log.d(TAG+"RESPONSE: ", responseStr);
        onSuccess(responseStr, task);
    }
}
