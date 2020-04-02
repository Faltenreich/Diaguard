package com.faltenreich.diaguard.feature.export;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

import java.util.ArrayList;

class ExportCategoryListAdapter extends BaseAdapter<ExportCategoryListItem, ExportCategoryViewHolder> {

    ExportCategoryListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ExportCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExportCategoryViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_export_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ExportCategoryViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    Category[] getSelectedCategories() {
        ArrayList<Category> selectedCategories = new ArrayList<>();
        for (ExportCategoryListItem item : getItems()) {
            if (item.isCategorySelected()) {
                selectedCategories.add(item.getCategory());
            }
        }
        return selectedCategories.toArray(new Category[0]);
    }

    boolean exportFood() {
        for (ExportCategoryListItem item : getItems()) {
            if (item.getCategory() == Category.MEAL) {
                return item.isExtraSelected();
            }
        }
        return false;
    }

    boolean splitInsulin() {
        for (ExportCategoryListItem item : getItems()) {
            if (item.getCategory() == Category.INSULIN) {
                return item.isExtraSelected();
            }
        }
        return false;
    }

    boolean highlightLimits() {
        for (ExportCategoryListItem item : getItems()) {
            if (item.getCategory() == Category.BLOODSUGAR) {
                return item.isExtraSelected();
            }
        }
        return false;
    }
}
