package com.faltenreich.diaguard.feature.export;

import com.faltenreich.diaguard.shared.data.database.entity.Category;

public class ExportCategoryListItem {

    private final Category category;
    private boolean isCategorySelected;
    private boolean isExtraSelected;
    private final boolean isExtraVisible;

    public ExportCategoryListItem(
        Category category,
        boolean isCategorySelected,
        boolean isExtraSelected,
        boolean isExtraVisible
    ) {
        this.category = category;
        this.isCategorySelected = isCategorySelected;
        this.isExtraSelected = isExtraSelected;
        this.isExtraVisible = isExtraVisible;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isCategorySelected() {
        return isCategorySelected;
    }

    public void setCategorySelected(boolean categorySelected) {
        isCategorySelected = categorySelected;
    }

    public boolean isExtraSelected() {
        return isExtraSelected;
    }

    public void setExtraSelected(boolean extraSelected) {
        isExtraSelected = extraSelected;
    }

    public boolean isExtraVisible() {
        return isExtraVisible;
    }
}
