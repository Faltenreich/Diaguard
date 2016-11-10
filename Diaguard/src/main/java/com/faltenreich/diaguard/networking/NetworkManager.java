package com.faltenreich.diaguard.networking;

import com.faltenreich.diaguard.DiaguardApplication;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;

/**
 * Created by Faltenreich on 26.01.2016.
 */
public abstract class NetworkManager <SERVICE extends SpiceService> {

    private SpiceManager spiceManager;

    protected NetworkManager(Class<SERVICE> serviceClass) {
        spiceManager = new SpiceManager(serviceClass);
    }

    public void start() {
        spiceManager.start(DiaguardApplication.getContext());
    }

    public void stop() {
        spiceManager.shouldStop();
    }

    protected void execute(final NetworkRequest request) {
        spiceManager.execute(request, request);
    }
}
