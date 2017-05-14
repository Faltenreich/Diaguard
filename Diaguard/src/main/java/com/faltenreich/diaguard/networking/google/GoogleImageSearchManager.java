package com.faltenreich.diaguard.networking.google;

import android.util.Log;

import com.faltenreich.diaguard.networking.NetworkManager;
import com.faltenreich.diaguard.networking.google.dto.GoogleImageDto;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;

import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class GoogleImageSearchManager extends NetworkManager<GoogleImageSearchService> {

    private static final String TAG = GoogleImageSearchManager.class.getSimpleName();
    private static final String TBM = "isch";
    private static final String LICENSE = "sur:fc";

    private static final String JSON_START = "rg_meta\">";
    private static final String JSON_END = "}";

    private static GoogleImageSearchManager instance;

    public static GoogleImageSearchManager getInstance() {
        if (instance == null) {
            instance = new GoogleImageSearchManager();
        }
        return instance;
    }

    private GoogleImageSearchManager() {
        super(GoogleImageSearchService.class);
    }

    public void search(final String query, final ImageGrabCallback callback) {
        execute(new GoogleImageSearchRequest<String>(String.class) {
            @Override
            public String getResponse() {
                Response response = getService().search(query, TBM, LICENSE);
                return new String(((TypedByteArray) response.getBody()).getBytes());
            }
            @Override
            public void onSuccess(String body) {
                try {
                    int jsonStart = body.indexOf(JSON_START);
                    int jsonEnd = body.indexOf(JSON_END, jsonStart);
                    String json = body.substring(jsonStart + JSON_START.length(), jsonEnd + JSON_END.length());
                    GoogleImageDto dto = new Gson().fromJson(json, GoogleImageDto.class);
                    callback.onSuccess(dto.imageUrl);
                } catch (Exception exception) {
                    Log.e(TAG, exception.getMessage());
                    callback.onError();
                }
            }
            @Override
            public void onFailure(SpiceException spiceException) {
                callback.onError();
            }
        });
    }

    public interface ImageGrabCallback {
        void onSuccess(String url);
        void onError();
    }
}
