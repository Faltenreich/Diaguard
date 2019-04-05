package com.faltenreich.diaguard.data.event.data;

import com.faltenreich.diaguard.data.event.BaseContextEvent;

/**
 * Created by Faltenreich on 10.11.2016.
 */

public class FoodQueryEndedEvent extends BaseContextEvent<Boolean> {

    public FoodQueryEndedEvent(Boolean hasMore) {
        super(hasMore);
    }
}
