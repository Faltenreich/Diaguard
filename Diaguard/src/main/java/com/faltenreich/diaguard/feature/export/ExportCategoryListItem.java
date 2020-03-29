package com.faltenreich.diaguard.feature.export;

import com.faltenreich.diaguard.shared.data.database.entity.Category;

public class ExportCategoryListItem {

    private Category category;
    private boolean isCategorySelected;
    private boolean isExtraSelected;

    public ExportCategoryListItem(Category category, boolean isCategorySelected, boolean isExtraSelected) {
        this.category = category;
        this.isCategorySelected = isCategorySelected;
        this.isExtraSelected = isExtraSelected;
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
}
