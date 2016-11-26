package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.networking.FoodSearchFailedEvent;
import com.faltenreich.diaguard.networking.NetworkManager;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.SearchResponseDto;
import com.octo.android.robospice.persistence.exception.SpiceException;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class OpenFoodFactsManager extends NetworkManager<OpenFoodFactsService> {

    private static final int PAGE_SIZE = 50;

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

    public void search(String query, final int page) {
        final String finalQuery = query != null ? query : "";
        execute(new OpenFoodFactsRequest<SearchResponseDto>(SearchResponseDto.class) {
            @Override
            public SearchResponseDto getResponse() {
                return getService().search(finalQuery, 1, PAGE_SIZE, page);
            }
            @Override
            public void onSuccess(SearchResponseDto dto) {
                FoodDao.getInstance().createOrUpdate(dto);
            }
            @Override
            public void onFailure(SpiceException spiceException) {
                Events.post(new FoodSearchFailedEvent());
            }
        });
    }
}
