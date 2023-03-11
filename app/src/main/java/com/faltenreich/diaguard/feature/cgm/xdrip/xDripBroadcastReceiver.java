package com.faltenreich.diaguard.feature.cgm.xdrip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class xDripBroadcastReceiver extends BroadcastReceiver {

    private final xDripRepository repository = new xDripRepository();

    @Override
    public void onReceive(Context context, Intent intent) {
        repository.handleIntent(context, intent);
    }
}
