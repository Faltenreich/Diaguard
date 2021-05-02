package com.faltenreich.diaguard.shared.event.data;

import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.event.BaseContextEvent;

public class TagSavedEvent extends BaseContextEvent<Tag> {

    public TagSavedEvent(Tag tag) {
        super(tag);
    }
}
