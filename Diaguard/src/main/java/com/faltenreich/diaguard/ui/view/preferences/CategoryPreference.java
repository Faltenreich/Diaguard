package com.faltenreich.diaguard.ui.view.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.ViewHelper;

/**
 * Created by Filip on 04.11.13.
 */
public class CategoryPreference extends DialogPreference {

    public final static String ACTIVE = "_active";

    private ListView listView;

    public CategoryPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_category);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        Activity activity = (Activity) getContext();
        listView = (ListView) view.findViewById(R.id.listview);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(activity,
                android.R.layout.simple_list_item_multiple_choice,
                activity.getResources().getTextArray(R.array.categories));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        if(Build.VERSION.SDK_INT <= 10) {
            listView.setBackgroundColor(Color.WHITE);
        }

        for(int item = 0; item < Measurement.Category.values().length; item++) {
            Measurement.Category category = Measurement.Category.values()[item];
            listView.setItemChecked(item, PreferenceHelper.getInstance().isCategoryActive(category));
        }
        // TODO: listView.getChildAt(Measurement.Category.BLOODSUGAR.ordinal()).setEnabled(false);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            SparseBooleanArray checkedItems = listView.getCheckedItemPositions();

            if(checkedItems != null && checkedItems.indexOfValue(true) != -1) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                for (int item = 0; item < checkedItems.size(); item++)
                    editor.putBoolean(Measurement.Category.values()[item].name() + ACTIVE, checkedItems.valueAt(item));
                editor.apply();
            }
            else {
                ViewHelper.showToast(getContext(), getContext().getString(R.string.validator_value_none));
                // TODO: Keep Dialog open
            }
        }
    }
}