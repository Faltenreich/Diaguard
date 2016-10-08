package com.faltenreich.diaguard.event.ui;

import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.event.BaseContextEvent;

/**
 * Created by Faltenreich on 08.10.2016.
 */

public class FoodEatenRemovedEvent extends BaseContextEvent<FoodEaten> {

    public FoodEatenRemovedEvent(FoodEaten context) {
        super(context);
    }
}
