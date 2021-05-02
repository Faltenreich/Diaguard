package com.faltenreich.diaguard.shared.event.ui;

import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.event.BaseContextEvent;

public class FoodSearchedEvent extends BaseContextEvent<Food> {

    public FoodSearchedEvent(Food context) {
        super(context);
    }
}
