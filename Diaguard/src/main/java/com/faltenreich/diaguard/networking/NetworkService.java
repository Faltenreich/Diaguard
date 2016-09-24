package com.faltenreich.diaguard.networking;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

/**
 * Created by Faltenreich on 01.08.2016.
 */
public abstract class NetworkService <API> extends RetrofitGsonSpiceService {

    private Class<API> apiClass;

    public NetworkService(Class<API> apiClass) {
        this.apiClass = apiClass;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(apiClass);
    }
}