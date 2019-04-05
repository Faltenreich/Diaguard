package com.faltenreich.diaguard.util;

import com.faltenreich.diaguard.ui.list.item.ListItemCategoryValue;
import com.faltenreich.diaguard.data.entity.Measurement;

import java.util.List;

/**
 * Created by Faltenreich on 18.10.2015.
 */
public class ArrayUtils {

    public static long sum(long[] array) {
        long sum = 0L;
        for (long number : array) {
            sum += number;
        }
        return sum;
    }

    public static long sum(Long[] array) {
        long sum = 0L;
        for (long number : array) {
            sum += number;
        }
        return sum;
    }

    public static float sum(float[] array) {
        float sum = 0f;
        for (float number : array) {
            sum += number;
        }
        return sum;
    }

    public static float sum(Float[] array) {
        float sum = 0f;
        for (float number : array) {
            sum += number;
        }
        return sum;
    }

    public static float sum(List<Float> array) {
        float sum = 0f;
        for (float number : array) {
            sum += number;
        }
        return sum;
    }

    public static int sum(int[] array) {
        int sum = 0;
        for (int number : array) {
            sum += number;
        }
        return sum;
    }

    public static ListItemCategoryValue sum(Measurement.Category category, List<ListItemCategoryValue> items) {
        ListItemCategoryValue item = new ListItemCategoryValue(category);
        for (ListItemCategoryValue value : items) {
            item.setValueOne(item.getValueOne() + value.getValueOne());
            item.setValueTwo(item.getValueTwo() + value.getValueTwo());
        }
        return item;
    }

    public static float avg(float[] avg) {
        return sum(avg) / avg.length;
    }

    public static float avg(List<Float> avg) {
        return sum(avg) / avg.size();
    }

    public static ListItemCategoryValue avg(Measurement.Category category, List<ListItemCategoryValue> items) {
        ListItemCategoryValue item = sum(category, items);
        return new ListItemCategoryValue(category, item.getValueOne() / items.size(), item.getValueTwo() / items.size());
    }

    public static String[] toStringArray(float[] array) {
        String[] stringArray = new String[array.length];
        for (int position = 0; position < array.length; position++) {
            stringArray[position] = Float.toString(array[position]);
        }
        return stringArray;
    }
}
