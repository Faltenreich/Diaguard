package com.faltenreich.diaguard.util.event;

import de.greenrobot.event.EventBus;

/**
 * Created by Faltenreich on 01.01.2016.
 */
public class Events {

    private static EventBus getEventBus() {
        return EventBus.getDefault();
    }

    public static void register(Object object) {
        if (!isRegistered(object)) {
            getEventBus().register(object);
        }
    }

    public static void unregister(Object object) {
        if (isRegistered(object)) {
            getEventBus().unregister(object);
        }
    }

    private static boolean isRegistered(Object object) {
        return getEventBus().isRegistered(object);
    }

    public static void post(Event.BaseEvent event) {
        getEventBus().post(event);
    }
}
