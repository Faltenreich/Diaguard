package com.faltenreich.diaguard.event.ui;

import com.faltenreich.diaguard.data.entity.FoodEaten;

/**
 * Created by Faltenreich on 08.10.2016.
 */

public class FoodEatenRemovedEvent extends FoodEatenListEvent {

    public FoodEatenRemovedEvent(FoodEaten context, int position) {
        super(context, position);
    }
}
