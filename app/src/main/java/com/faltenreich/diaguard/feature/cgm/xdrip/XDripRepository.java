package com.faltenreich.diaguard.feature.cgm.xdrip;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.faltenreich.diaguard.feature.cgm.CgmData;
import com.faltenreich.diaguard.feature.cgm.CgmRepository;

public class XDripRepository {

    private static final String TAG = XDripRepository.class.getSimpleName();

    private final CgmRepository repository = CgmRepository.getInstance();
    private final XDripMapper mapper = new XDripMapper();

    public void handleIntent(Context context, Intent intent) {
        CgmData cgmData = mapper.mapData(intent);
        if (cgmData != null) {
            Log.v(TAG, "Received CgmData: " + cgmData);
            repository.storeData(context, cgmData);
        }
    }
}
