package com.faltenreich.diaguard.shared.event.preference;

import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.event.BaseContextEvent;

/**
 * Created by Faltenreich on 04.05.2017
 */

public class UnitChangedEvent extends BaseContextEvent<Category> {

    public UnitChangedEvent(Category context) {
        super(context);
    }
}
