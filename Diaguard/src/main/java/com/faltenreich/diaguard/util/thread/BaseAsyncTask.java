package com.faltenreich.diaguard.util.thread;

import android.content.Context;
import android.os.AsyncTask;

public abstract class BaseAsyncTask <Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private Context context;
    private OnAsyncProgressListener<Result> onAsyncProgressListener;

    BaseAsyncTask(Context context, OnAsyncProgressListener<Result> onAsyncProgressListener) {
        this.context = context;
        this.onAsyncProgressListener = onAsyncProgressListener;
    }

    protected Context getContext() {
        return context;
    }

    @Override
    protected void onPostExecute(Result data) {
        if (onAsyncProgressListener != null) {
            onAsyncProgressListener.onPostExecute(data);
        }
    }

    public interface OnAsyncProgressListener <Result> {
        void onPostExecute(Result result);
    }
}