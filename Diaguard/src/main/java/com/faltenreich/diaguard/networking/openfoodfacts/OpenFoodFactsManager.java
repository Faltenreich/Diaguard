package com.faltenreich.diaguard.networking.openfoodfacts;

import android.util.Log;

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

    private static final String TAG = OpenFoodFactsManager.class.getSimpleName();
    private static final int JSON = 1;
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

    public void search(final String query, final int page) {
        final String finalQuery = query != null ? query : "";
        execute(new OpenFoodFactsRequest<SearchResponseDto>(SearchResponseDto.class) {
            @Override
            public SearchResponseDto getResponse() {
                // Paging of this api starts at page 1
                return getService().search(finalQuery, JSON, PAGE_SIZE, page + 1);
            }
            @Override
            public void onSuccess(SearchResponseDto dto) {
                Log.d(TAG, String.format("Received %d products for '%s' on page %d", dto.products.size(), query, page));
                FoodDao.getInstance().createOrUpdate(dto);
            }
            @Override
            public void onFailure(SpiceException spiceException) {
                Events.post(new FoodSearchFailedEvent());
            }
        });
    }
}
