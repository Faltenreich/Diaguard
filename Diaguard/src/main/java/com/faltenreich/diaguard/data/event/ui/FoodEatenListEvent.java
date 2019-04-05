package com.faltenreich.diaguard.data.event.ui;

import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.event.BaseContextEvent;

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
