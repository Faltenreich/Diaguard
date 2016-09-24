package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.networking.NetworkManager;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.ProductResponseDto;
import com.octo.android.robospice.persistence.exception.SpiceException;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class OpenFoodFactsManager extends NetworkManager<OpenFoodFactsService> {

    private static OpenFoodFactsManager instance;

    public static OpenFoodFactsManager getInstance() {
        if (instance == null) {
            instance = new OpenFoodFactsManager();
        }
        return instance;
    }

    private OpenFoodFactsManager() {
        super(OpenFoodFactsService.class);
    }

    public void getProduct(final String productId) {
        execute(new OpenFoodFactsRequest<ProductResponseDto>(ProductResponseDto.class) {
            @Override
            public ProductResponseDto getResponse() {
                return getService().getProduct(productId);
            }
            @Override
            public void onSuccess(ProductResponseDto dto) {

            }
            @Override
            public void onFailure(SpiceException spiceException) {

            }
        });
    }
}
