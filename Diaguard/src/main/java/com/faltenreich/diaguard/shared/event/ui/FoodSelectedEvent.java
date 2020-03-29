package com.faltenreich.diaguard.shared.event.ui;

import android.view.View;

import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.event.BaseContextEvent;

/**
 * Created by Faltenreich on 08.10.2016.
 */

public class FoodSelectedEvent extends BaseContextEvent<Food> {

    public View view;

    public FoodSelectedEvent(Food context, View view) {
        super(context);
        this.view = view;
    }
}
