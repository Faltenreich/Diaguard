package com.faltenreich.diaguard.data.event.ui;

import com.faltenreich.diaguard.data.entity.FoodEaten;

/**
 * Created by Faltenreich on 08.10.2016.
 */

public class FoodEatenUpdatedEvent extends FoodEatenListEvent {

    public FoodEatenUpdatedEvent(FoodEaten context, int position) {
        super(context, position);
    }
}
