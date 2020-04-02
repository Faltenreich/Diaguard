package com.faltenreich.diaguard.shared.event.data;

import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.event.BaseContextEvent;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class FoodSavedEvent extends BaseContextEvent<Food> {

    public FoodSavedEvent(Food food) {
        super(food);
    }
}
