package com.faltenreich.diaguard.event;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Faltenreich on 01.01.2016.
 */
public class Events {

    private static final String TAG = Events.class.getSimpleName();

    private static EventBus getEventBus() {
        return EventBus.getDefault();
    }

    public static void register(Object object) {
        if (!isRegistered(object)) {
            Log.d(TAG, "Registered " + object.getClass().getSimpleName());
            getEventBus().register(object);
        }
    }

    public static void unregister(Object object) {
        if (isRegistered(object)) {
            Log.d(TAG, "Unregistered " + object.getClass().getSimpleName());
            getEventBus().unregister(object);
        }
    }

    private static boolean isRegistered(Object object) {
        return getEventBus().isRegistered(object);
    }

    public static void post(BaseEvent event) {
        Log.d(TAG, "Posted " + event.getClass().getSimpleName());
        getEventBus().post(event);
    }
}
