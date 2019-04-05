package com.faltenreich.diaguard.data.event.data;

import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.event.BaseContextEvent;

/**
 * Created by Faltenreich on 14.11.2016.
 */

public class CategoryPinChangedEvent extends BaseContextEvent<Measurement.Category> {

    public boolean isPinned;

    public CategoryPinChangedEvent(Measurement.Category category, boolean isPinned) {
        super(category);
        this.isPinned = isPinned;
    }
}
