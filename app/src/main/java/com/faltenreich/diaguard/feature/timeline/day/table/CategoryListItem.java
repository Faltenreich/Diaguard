package com.faltenreich.diaguard.feature.timeline.day.table;

import com.faltenreich.diaguard.shared.data.database.entity.Category;

/**
 * Created by Faltenreich on 15.12.2015.
 */
public class CategoryListItem {

    private Category category;

    CategoryListItem(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
}
