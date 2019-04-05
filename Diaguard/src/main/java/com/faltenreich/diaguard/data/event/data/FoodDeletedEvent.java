package com.faltenreich.diaguard.data.event.data;

import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.event.BaseContextEvent;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class FoodDeletedEvent extends BaseContextEvent<Food> {

    public FoodDeletedEvent(Food food) {
        super(food);
    }
}
