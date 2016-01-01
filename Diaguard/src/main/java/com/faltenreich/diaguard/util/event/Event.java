package com.faltenreich.diaguard.util.event;

import com.faltenreich.diaguard.data.entity.Entry;

/**
 * Created by Faltenreich on 01.01.2016.
 */
public abstract class Event {

    public static abstract class BaseEvent {

    }

    private static abstract class EntryEvent extends BaseEvent {
        public Entry entry;
        public EntryEvent(Entry entry) {
            this.entry = entry;
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
}
