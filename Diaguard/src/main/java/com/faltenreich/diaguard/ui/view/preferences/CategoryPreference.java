package com.faltenreich.diaguard.ui.view.preferences;

import android.app.Activity;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ViewUtils;

/**
 * Created by Filip on 04.11.13.
 */
public class CategoryPreference extends DialogPreference {

    public final static String ACTIVE = "_active";
    public final static String ACTIVE_FOR_EXPORT = "_active_for_export";

    private ListView listView;

    public CategoryPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_category);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        Activity activity = (Activity) getContext();
        listView = view.findViewById(R.id.listview);

        CategoryListAdapter adapter = new CategoryListAdapter(activity,
                android.R.layout.simple_list_item_multiple_choice,
                activity.getResources().getTextArray(R.array.categories));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        for(int item = 0; item < Measurement.Category.values().length; item++) {
            Measurement.Category category = Measurement.Category.values()[item];
            listView.setItemChecked(item, PreferenceHelper.getInstance().isCategoryActive(category));
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            SparseBooleanArray checkedItems = listView.getCheckedItemPositions();

            if (checkedItems != null && checkedItems.indexOfValue(true) != -1) {
                for (int item = 0; item < checkedItems.size(); item++) {
                    Measurement.Category category = Measurement.Category.values()[item];
                    boolean isChecked = checkedItems.valueAt(item);
                    PreferenceHelper.getInstance().setIsCategoryActive(category, isChecked);
                }
            } else {
                ViewUtils.showToast(getContext(), getContext().getString(R.string.validator_value_none));
            }
        }
    }
}