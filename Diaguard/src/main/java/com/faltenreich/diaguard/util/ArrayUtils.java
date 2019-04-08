package com.faltenreich.diaguard.util;

import com.faltenreich.diaguard.ui.list.item.ListItemCategoryValue;
import com.faltenreich.diaguard.data.entity.Measurement;

import java.util.List;

/**
 * Created by Faltenreich on 18.10.2015.
 */
public class ArrayUtils {

    public static float sum(float[] array) {
        float sum = 0f;
        for (float number : array) {
            sum += number;
        }
        return sum;
    }

    public static ListItemCategoryValue sum(Measurement.Category category, List<ListItemCategoryValue> items) {
        ListItemCategoryValue item = new ListItemCategoryValue(category);
        for (ListItemCategoryValue value : items) {
            item.setValueOne(item.getValueOne() + value.getValueOne());
            item.setValueTwo(item.getValueTwo() + value.getValueTwo());
            item.setValueThree(item.getValueThree() + value.getValueThree());
        }
        return item;
    }

    public static float avg(float[] avg) {
        return sum(avg) / avg.length;
    }

    public static ListItemCategoryValue avg(Measurement.Category category, List<ListItemCategoryValue> items) {
        ListItemCategoryValue item = sum(category, items);
        return new ListItemCategoryValue(category, item.getValueOne() / items.size(), item.getValueTwo() / items.size(), item.getValueThree() / items.size());
    }
}
