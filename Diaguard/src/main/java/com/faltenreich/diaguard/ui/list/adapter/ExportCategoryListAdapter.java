package com.faltenreich.diaguard.ui.list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.list.item.ListItemExportCategory;
import com.faltenreich.diaguard.ui.list.viewholder.ExportCategoryViewHolder;

import java.util.ArrayList;

public class ExportCategoryListAdapter extends BaseAdapter<ListItemExportCategory, ExportCategoryViewHolder> {

    public ExportCategoryListAdapter(Context context) {
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

    public Measurement.Category[] getSelectedCategories() {
        ArrayList<Measurement.Category> selectedCategories = new ArrayList<>();
        for (ListItemExportCategory item : getItems()) {
            if (item.isCategorySelected()) {
                selectedCategories.add(item.getCategory());
            }
        }
        return selectedCategories.toArray(new Measurement.Category[0]);
    }

    public boolean exportFood() {
        for (ListItemExportCategory item : getItems()) {
            if (item.getCategory() == Measurement.Category.MEAL) {
                return item.isExtraSelected();
            }
        }
        return false;
    }

    public boolean splitInsulin() {
        for (ListItemExportCategory item : getItems()) {
            if (item.getCategory() == Measurement.Category.INSULIN) {
                return item.isExtraSelected();
            }
        }
        return false;
    }

    public boolean highlightLimits() {
        for (ListItemExportCategory item : getItems()) {
            if (item.getCategory() == Measurement.Category.BLOODSUGAR) {
                return item.isExtraSelected();
            }
        }
        return false;
    }
}
