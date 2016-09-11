package com.faltenreich.diaguard.adapter.list;

import com.faltenreich.diaguard.data.entity.Food;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class ListItemFood extends ListItem {

    private Food food;

    public ListItemFood(Food food) {
        this.food = food;
    }

    public Food getFood() {
        return food;
    }
}
