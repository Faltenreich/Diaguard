package com.faltenreich.diaguard.networking;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.event.EventDrivenComponent;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;

/**
 * Created by Faltenreich on 26.01.2016.
 */
public class NetworkManager <SERVICE extends SpiceService> extends EventDrivenComponent {

    private SpiceManager spiceManager;

    protected NetworkManager(Class<SERVICE> serviceClass) {
        spiceManager = new SpiceManager(serviceClass);
    }

    @Override
    public void start() {
        //super.start();
        spiceManager.start(DiaguardApplication.getContext());
    }

    @Override
    public void stop() {
        super.stop();
        spiceManager.shouldStop();
    }

    protected void execute(NetworkRequest request) {
        spiceManager.execute(request, request);
    }
}
