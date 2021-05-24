package com.faltenreich.diaguard.shared.data.async;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class DataTask<T> extends AsyncTask<Void, Void, T> {

    private final WeakReference<Context> context;
    private final DataLoaderListener<T> listener;

    DataTask(Context context, DataLoaderListener<T> listener) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
    }

    @Override
    protected T doInBackground(Void... voids) {
        return listener.onShouldLoad(context.get());
    }

    @Override
    protected void onPostExecute(T data) {
        super.onPostExecute(data);
        listener.onDidLoad(data);
    }
}
