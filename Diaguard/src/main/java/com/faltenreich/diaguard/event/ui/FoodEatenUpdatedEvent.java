package com.faltenreich.diaguard.event.ui;

import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.event.BaseContextEvent;

/**
 * Created by Faltenreich on 08.10.2016.
 */

public class FoodEatenUpdatedEvent extends BaseContextEvent<FoodEaten> {

    public FoodEatenUpdatedEvent(FoodEaten context) {
        super(context);
    }
}
