package com.faltenreich.diaguard.data.async;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class DataTask<T> extends AsyncTask<Void, Void, T> {

    private WeakReference<Context> context;
    private DataLoaderListener<T> listener;

    DataTask(Context context, DataLoaderListener<T> listener) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
    }

    protected Context getContext() {
        return context.get();
    }

    @Override
    protected T doInBackground(Void... voids) {
        return listener.onShouldLoad();
    }

    @Override
    protected void onPostExecute(T data) {
        super.onPostExecute(data);
        listener.onDidLoad(data);
    }
}
