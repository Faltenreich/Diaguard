package com.faltenreich.diaguard.event.data;

import com.faltenreich.diaguard.event.BaseContextEvent;

/**
 * Created by Faltenreich on 10.11.2016.
 */

public class FoodQueryEndedEvent extends BaseContextEvent<Boolean> {

    public FoodQueryEndedEvent(Boolean hasMore) {
        super(hasMore);
    }
}
