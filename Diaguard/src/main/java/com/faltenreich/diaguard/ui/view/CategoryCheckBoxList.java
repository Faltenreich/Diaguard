package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.list.adapter.ExportCategoryListAdapter;
import com.faltenreich.diaguard.ui.list.decoration.LinearDividerItemDecoration;
import com.faltenreich.diaguard.ui.list.item.ListItemExportCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryCheckBoxList extends RecyclerView {

    private ExportCategoryListAdapter adapter;

    public CategoryCheckBoxList(Context context) {
        super(context);
        init();
    }

    public CategoryCheckBoxList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ExportCategoryListAdapter(getContext());
            addItemDecoration(new LinearDividerItemDecoration(getContext()));
            setAdapter(adapter);
            initData();
        }
    }

    private void initData() {
        adapter.clear();

        List<ListItemExportCategory> items = new ArrayList<>();
        Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
        List<Measurement.Category> selectedCategories = Arrays.asList(PreferenceHelper.getInstance().getExportCategories());

        for (Measurement.Category category : activeCategories) {
            boolean isCategorySelected = selectedCategories.contains(category);
            boolean isExtraSelected;
            switch (category) {
                case BLOODSUGAR:
                    isExtraSelected = PreferenceHelper.getInstance().limitsAreHighlighted();
                    break;
                case INSULIN:
                    isExtraSelected = PreferenceHelper.getInstance().exportInsulinSplit();
                    break;
                case MEAL:
                    isExtraSelected = PreferenceHelper.getInstance().exportFood();
                    break;
                default:
                    isExtraSelected = false;
            }
            items.add(new ListItemExportCategory(category, isCategorySelected, isExtraSelected));
        }

        adapter.addItems(items);
        adapter.notifyDataSetChanged();
    }

    public Measurement.Category[] getSelectedCategories() {
        ArrayList<Measurement.Category> selectedCategories = new ArrayList<>();
        for (ListItemExportCategory item : adapter.getItems()) {
            if (item.isCategorySelected()) {
                selectedCategories.add(item.getCategory());
            }
        }
        return selectedCategories.toArray(new Measurement.Category[selectedCategories.size()]);
    }

    public boolean exportFood() {
        for (ListItemExportCategory item : adapter.getItems()) {
            if (item.getCategory() == Measurement.Category.MEAL) {
                return item.isExtraSelected();
            }
        }
        return false;
    }

    public boolean splitInsulin() {
        for (ListItemExportCategory item : adapter.getItems()) {
            if (item.getCategory() == Measurement.Category.INSULIN) {
                return item.isExtraSelected();
            }
        }
        return false;
    }

    public boolean highlightLimits() {
        for (ListItemExportCategory item : adapter.getItems()) {
            if (item.getCategory() == Measurement.Category.BLOODSUGAR) {
                return item.isExtraSelected();
            }
        }
        return false;
    }
}
