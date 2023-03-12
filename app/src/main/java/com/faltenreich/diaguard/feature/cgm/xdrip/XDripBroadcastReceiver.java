package com.faltenreich.diaguard.feature.cgm.xdrip;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.faltenreich.diaguard.feature.config.ApplicationConfig;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;

public class XDripBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = XDripBroadcastReceiver.class.getSimpleName();

    private final XDripRepository repository = new XDripRepository();

    @Override
    public void onReceive(Context context, Intent intent) {
        repository.handleIntent(context, intent);
    }

    public static void invalidate(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, XDripBroadcastReceiver.class);
        boolean isEnabled = ApplicationConfig.isCgmSupported()
            && PreferenceStore.getInstance().shouldReadCgmDataFromXDrip();
        int state = isEnabled
            ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        packageManager.setComponentEnabledSetting(componentName, state, PackageManager.DONT_KILL_APP);
        Log.d(TAG, "BroadcastReceiver is running: " + isEnabled);
    }
}
