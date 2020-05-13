package com.faltenreich.diaguard.shared.event.ui;

import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;

/**
 * Created by Faltenreich on 08.10.2016.
 */

public class FoodEatenUpdatedEvent extends FoodEatenListEvent {

    public FoodEatenUpdatedEvent(FoodEaten context, int position) {
        super(context, position);
    }
}
