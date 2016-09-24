package com.faltenreich.diaguard.networking;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public abstract class NetworkRequest<DTO extends NetworkDto, API> extends RetrofitSpiceRequest<DTO, API> implements RequestListener<DTO> {

    public NetworkRequest(Class<DTO> dtoClass, Class<API> apiClass) {
        super(dtoClass, apiClass);
    }

    @Override
    public DTO loadDataFromNetwork() throws Exception {
        return getResponse();
    }

    @Override
    public void onRequestSuccess(DTO dto) {
        onSuccess(dto);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        onFailure(spiceException);
    }

    public abstract DTO getResponse();

    public abstract void onSuccess(DTO dto);

    public abstract void onFailure(SpiceException spiceException);
}
