package com.faltenreich.diaguard.shared.event.networking;

import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.event.BaseContextEvent;

import java.util.List;

/**
 * Created by Faltenreich on 25.09.2016.
 */

public class FoodSearchSucceededEvent extends BaseContextEvent<List<Food>> {

    public FoodSearchSucceededEvent(List<Food> context) {
        super(context);
    }
}
