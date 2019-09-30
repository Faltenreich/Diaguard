package com.faltenreich.diaguard.ui.list.item;

import com.faltenreich.diaguard.data.entity.Measurement;

public class ListItemExportCategory {

    private Measurement.Category category;
    private boolean isCategorySelected;
    private boolean isExtraSelected;

    public ListItemExportCategory(Measurement.Category category, boolean isCategorySelected, boolean isExtraSelected) {
        this.category = category;
        this.isCategorySelected = isCategorySelected;
        this.isExtraSelected = isExtraSelected;
    }

    public Measurement.Category getCategory() {
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
