package com.faltenreich.diaguard.feature.food.networking;

import android.util.Log;

import com.faltenreich.diaguard.feature.food.networking.dto.SearchResponseDto;
import com.faltenreich.diaguard.shared.data.async.DataCallback;
import com.faltenreich.diaguard.shared.networking.NetworkResponse;
import com.faltenreich.diaguard.shared.networking.NetworkService;

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

    public void search(final String query, final int page, DataCallback<SearchResponseDto> callback) {
        // Paging of this api starts at page 1
        execute(server.api.search(query != null ? query : "", JSON, page + 1, PAGE_SIZE), new NetworkResponseListener<SearchResponseDto>() {
            @Override
            public void onResponse(NetworkResponse<SearchResponseDto> response) {
                SearchResponseDto dto = response.getData();
                if (dto != null && dto.products.size() > 0) {
                    Log.d(TAG, String.format("Received %d products for '%s' on page %d (%d total)", dto.products.size(), query, page, dto.count));
                    callback.onResult(dto);
                } else {
                    callback.onResult(null);
                }
            }
            @Override
            public void onError(NetworkResponse<SearchResponseDto> response) {
                callback.onResult(null);
            }
        });
    }
}
