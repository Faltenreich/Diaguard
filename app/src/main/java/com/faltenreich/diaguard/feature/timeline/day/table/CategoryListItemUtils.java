package com.faltenreich.diaguard.feature.timeline.day.table;

import com.faltenreich.diaguard.shared.data.database.entity.Category;

import java.util.List;

public class CategoryListItemUtils {

    public static CategoryValueListItem sum(Category category, List<CategoryValueListItem> items) {
        CategoryValueListItem item = new CategoryValueListItem(category);
        for (CategoryValueListItem value : items) {
            item.setValueOne(item.getValueOne() + value.getValueOne());
            item.setValueTwo(item.getValueTwo() + value.getValueTwo());
            item.setValueThree(item.getValueThree() + value.getValueThree());
        }
        return item;
    }

    public static CategoryValueListItem avg(Category category, List<CategoryValueListItem> items) {
        CategoryValueListItem item = sum(category, items);
        return new CategoryValueListItem(category, item.getValueOne() / items.size(), item.getValueTwo() / items.size(), item.getValueThree() / items.size());
    }
}
