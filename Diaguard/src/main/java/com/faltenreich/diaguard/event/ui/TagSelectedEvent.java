package com.faltenreich.diaguard.event.ui;

import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.event.BaseContextEvent;

/**
 * Created by Faltenreich on 08.01.2018
 */

public class TagSelectedEvent extends BaseContextEvent<Tag> {

    public TagSelectedEvent(Tag context) {
        super(context);
    }
}
