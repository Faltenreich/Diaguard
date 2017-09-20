package com.faltenreich.diaguard.networking;

import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;

/**
 * Created by Faltenreich on 26.01.2016.
 */
public abstract class NetworkManager <SERVICE extends SpiceService> {

    private static final String TAG = NetworkManager.class.getSimpleName();

    private SpiceManager spiceManager;

    protected NetworkManager(Class<SERVICE> serviceClass) {
        spiceManager = new SpiceManager(serviceClass);
    }

    public void start() {
        if (!isStarted()) {
            try {
                spiceManager.start(DiaguardApplication.getContext());
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }
        }
    }

    private boolean isStarted() {
        return spiceManager.isStarted();
    }

    public void stop() {
        spiceManager.shouldStop();
    }

    protected void execute(final NetworkRequest request) {
        spiceManager.execute(request, request);
    }
}
