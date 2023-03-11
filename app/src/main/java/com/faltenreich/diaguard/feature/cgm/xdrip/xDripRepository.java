package com.faltenreich.diaguard.feature.cgm.xdrip;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.faltenreich.diaguard.feature.cgm.CgmData;
import com.faltenreich.diaguard.feature.cgm.CgmRepository;

public class xDripRepository {

    private static final String TAG = xDripRepository.class.getSimpleName();

    private final CgmRepository repository = new CgmRepository();
    private final xDripMapper mapper = new xDripMapper();

    public void toggleBroadcastReceiver(Context context, boolean isEnabled) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, xDripBroadcastReceiver.class);
        int state = isEnabled
            ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        packageManager.setComponentEnabledSetting(componentName, state, PackageManager.DONT_KILL_APP);
        Log.d(TAG, "Toggled xDripBroadcastReceiver: " + isEnabled);
    }

    public void handleIntent(Intent intent) {
        CgmData cgmData = mapper.mapData(intent);
        if (cgmData != null) {
            Log.v(TAG, "Received CgmData: " + cgmData);
            repository.storeData(cgmData);
        }
    }
}
