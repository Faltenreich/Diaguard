package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.networking.FoodSearchFailedEvent;
import com.faltenreich.diaguard.event.networking.FoodSearchSucceededEvent;
import com.faltenreich.diaguard.networking.NetworkManager;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.ProductResponseDto;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.SearchResponseDto;
import com.octo.android.robospice.persistence.exception.SpiceException;

import java.util.List;

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

    public void search(final String query) {
        execute(new OpenFoodFactsRequest<SearchResponseDto>(SearchResponseDto.class) {
            @Override
            public SearchResponseDto getResponse() {
                return getService().search(query, "Deutschland", 1);
            }
            @Override
            public void onSuccess(SearchResponseDto dto) {
                List<Food> foodList = FoodDao.getInstance().createOrUpdate(dto);
                Events.post(new FoodSearchSucceededEvent(foodList));
            }
            @Override
            public void onFailure(SpiceException spiceException) {
                Events.post(new FoodSearchFailedEvent());
            }
        });
    }
}
