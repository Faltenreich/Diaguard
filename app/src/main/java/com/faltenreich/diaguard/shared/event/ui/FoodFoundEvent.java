package com.faltenreich.diaguard.shared.event.ui;

import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.event.BaseContextEvent;

public class FoodFoundEvent extends BaseContextEvent<Food> {

    public FoodFoundEvent(Food context) {
        super(context);
    }
}
