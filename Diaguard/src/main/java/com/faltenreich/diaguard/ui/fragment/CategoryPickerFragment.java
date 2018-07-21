package com.faltenreich.diaguard.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CategoryPickerFragment extends DialogFragment {

    private static final String ARGUMENT_SELECTED_CATEGORIES_STABLE_IDS = "DATE_PICKER_FRAGMENT_DATE_SELECTED";

    public static CategoryPickerFragment newInstance(@Nullable Measurement.Category[] selectedCategories, OnCategorySelectedListener listener) {
        CategoryPickerFragment fragment = new CategoryPickerFragment();
        fragment.setOnCategorySelectedListener(listener);
        Bundle bundle = new Bundle();
        if (selectedCategories != null) {
            int[] selectedCategoriesStableIds = new int[selectedCategories.length];
            for (int index = 0; index < selectedCategories.length; index++) {
                Measurement.Category selectedCategory = selectedCategories[index];
                selectedCategoriesStableIds[index] = selectedCategory.getStableId();
            }
            bundle.putIntArray(ARGUMENT_SELECTED_CATEGORIES_STABLE_IDS, selectedCategoriesStableIds);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    private OnCategorySelectedListener listener;
    private LinkedHashMap<Measurement.Category, Boolean> categoryMap;
    private ArrayList<Measurement.Category> categories;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        categoryMap = new LinkedHashMap<>();
        for (Measurement.Category activeCategory : PreferenceHelper.getInstance().getActiveCategories()) {
            categoryMap.put(activeCategory, false);
        }
        categories = new ArrayList<Measurement.Category>(categoryMap.keySet());

        if (getArguments() != null) {
            int[] selectedCategoriesStableIds = getArguments().getIntArray(ARGUMENT_SELECTED_CATEGORIES_STABLE_IDS);
            if (selectedCategoriesStableIds != null) {
                for (int stableId : selectedCategoriesStableIds) {
                    Measurement.Category selectedCategory = Measurement.Category.fromStableId(stableId);
                    if (categoryMap.containsKey(selectedCategory)) {
                        categoryMap.put(selectedCategory, true);
                    }
                }
            }
        }

        CharSequence[] titles = new CharSequence[categories.size()];
        boolean[] checkedItems = new boolean[categories.size()];
        for (int index = 0; index < categories.size(); index++) {
            Measurement.Category category = categories.get(index);
            titles[index] = category.toLocalizedString();
            checkedItems[index] = categoryMap.get(category);
        }
        return new AlertDialog.Builder(getContext())
                .setMultiChoiceItems(titles, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        categoryMap.put(categories.get(which), isChecked);
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Measurement.Category> selectedCategoryList = new ArrayList<>();
                        for (Map.Entry<Measurement.Category, Boolean> entry : categoryMap.entrySet()) {
                            if (entry.getValue()) {
                                selectedCategoryList.add(entry.getKey());
                            }
                        }
                        Measurement.Category[] selectedCategories = !selectedCategoryList.isEmpty() ? selectedCategoryList.toArray(new Measurement.Category[selectedCategoryList.size()]) : null;
                        listener.onCategoriesSelected(selectedCategories);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
    }

    public void setOnCategorySelectedListener(@NonNull OnCategorySelectedListener listener) {
        this.listener = listener;
    }

    public DateTime getDate() {
        DatePicker datePicker = ((DatePickerDialog) getDialog()).getDatePicker();
        return DateTime.now().withYear(datePicker.getYear()).withMonthOfYear(datePicker.getMonth() + 1).withDayOfMonth(datePicker.getDayOfMonth());
    }

    public void show(FragmentManager manager) {
        super.show(manager, CategoryPickerFragment.class.getSimpleName());
    }

    public interface OnCategorySelectedListener {
        void onCategoriesSelected(@Nullable Measurement.Category[] categories);
    }
}