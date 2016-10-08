package com.faltenreich.diaguard.event.ui;

import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.event.BaseContextEvent;

/**
 * Created by Faltenreich on 08.10.2016.
 */

public class FoodRemovedEvent extends BaseContextEvent<Food> {

    public FoodRemovedEvent(Food context) {
        super(context);
    }
}
