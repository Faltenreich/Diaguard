package com.faltenreich.diaguard.feature.food.networking;

import com.faltenreich.diaguard.feature.food.networking.dto.SearchResponseDto;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.async.DataCallback;
import com.faltenreich.diaguard.shared.networking.NetworkResponse;
import com.faltenreich.diaguard.shared.networking.NetworkService;

public class OpenFoodFactsService extends NetworkService<OpenFoodFactsServer> {

    private static final String TAG = OpenFoodFactsService.class.getSimpleName();
    private static final int PAGE_SIZE = 50;
    private static final int JSON = 1;

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

    public void search(String query, int page, DataCallback<SearchResponseDto> callback) {
        execute(server.api.search(
            query != null ? query : "",
            page + 1, // Paging of this api starts at page 1
            PAGE_SIZE,
            Helper.getCountryCode(),
            Helper.getLanguageCode(),
            JSON
        ), new NetworkResponseListener<SearchResponseDto>() {
            @Override
            public void onResponse(NetworkResponse<SearchResponseDto> response) {
                callback.onResult(response.getData());
            }
            @Override
            public void onError(NetworkResponse<SearchResponseDto> response) {
                callback.onResult(null);
            }
        });
    }
}
