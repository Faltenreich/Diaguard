package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.networking.NetworkRequest;
import com.faltenreich.diaguard.networking.NetworkDto;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public abstract class OpenFoodFactsRequest <DTO extends NetworkDto> extends NetworkRequest<DTO, OpenFoodFactsApi> {

    public OpenFoodFactsRequest(Class<DTO> dtoClass) {
        super(dtoClass, OpenFoodFactsApi.class);
    }
}
