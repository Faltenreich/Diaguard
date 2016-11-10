package com.faltenreich.diaguard.networking;

import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public abstract class NetworkRequest<DTO extends NetworkDto, API> extends RetrofitSpiceRequest<DTO, API> implements RequestListener<DTO> {

    private static final String TAG = NetworkRequest.class.getSimpleName();

    public NetworkRequest(Class<DTO> dtoClass, Class<API> apiClass) {
        super(dtoClass, apiClass);
    }

    @Override
    public DTO loadDataFromNetwork() throws Exception {
        return getResponse();
    }

    @Override
    public void onRequestSuccess(final DTO dto) {
        new Thread(new Runnable() {
            public void run() {
                onSuccess(dto);
            }
        }).start();
    }

    @Override
    public void onRequestFailure(final SpiceException spiceException) {
        new Thread(new Runnable() {
            public void run() {
                Log.e(TAG, spiceException.getLocalizedMessage());
                onFailure(spiceException);
            }
        }).start();
    }

    public abstract DTO getResponse();

    public abstract void onSuccess(DTO dto);

    public abstract void onFailure(SpiceException spiceException);
}
