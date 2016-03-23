package com.faltenreich.diaguard.util.event;

import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.util.TimeSpan;

/**
 * Created by Faltenreich on 01.01.2016.
 */
public abstract class Event {

    protected static abstract class BaseEvent {

    }

    private static abstract class BaseContextEvent <T> extends BaseEvent {
        public T context;
        public BaseContextEvent(T context) {
            this.context = context;
        }
    }

    private static abstract class EntryEvent extends BaseContextEvent<Entry> {
        public EntryEvent(Entry entry) {
            super(entry);
        }
    }

    public static class EntryAddedEvent extends EntryEvent {
        public EntryAddedEvent(Entry entry) {
            super(entry);
        }
    }

    public static class EntryUpdatedEvent extends EntryEvent {
        public EntryUpdatedEvent(Entry entry) {
            super(entry);
        }
    }

    public static class EntryDeletedEvent extends EntryEvent {
        public EntryDeletedEvent(Entry entry) {
            super(entry);
        }
    }

    public static class TimeSpanChangedEvent extends BaseContextEvent<TimeSpan> {
        public TimeSpanChangedEvent(TimeSpan timeSpan) {
            super(timeSpan);
        }
    }
}
