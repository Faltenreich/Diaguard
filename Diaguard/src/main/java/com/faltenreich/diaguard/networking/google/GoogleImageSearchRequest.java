package com.faltenreich.diaguard.networking.google;

import com.faltenreich.diaguard.networking.NetworkRequest;

/**
 * Created by Faltenreich on 23.09.2016.
 */

abstract class GoogleImageSearchRequest<DTO> extends NetworkRequest<DTO, GoogleImageSearchApi> {

    GoogleImageSearchRequest(Class<DTO> dtoClass) {
        super(dtoClass, GoogleImageSearchApi.class);
    }
}
