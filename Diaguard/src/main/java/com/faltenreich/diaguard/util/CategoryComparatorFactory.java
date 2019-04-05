package com.faltenreich.diaguard.util;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class CategoryComparatorFactory {

    private static CategoryComparatorFactory instance;

    public static CategoryComparatorFactory getInstance() {
        if (instance == null) {
            instance = new CategoryComparatorFactory();
        }
        return instance;
    }

    private HashMap<Measurement.Category, Integer> sortCache;

    private CategoryComparatorFactory() {

    }

    public void invalidate() {
        sortCache = new HashMap<>();
        Comparator<Measurement.Category> comparator = (first, second) -> PreferenceHelper.getInstance().getCategorySortIndex(first) - PreferenceHelper.getInstance().getCategorySortIndex(second);
        List<Measurement.Category> categories = PreferenceHelper.getInstance().getSortedCategories(comparator);
        for (int sortIndex = 0; sortIndex < categories.size(); sortIndex++) {
            Measurement.Category category = categories.get(sortIndex);
            sortCache.put(category, sortIndex);
        }
    }

    private HashMap<Measurement.Category, Integer> getSortCache() {
        if (sortCache == null) {
            invalidate();
        }
        return sortCache;
    }

    public Comparator<Measurement.Category> createComparatorFromCategories() {
        return (first, second) -> getSortCache().get(first) - getSortCache().get(second);
    }

    public Comparator<Measurement> createComparatorFromMeasurements() {
        return (first, second) -> getSortCache().get(first.getCategory()) - getSortCache().get(second.getCategory());
    }
}
