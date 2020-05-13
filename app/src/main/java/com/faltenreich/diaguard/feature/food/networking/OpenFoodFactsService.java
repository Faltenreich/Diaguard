package com.faltenreich.diaguard.feature.food.networking;

import android.util.Log;

import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.networking.FoodSearchFailedEvent;
import com.faltenreich.diaguard.shared.event.networking.FoodSearchSucceededEvent;
import com.faltenreich.diaguard.shared.networking.NetworkResponse;
import com.faltenreich.diaguard.shared.networking.NetworkService;
import com.faltenreich.diaguard.feature.food.networking.dto.SearchResponseDto;

import java.util.ArrayList;
import java.util.List;

public class OpenFoodFactsService extends NetworkService<OpenFoodFactsServer> {

    private static final String TAG = OpenFoodFactsService.class.getSimpleName();
    private static final int JSON = 1;
    private static final int PAGE_SIZE = 50;

    private static OpenFoodFactsService instance;

    public static OpenFoodFactsService getInstance() {
        if (instance == null) {
            instance = new OpenFoodFactsService();
        }
        return instance;
    }

    private OpenFoodFactsService() {
        super(new OpenFoodFactsServer());
    }

    public void search(final String query, final int page) {
        // Paging of this api starts at page 1
        execute(server.api.search(query != null ? query : "", JSON, page + 1, PAGE_SIZE), new NetworkResponseListener<SearchResponseDto>() {
            @Override
            public void onResponse(NetworkResponse<SearchResponseDto> response) {
                SearchResponseDto dto = response.getData();
                if (dto != null && dto.products.size() > 0) {
                    Log.d(TAG, String.format("Received %d products for '%s' on page %d (%d total)", dto.products.size(), query, page, dto.count));
                    List<Food> foodList = FoodDao.getInstance().createOrUpdate(dto);
                    Events.post(new FoodSearchSucceededEvent(foodList));
                } else {
                    Events.post(new FoodSearchSucceededEvent(new ArrayList<>()));
                }
            }
            @Override
            public void onError(NetworkResponse<SearchResponseDto> response) {
                Events.post(new FoodSearchFailedEvent());
            }
        });
    }
}
