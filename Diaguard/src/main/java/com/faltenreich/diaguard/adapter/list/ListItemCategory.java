package com.faltenreich.diaguard.adapter.list;

import com.faltenreich.diaguard.data.entity.Measurement;

/**
 * Created by Faltenreich on 15.12.2015.
 */
public class ListItemCategory extends ListItem {

    private Measurement.Category category;

    public ListItemCategory(Measurement.Category category) {
        this.category = category;
    }

    public Measurement.Category getCategory() {
        return category;
    }
}
