package com.faltenreich.diaguard.shared.event.preference;

import com.faltenreich.diaguard.shared.event.BaseContextEvent;

public class DecimalPlacesChangedEvent extends BaseContextEvent<Integer> {

    public DecimalPlacesChangedEvent(Integer context) {
        super(context);
    }
}
