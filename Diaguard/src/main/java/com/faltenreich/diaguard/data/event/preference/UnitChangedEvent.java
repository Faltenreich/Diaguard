package com.faltenreich.diaguard.data.event.preference;

import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.event.BaseContextEvent;

/**
 * Created by Faltenreich on 04.05.2017
 */

public class UnitChangedEvent extends BaseContextEvent<Measurement.Category> {

    public UnitChangedEvent(Measurement.Category context) {
        super(context);
    }
}
