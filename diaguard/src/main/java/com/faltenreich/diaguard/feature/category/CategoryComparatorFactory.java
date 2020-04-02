package com.faltenreich.diaguard.feature.category;

import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;

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

    private HashMap<Category, Integer> sortCache;

    private CategoryComparatorFactory() {

    }

    public void invalidate() {
        sortCache = new HashMap<>();
        Comparator<Category> comparator = (first, second) -> PreferenceHelper.getInstance().getCategorySortIndex(first) - PreferenceHelper.getInstance().getCategorySortIndex(second);
        List<Category> categories = PreferenceHelper.getInstance().getSortedCategories(comparator);
        for (int sortIndex = 0; sortIndex < categories.size(); sortIndex++) {
            Category category = categories.get(sortIndex);
            sortCache.put(category, sortIndex);
        }
    }

    private HashMap<Category, Integer> getSortCache() {
        if (sortCache == null) {
            invalidate();
        }
        return sortCache;
    }

    private int getSortIndex(Category category) {
        HashMap<Category, Integer> cache = getSortCache();
        Integer sortIndex = cache != null && cache.containsKey(category) ? cache.get(category) : null;
        return sortIndex != null ? sortIndex : 0;
    }

    public Comparator<Category> createComparatorFromCategories() {
        return (first, second) -> getSortIndex(first) - getSortIndex(second);
    }

    public Comparator<Measurement> createComparatorFromMeasurements() {
        return (first, second) -> getSortIndex(first.getCategory()) - getSortIndex(second.getCategory());
    }
}
