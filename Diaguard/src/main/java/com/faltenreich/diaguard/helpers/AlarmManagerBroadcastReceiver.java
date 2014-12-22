package com.faltenreich.diaguard.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.faltenreich.diaguard.R;

/**
 * Created by Filip on 22.12.2014.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Diaguard");
        wakeLock.acquire();

        Helper.notify(context, R.string.alarm_desc);

        PreferenceHelper preferenceHelper = new PreferenceHelper(context);
        if(preferenceHelper.isSoundAllowed()) {
            Helper.playSound(context, R.raw.fireflies);
        }
        if(preferenceHelper.isVibrationAllowed()) {
            Helper.vibrate(context, 1000);
        }

        wakeLock.release();
    }
}