package com.faltenreich.diaguard.shared.event.ui;

import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.event.BaseContextEvent;

/**
 * Created by Faltenreich on 09.10.2016.
 */

public class FoodEatenListEvent extends BaseContextEvent<FoodEaten> {

    public int position;

    public FoodEatenListEvent(FoodEaten context, int position) {
        super(context);
        this.position = position;
    }
}
