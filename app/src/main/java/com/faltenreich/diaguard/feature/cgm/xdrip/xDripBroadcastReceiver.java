package com.faltenreich.diaguard.feature.cgm.xdrip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.faltenreich.diaguard.feature.cgm.CgmData;
import com.faltenreich.diaguard.feature.cgm.CgmRepository;

public class xDripBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = xDripBroadcastReceiver.class.getSimpleName();

    private final xDripMapper mapper;
    private final CgmRepository repository;

    public xDripBroadcastReceiver() {
        this.mapper = new xDripMapper();
        this.repository = new CgmRepository();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        CgmData cgmData = mapper.mapData(intent);
        if (cgmData != null) {
            Log.v(TAG, "Received CgmData: " + cgmData);
            repository.store(context, cgmData);
        }
    }
}
