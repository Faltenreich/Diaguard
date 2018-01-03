package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.Helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Faltenreich on 21.10.2015.
 */
public class CategoryCheckBoxList extends LinearLayout {

    private static final int PADDING = (int) Helper.getDPI(R.dimen.padding);

    private LinkedHashMap<Measurement.Category, Boolean> categories;

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
            this.categories = new LinkedHashMap<>();

            for (final Measurement.Category category : PreferenceHelper.getInstance().getActiveCategories()) {
                boolean isActive = PreferenceHelper.getInstance().isCategoryActiveForExport(category);
                categories.put(category, isActive);

                CheckBox checkBox = new CheckBox(getContext());
                checkBox.setMinimumHeight((int) getResources().getDimension(R.dimen.height_element));
                checkBox.setText(category.toLocalizedString());
                checkBox.setChecked(isActive);
                checkBox.setPadding(PADDING, PADDING, PADDING, PADDING);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        categories.put(category, isChecked);
                    }
                });

                addView(checkBox);
            }
        }
    }

    public List<Measurement.Category> getSelectedCategories() {
        ArrayList<Measurement.Category> selectedCategories = new ArrayList<>();
        for (Map.Entry<Measurement.Category, Boolean> mapEntry : categories.entrySet()) {
            if (mapEntry.getValue()) {
                selectedCategories.add(mapEntry.getKey());
            }
        }
        return selectedCategories;
    }
}
