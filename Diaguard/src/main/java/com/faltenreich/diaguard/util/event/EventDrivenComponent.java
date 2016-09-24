package com.faltenreich.diaguard.util.event;

/**
 * Created by Faltenreich on 01.08.2016.
 */
public abstract class EventDrivenComponent {

    public void start() {
        Events.register(this);
    }

    public void stop() {
        Events.unregister(this);
    }
}
